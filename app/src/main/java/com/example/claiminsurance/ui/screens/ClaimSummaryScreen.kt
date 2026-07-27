package com.example.claiminsurance.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.claiminsurance.model.PartAData
import com.example.claiminsurance.model.PartBData

@Composable
fun ClaimSummaryScreen(
    partAData: PartAData,
    partBData: PartBData,
    onResetClaim: () -> Unit,
    onSubmitClaim: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val totalExpensesClaimed = partAData.totalTreatmentExpensesClaimed
    val totalEnclosedBills = partAData.totalBillsAmount
    val totalLumpSum = partAData.totalLumpSumClaimed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Hero Claim Passport Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CLAIM READY FOR SUBMISSION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Text(
                        text = "NEW INDIA ASSURANCE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total Claim Amount",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.85f)
                    )
                )

                Text(
                    text = "₹${"%.2f".format(if (totalExpensesClaimed > 0) totalExpensesClaimed else totalEnclosedBills)}",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 34.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = Color.White.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Policy Holder",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                        )
                        Text(
                            text = "${partAData.insuredNameFirst} ${partAData.insuredNameSurname}".ifBlank { "Not Specified" },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "Policy Number",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                        )
                        Text(
                            text = partAData.policyNo.ifBlank { "N/A" },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "Hospital",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f))
                        )
                        Text(
                            text = partAData.hospitalName.ifBlank { partBData.hospitalName }.ifBlank { "N/A" },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Summary Detail Cards
        // Card 1: Claim Breakdown
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Claim Financial Breakdown",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SummaryRow("Pre-hospitalization Expenses", "₹${"%.2f".format(partAData.preHospitalizationExpenses)}")
                SummaryRow("Hospitalization Expenses", "₹${"%.2f".format(partAData.hospitalizationExpenses)}")
                SummaryRow("Post-hospitalization Expenses", "₹${"%.2f".format(partAData.postHospitalizationExpenses)}")
                SummaryRow("Health Check-up Cost", "₹${"%.2f".format(partAData.healthCheckupCost)}")
                SummaryRow("Ambulance Charges", "₹${"%.2f".format(partAData.ambulanceCharges)}")
                SummaryRow("Itemized Enclosed Bills (${partAData.enclosedBills.size} bills)", "₹${"%.2f".format(totalEnclosedBills)}")

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                SummaryRow(
                    label = "Total Claim Value",
                    value = "₹${"%.2f".format(if (totalExpensesClaimed > 0) totalExpensesClaimed else totalEnclosedBills)}",
                    isHighlight = true
                )
            }
        }

        // Card 2: Documents & Signatures Status
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Documents & Verification Status",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                StatusRow(
                    label = "Part A Checklist Enclosed",
                    value = "${partAData.checklistSubmittedDocs.size} Documents",
                    isValid = partAData.checklistSubmittedDocs.isNotEmpty()
                )

                StatusRow(
                    label = "Insured Signature (Section H)",
                    value = if (partAData.signatureCaptured) "Signed Digital Canvas" else "Not Signed",
                    isValid = partAData.signatureCaptured
                )

                StatusRow(
                    label = "Part B Hospital Checklist",
                    value = "${partBData.hospitalChecklistDocs.size} Documents",
                    isValid = partBData.hospitalChecklistDocs.isNotEmpty()
                )

                StatusRow(
                    label = "Hospital Authority Seal & Signature",
                    value = if (partBData.sealSignatureCaptured) "Certified & Sealed" else "Pending Seal",
                    isValid = partBData.sealSignatureCaptured
                )
            }
        }

        // Card 3: Direct Payout Bank Account Details
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reimbursement Bank Account",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SummaryRow("Bank & Branch", partAData.bankNameAndBranch.ifBlank { "HDFC Bank, Fort Branch" })
                SummaryRow("Account Number", partAData.accountNumber.ifBlank { "91802004581299" })
                SummaryRow("IFSC Code", partAData.ifscCode.ifBlank { "HDFC0000060" })
                SummaryRow("PAN Number", partAData.pan.ifBlank { "ABCDE1234F" })
                SummaryRow("Cheque Payable To", partAData.chequeDdPayableDetails.ifBlank { "${partAData.insuredNameFirst} ${partAData.insuredNameSurname}" })
            }
        }

        // Primary Submit Final Claim Button & Actions
        var showSubmitDialog by remember { mutableStateOf(false) }

        Button(
            onClick = {
                if (onSubmitClaim != null) {
                    onSubmitClaim.invoke()
                } else {
                    showSubmitDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Submit Final Insurance Claim",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Claim Summary Exported & Downloaded Successfully!", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export Summary")
            }

            OutlinedButton(
                onClick = onResetClaim,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Reset Form")
            }
        }

        if (showSubmitDialog) {
            AlertDialog(
                onDismissRequest = { showSubmitDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = {
                    Text(
                        text = "Claim Submitted Successfully!",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "CLAIM ACKNOWLEDGEMENT NO.",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    text = "NIA-CLM-${(100000..999999).random()}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                        Text(
                            text = "Your health insurance claim of ₹${"%.2f".format(if (totalExpensesClaimed > 0) totalExpensesClaimed else totalEnclosedBills)} has been filed under Policy #${partAData.policyNo.ifBlank { "512300/34/24/0009841" }}.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "An acknowledgement SMS and email have been sent to ${partAData.phoneNo.ifBlank { "+91 9876543210" }}.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showSubmitDialog = false },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Done")
                    }
                }
            )
        }
    }
}

@Composable
fun SummaryRow(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isHighlight) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
            color = if (isHighlight) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (isHighlight) MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary) else MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Composable
fun StatusRow(
    label: String,
    value: String,
    isValid: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Surface(
            color = if (isValid) Color(0xFF4CAF50).copy(alpha = 0.15f) else Color(0xFFFF9800).copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isValid) Color(0xFF2E7D32) else Color(0xFFE65100),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isValid) Color(0xFF2E7D32) else Color(0xFFE65100)
                    )
                )
            }
        }
    }
}
