package com.example.claiminsurance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.claiminsurance.model.DemoClaimDataGenerator
import com.example.claiminsurance.model.FormTab
import com.example.claiminsurance.model.PartAData
import com.example.claiminsurance.model.PartBData
import com.example.claiminsurance.ui.components.ClaimHeader
import com.example.claiminsurance.ui.screens.ClaimSummaryScreen
import com.example.claiminsurance.ui.screens.PartAScreen
import com.example.claiminsurance.ui.screens.PartBScreen

@Composable
fun ClaimAppScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(FormTab.PART_A) }

    var partAData by remember { mutableStateOf(PartAData()) }
    var partBData by remember { mutableStateOf(PartBData()) }

    Scaffold(
        topBar = {
            ClaimHeader(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onQuickAutofill = {
                    partAData = DemoClaimDataGenerator.generatePartA()
                    partBData = DemoClaimDataGenerator.generatePartB()
                    Toast.makeText(context, "⚡ Demo Claim Form Autofilled Successfully!", Toast.LENGTH_SHORT).show()
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (selectedTab) {
                        FormTab.PART_A -> {
                            Button(
                                onClick = {
                                    selectedTab = FormTab.PART_B
                                    Toast.makeText(context, "Part A Saved! Please complete Part B Hospital Details.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = "Submit Part A & Continue to Part B",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                            }
                        }
                        FormTab.PART_B -> {
                            Button(
                                onClick = {
                                    selectedTab = FormTab.SUMMARY
                                    Toast.makeText(context, "Part B Saved! Reviewing Claim Summary.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                            ) {
                                Text(
                                    text = "Submit Hospital Form & View Summary",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                            }
                        }
                        FormTab.SUMMARY -> {
                            Button(
                                onClick = {
                                    selectedTab = FormTab.PAYOUT_CONFIRMATION
                                    Toast.makeText(context, "🎉 Claim Submitted! Payout Advice Dispatched.", Toast.LENGTH_LONG).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(imageVector = Icons.Default.Verified, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Submit Final Insurance Claim",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                        FormTab.PAYOUT_CONFIRMATION -> {
                            OutlinedButton(
                                onClick = {
                                    partAData = PartAData()
                                    partBData = PartBData()
                                    selectedTab = FormTab.PART_A
                                    Toast.makeText(context, "Claim Form Reset for New Submission", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Start New Insurance Claim",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                FormTab.PART_A -> {
                    PartAScreen(
                        partAData = partAData,
                        onDataChange = { partAData = it },
                        onSubmitPartA = {
                            selectedTab = FormTab.PART_B
                            Toast.makeText(context, "Part A Submitted!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                FormTab.PART_B -> {
                    PartBScreen(
                        partBData = partBData,
                        onDataChange = { partBData = it },
                        onSubmitPartB = {
                            selectedTab = FormTab.SUMMARY
                            Toast.makeText(context, "Part B Submitted!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                FormTab.SUMMARY -> {
                    ClaimSummaryScreen(
                        partAData = partAData,
                        partBData = partBData,
                        onResetClaim = {
                            partAData = PartAData()
                            partBData = PartBData()
                            selectedTab = FormTab.PART_A
                            Toast.makeText(context, "Claim Form Reset", Toast.LENGTH_SHORT).show()
                        },
                        onSubmitClaim = {
                            selectedTab = FormTab.PAYOUT_CONFIRMATION
                            Toast.makeText(context, "🎉 Claim Submitted! Payout Advice Dispatched.", Toast.LENGTH_LONG).show()
                        }
                    )
                }
                FormTab.PAYOUT_CONFIRMATION -> {
                    com.example.claiminsurance.ui.screens.PayoutConfirmationScreen(
                        partAData = partAData,
                        partBData = partBData,
                        onReturnHome = {
                            partAData = PartAData()
                            partBData = PartBData()
                            selectedTab = FormTab.PART_A
                        }
                    )
                }
            }
        }
    }
}
