package com.example.claiminsurance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.claiminsurance.model.EnclosedBill

@Composable
fun BillTableManager(
    bills: List<EnclosedBill>,
    onAddBill: (EnclosedBill) -> Unit,
    onDeleteBill: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Enclosed Bills List (${bills.size})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Bill", style = MaterialTheme.typography.labelMedium)
            }
        }

        if (bills.isEmpty()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "No bills added yet. Tap '+ Add Bill' to add hospital/pharmacy receipts.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            bills.forEachIndexed { index, bill ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "#${index + 1}",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = bill.towards.ifBlank { "Hospital Expense" },
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bill No: ${bill.billNo} • Date: ${bill.date} • Issued: ${bill.issuedBy}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${"%.2f".format(bill.amount)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            IconButton(onClick = { onDeleteBill(bill.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Bill",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }

            // Total Bar
            val totalAmount = bills.sumOf { it.amount }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Enclosed Bills Amount",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "₹${"%.2f".format(totalAmount)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddBillDialog(
            slNo = bills.size + 1,
            onDismiss = { showAddDialog = false },
            onConfirm = { newBill ->
                onAddBill(newBill)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddBillDialog(
    slNo: Int,
    onDismiss: () -> Unit,
    onConfirm: (EnclosedBill) -> Unit
) {
    var billNo by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var issuedBy by remember { mutableStateOf("") }
    var towards by remember { mutableStateOf("Hospital Main Bill") }
    var amountText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Enclosed Bill Details") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                RadioGroupField(
                    title = "Category / Towards",
                    options = listOf("Hospital Main Bill", "Pre-hospitalization", "Post-hospitalization", "Pharmacy"),
                    selectedOption = towards,
                    onOptionSelected = { towards = it }
                )
                StandardTextField(
                    value = billNo,
                    onValueChange = { billNo = it },
                    label = "Bill No",
                    placeholder = "e.g. B-10928"
                )
                DatePickerField(
                    value = date,
                    onDateSelected = { date = it },
                    label = "Bill Date"
                )
                StandardTextField(
                    value = issuedBy,
                    onValueChange = { issuedBy = it },
                    label = "Issued By",
                    placeholder = "Hospital / Pharmacy Name"
                )
                StandardTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = "Amount (₹)",
                    placeholder = "0.00",
                    keyboardType = KeyboardType.Number
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    onConfirm(
                        EnclosedBill(
                            slNo = slNo,
                            billNo = billNo,
                            date = date,
                            issuedBy = issuedBy,
                            towards = towards,
                            amount = amount
                        )
                    )
                }
            ) {
                Text("Add Bill")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
