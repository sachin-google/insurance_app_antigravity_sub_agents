#!/usr/bin/env bash
# Called by .agents/hooks.json. Reads the hook payload from stdin and appends
# one JSONL line per event to .agents/metrics/sessions/<conversationId>.jsonl.
# On a Stop event, kicks off report.py for the just-finished session.
#
# Args: <event_kind>   one of: subagent_end | stop
#
# The hook must never fail loudly — it exits 0 unconditionally so agent runs
# are never blocked by instrumentation.
set +e
EVENT_KIND="${1:-unknown}"

# jq is required for defensive JSON extraction. If missing, log a stub and
# exit cleanly — the collector degrades to invocation-count-only mode.
if ! command -v jq >/dev/null 2>&1; then
  mkdir -p .agents/metrics/sessions
  echo "{\"ts\":\"$(date -u +%Y-%m-%dT%H:%M:%SZ)\",\"kind\":\"$EVENT_KIND\",\"error\":\"jq not installed\"}" \
    >> .agents/metrics/sessions/unknown.jsonl
  exit 0
fi

PAYLOAD=$(cat)
CID=$(printf '%s' "$PAYLOAD"      | jq -r '.conversationId // "unknown"')
STEP=$(printf '%s' "$PAYLOAD"     | jq -r '.stepIdx // empty')
TPATH=$(printf '%s' "$PAYLOAD"    | jq -r '.transcriptPath // empty')
# invoke_subagent's args field may hold the sub-agent name under several keys;
# try each in turn.
SUBAGENT=$(printf '%s' "$PAYLOAD" | jq -r '
  .toolCall.args.name //
  .toolCall.args.agent //
  .toolCall.args.subagent //
  .toolCall.args.agentName //
  empty
')

mkdir -p .agents/metrics/sessions
OUT=".agents/metrics/sessions/${CID}.jsonl"

jq -cn \
  --arg ts       "$(date -u +%Y-%m-%dT%H:%M:%SZ)" \
  --arg kind     "$EVENT_KIND" \
  --arg cid      "$CID" \
  --arg step     "$STEP" \
  --arg subagent "$SUBAGENT" \
  --arg tpath    "$TPATH" \
  '{ts:$ts, kind:$kind, conversation_id:$cid, step_idx:$step, subagent:$subagent, transcript_path:$tpath}' \
  >> "$OUT" 2>/dev/null

# On Stop, run the aggregator against just this session.
if [ "$EVENT_KIND" = "stop" ] && [ -x .agents/metrics/report.py ]; then
  .agents/metrics/report.py "$OUT" > ".agents/metrics/report-${CID}.md" 2>/dev/null
fi

exit 0
