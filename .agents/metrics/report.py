#!/usr/bin/env python3
"""Aggregate .agents/metrics/sessions/*.jsonl into a token / cost report.

Usage:
    report.py [session_file.jsonl ...]
    report.py --all      # every session under .agents/metrics/sessions/

For an A/B baseline vs sub-agent comparison, save a baseline report as
`.agents/metrics/baseline.md` (rename an earlier `report-*.md`); this script
prints a pointer to it so the two are easy to diff.
"""
import glob, json, os, re, sys
from collections import defaultdict

HERE = os.path.dirname(os.path.abspath(__file__))
PRICING_PATH = os.path.join(HERE, 'pricing.json')

def load_pricing():
    with open(PRICING_PATH) as fh:
        return json.load(fh)

def load_agent_tiers():
    """Map subagent name -> model tier by parsing .agents/agents/*.md frontmatter."""
    tiers = {}
    for path in glob.glob('.agents/agents/*.md'):
        try:
            with open(path) as fh:
                fm = fh.read().split('---')[1]
            name  = re.search(r'^name:\s*(\S+)',  fm, re.M).group(1)
            model = re.search(r'^model:\s*(\S+)', fm, re.M).group(1)
            tiers[name] = model
        except Exception:
            continue
    return tiers

def try_extract_tokens(transcript_path):
    """Best-effort parse transcript.jsonl for token usage. Returns list of
    dicts {model, in_tok, out_tok, cached_tok}. Empty list if the schema
    doesn't expose usage — invocation counts still work in that case."""
    if not transcript_path or not os.path.exists(transcript_path):
        return []
    events = []
    with open(transcript_path) as fh:
        for line in fh:
            try:
                obj = json.loads(line)
            except Exception:
                continue
            usage = (obj.get('usage')
                     or obj.get('tokenUsage')
                     or obj.get('token_usage'))
            if not usage:
                continue
            in_tok  = (usage.get('input_tokens')     or usage.get('inputTokens')
                       or usage.get('promptTokens')  or 0)
            out_tok = (usage.get('output_tokens')    or usage.get('outputTokens')
                       or usage.get('completionTokens') or 0)
            cached  = (usage.get('cached_tokens')    or usage.get('cachedTokens')
                       or usage.get('cache_read_tokens') or 0)
            model   = obj.get('model') or obj.get('modelId') or ''
            events.append(dict(model=model, in_tok=in_tok, out_tok=out_tok, cached_tok=cached))
    return events

def cost(pricing, tier, in_t, out_t, cached_t):
    p = pricing.get(tier, {})
    billable_in = max(0, in_t - cached_t)
    return (billable_in * p.get('in_per_1M',     0)
          + cached_t    * p.get('cached_per_1M', 0)
          + out_t       * p.get('out_per_1M',    0)) / 1_000_000

def main():
    args = sys.argv[1:]
    if not args or args == ['--all']:
        files = sorted(glob.glob('.agents/metrics/sessions/*.jsonl'))
    else:
        files = args
    if not files:
        print("No session files found under .agents/metrics/sessions/")
        return 1

    pricing = load_pricing()
    tiers   = load_agent_tiers()

    per_agent = defaultdict(lambda: dict(invocations=0, in_tok=0, out_tok=0, cached_tok=0))
    main_agent = dict(in_tok=0, out_tok=0, cached_tok=0)
    transcripts_seen = set()

    for f in files:
        with open(f) as fh:
            for line in fh:
                try:
                    ev = json.loads(line)
                except Exception:
                    continue
                if ev.get('kind') == 'subagent_end':
                    per_agent[ev.get('subagent') or 'unknown']['invocations'] += 1
                tpath = ev.get('transcript_path')
                if tpath and tpath not in transcripts_seen:
                    transcripts_seen.add(tpath)
                    for tok in try_extract_tokens(tpath):
                        # Attribution note: without a per-event agent-name field in
                        # transcript.jsonl we can't split main-vs-sub cleanly.
                        # Assume everything is main-agent for now — sub-agents get
                        # only their invocation counts. Refine once schema is known.
                        main_agent['in_tok']     += tok['in_tok']
                        main_agent['out_tok']    += tok['out_tok']
                        main_agent['cached_tok'] += tok['cached_tok']

    lines = []
    lines.append('# Token / Cost Report')
    lines.append('')
    lines.append('| Agent | Model | Invocations | In tok | Out tok | Cached | Est. $ |')
    lines.append('| --- | --- | ---: | ---: | ---: | ---: | ---: |')

    total_cost = 0.0
    total_in = total_out = 0

    if main_agent['in_tok'] or main_agent['out_tok']:
        c = cost(pricing, 'pro', main_agent['in_tok'],
                 main_agent['out_tok'], main_agent['cached_tok'])
        total_cost += c
        total_in   += main_agent['in_tok']
        total_out  += main_agent['out_tok']
        lines.append(f"| main | pro | — | {main_agent['in_tok']:,} | "
                     f"{main_agent['out_tok']:,} | {main_agent['cached_tok']:,} | ${c:.4f} |")

    for name, agg in sorted(per_agent.items()):
        tier = tiers.get(name, 'flash')
        c = cost(pricing, tier, agg['in_tok'], agg['out_tok'], agg['cached_tok'])
        total_cost += c
        total_in   += agg['in_tok']
        total_out  += agg['out_tok']
        lines.append(f"| {name} | {tier} | {agg['invocations']} | {agg['in_tok']:,} | "
                     f"{agg['out_tok']:,} | {agg['cached_tok']:,} | ${c:.4f} |")

    lines.append('')
    lines.append(f"**Total tokens:** {total_in:,} in / {total_out:,} out")
    lines.append(f"**Total est. cost:** ${total_cost:.4f}")
    lines.append('')

    if total_in == 0 and any(a['invocations'] for a in per_agent.values()):
        lines.append('_Token counts were not present in `transcript.jsonl`; '
                     'invocation counts above are still reliable. Cross-check '
                     "the dollar figure against Antigravity's built-in usage panel._")

    baseline = '.agents/metrics/baseline.md'
    if os.path.exists(baseline):
        lines.append('')
        lines.append('---')
        lines.append(f'Compare with `{baseline}` for the A/B story.')

    print('\n'.join(lines))
    return 0

if __name__ == '__main__':
    sys.exit(main())
