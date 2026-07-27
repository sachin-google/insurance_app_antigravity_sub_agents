package com.example.claiminsurance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.claiminsurance.model.ClaimChecklistOptions
import com.example.claiminsurance.model.EnclosedBill
import com.example.claiminsurance.model.PartAData
import com.example.claiminsurance.ui.components.*

@Composable
fun PartAScreen(
    partAData: PartAData,
    onDataChange: (PartAData) -> Unit,
    onSubmitPartA: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Section Banner
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "PART A: TO BE FILLED IN BY THE INSURED",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                    Text(
                        text = "Please fill in all block details accurately for your health insurance claim.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // --- SECTION A: DETAILS OF PRIMARY INSURED ---
        SectionCard(
            sectionLetter = "A",
            title = "Details of Primary Insured",
            subtitle = "Policy Number, ID, Name and Communication Address",
            icon = Icons.Default.Badge
        ) {
            StandardTextField(
                value = partAData.policyNo,
                onValueChange = { onDataChange(partAData.copy(policyNo = it)) },
                label = "a) Policy No."
            )
            StandardTextField(
                value = partAData.slNoCertificateNo,
                onValueChange = { onDataChange(partAData.copy(slNoCertificateNo = it)) },
                label = "Sl. No / Certificate No."
            )
            StandardTextField(
                value = partAData.companyTpaIdNo,
                onValueChange = { onDataChange(partAData.copy(companyTpaIdNo = it)) },
                label = "c) Company / TPA ID No."
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partAData.insuredNameSurname,
                    onValueChange = { onDataChange(partAData.copy(insuredNameSurname = it)) },
                    label = "Surname",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.insuredNameFirst,
                    onValueChange = { onDataChange(partAData.copy(insuredNameFirst = it)) },
                    label = "First Name",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.insuredNameMiddle,
                    onValueChange = { onDataChange(partAData.copy(insuredNameMiddle = it)) },
                    label = "Middle Name",
                    modifier = Modifier.weight(1f)
                )
            }

            StandardTextField(
                value = partAData.address,
                onValueChange = { onDataChange(partAData.copy(address = it)) },
                label = "e) Address",
                singleLine = false
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partAData.city,
                    onValueChange = { onDataChange(partAData.copy(city = it)) },
                    label = "City",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.state,
                    onValueChange = { onDataChange(partAData.copy(state = it)) },
                    label = "State",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partAData.pinCode,
                    onValueChange = { onDataChange(partAData.copy(pinCode = it)) },
                    label = "Pin Code",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.phoneNo,
                    onValueChange = { onDataChange(partAData.copy(phoneNo = it)) },
                    label = "Phone No.",
                    keyboardType = KeyboardType.Phone,
                    modifier = Modifier.weight(1f)
                )
            }

            StandardTextField(
                value = partAData.emailId,
                onValueChange = { onDataChange(partAData.copy(emailId = it)) },
                label = "Email ID",
                keyboardType = KeyboardType.Email
            )
        }

        // --- SECTION B: DETAILS OF INSURANCE HISTORY ---
        SectionCard(
            sectionLetter = "B",
            title = "Details of Insurance History",
            subtitle = "Prior policy coverage & hospitalization history",
            icon = Icons.Default.History
        ) {
            SwitchField(
                title = "a) Currently covered by any other Mediclaim / Health Insurance?",
                checked = partAData.coveredOtherMediclaim,
                onCheckedChange = { onDataChange(partAData.copy(coveredOtherMediclaim = it)) }
            )

            if (partAData.coveredOtherMediclaim) {
                DatePickerField(
                    value = partAData.dateCommencementFirstInsurance,
                    onDateSelected = { onDataChange(partAData.copy(dateCommencementFirstInsurance = it)) },
                    label = "b) Date of commencement of first insurance without break"
                )
                StandardTextField(
                    value = partAData.otherInsuranceCompany,
                    onValueChange = { onDataChange(partAData.copy(otherInsuranceCompany = it)) },
                    label = "c) Company Name"
                )
                StandardTextField(
                    value = partAData.otherPolicyNo,
                    onValueChange = { onDataChange(partAData.copy(otherPolicyNo = it)) },
                    label = "Policy No."
                )
                StandardTextField(
                    value = partAData.sumInsured,
                    onValueChange = { onDataChange(partAData.copy(sumInsured = it)) },
                    label = "Sum Insured (Rs.)",
                    keyboardType = KeyboardType.Number
                )
            }

            SwitchField(
                title = "d) Have you been hospitalized in last four years since inception?",
                checked = partAData.hospitalizedLastFourYears,
                onCheckedChange = { onDataChange(partAData.copy(hospitalizedLastFourYears = it)) }
            )

            if (partAData.hospitalizedLastFourYears) {
                DatePickerField(
                    value = partAData.hospitalizationDate,
                    onDateSelected = { onDataChange(partAData.copy(hospitalizationDate = it)) },
                    label = "Hospitalization Date"
                )
                StandardTextField(
                    value = partAData.diagnosisHistory,
                    onValueChange = { onDataChange(partAData.copy(diagnosisHistory = it)) },
                    label = "Diagnosis Details"
                )
            }
        }

        // --- SECTION C: DETAILS OF INSURED PERSON HOSPITALIZED ---
        SectionCard(
            sectionLetter = "C",
            title = "Details of Insured Person Hospitalized",
            subtitle = "Patient personal details and relationship",
            icon = Icons.Default.Person
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partAData.patientSurname,
                    onValueChange = { onDataChange(partAData.copy(patientSurname = it)) },
                    label = "Surname",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.patientFirst,
                    onValueChange = { onDataChange(partAData.copy(patientFirst = it)) },
                    label = "First Name",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.patientMiddle,
                    onValueChange = { onDataChange(partAData.copy(patientMiddle = it)) },
                    label = "Middle Name",
                    modifier = Modifier.weight(1f)
                )
            }

            RadioGroupField(
                title = "b) Gender",
                options = listOf("Male", "Female"),
                selectedOption = partAData.gender,
                onOptionSelected = { onDataChange(partAData.copy(gender = it)) }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partAData.ageYears,
                    onValueChange = { onDataChange(partAData.copy(ageYears = it)) },
                    label = "Age (Years)",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.ageMonths,
                    onValueChange = { onDataChange(partAData.copy(ageMonths = it)) },
                    label = "Age (Months)",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                DatePickerField(
                    value = partAData.dateOfBirth,
                    onDateSelected = { onDataChange(partAData.copy(dateOfBirth = it)) },
                    label = "Date of Birth",
                    modifier = Modifier.weight(1f)
                )
            }

            RadioGroupField(
                title = "e) Relationship to Primary Insured",
                options = listOf("Self", "Spouse", "Child", "Father", "Mother", "Other"),
                selectedOption = partAData.relationshipToPrimary,
                onOptionSelected = { onDataChange(partAData.copy(relationshipToPrimary = it)) }
            )

            RadioGroupField(
                title = "f) Occupation",
                options = listOf("Service", "Self Employed", "Homemaker", "Student", "Retired", "Other"),
                selectedOption = partAData.occupation,
                onOptionSelected = { onDataChange(partAData.copy(occupation = it)) }
            )
        }

        // --- SECTION D: DETAILS OF HOSPITALIZATION ---
        SectionCard(
            sectionLetter = "D",
            title = "Details of Hospitalization",
            subtitle = "Hospital name, admission/discharge timeline & cause",
            icon = Icons.Default.LocalHospital
        ) {
            StandardTextField(
                value = partAData.hospitalName,
                onValueChange = { onDataChange(partAData.copy(hospitalName = it)) },
                label = "a) Name of Hospital where Admitted"
            )

            RadioGroupField(
                title = "b) Room Category occupied",
                options = listOf("Day care", "Single occupancy", "Twin sharing", "3 or more beds per room"),
                selectedOption = partAData.roomCategory,
                onOptionSelected = { onDataChange(partAData.copy(roomCategory = it)) }
            )

            RadioGroupField(
                title = "c) Hospitalization due to",
                options = listOf("Injury", "Illness", "Maternity"),
                selectedOption = partAData.hospitalizationDueTo,
                onOptionSelected = { onDataChange(partAData.copy(hospitalizationDueTo = it)) }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerField(
                    value = partAData.dateOfAdmission,
                    onDateSelected = { onDataChange(partAData.copy(dateOfAdmission = it)) },
                    label = "Date of Admission",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.timeOfAdmission,
                    onValueChange = { onDataChange(partAData.copy(timeOfAdmission = it)) },
                    label = "Time (HH:MM)",
                    placeholder = "09:30",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerField(
                    value = partAData.dateOfDischarge,
                    onDateSelected = { onDataChange(partAData.copy(dateOfDischarge = it)) },
                    label = "Date of Discharge",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.timeOfDischarge,
                    onValueChange = { onDataChange(partAData.copy(timeOfDischarge = it)) },
                    label = "Time (HH:MM)",
                    placeholder = "14:00",
                    modifier = Modifier.weight(1f)
                )
            }

            StandardTextField(
                value = partAData.systemOfMedicine,
                onValueChange = { onDataChange(partAData.copy(systemOfMedicine = it)) },
                label = "j) System of Medicine",
                placeholder = "Allopathy / Ayurveda / Homeopathy"
            )
        }

        // --- SECTION E: DETAILS OF CLAIM ---
        SectionCard(
            sectionLetter = "E",
            title = "Details of Claim & Documents",
            subtitle = "Claim breakdown amounts and supporting document checklist",
            icon = Icons.Default.Calculate,
            badgeText = "Total Claim: ₹${"%.2f".format(partAData.totalTreatmentExpensesClaimed)}",
            badgeColor = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = "a) Details of Treatment Expenses Claimed (Rs.)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = if (partAData.preHospitalizationExpenses > 0) partAData.preHospitalizationExpenses.toString() else "",
                    onValueChange = { onDataChange(partAData.copy(preHospitalizationExpenses = it.toDoubleOrNull() ?: 0.0)) },
                    label = "i. Pre-hospitalization",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = if (partAData.hospitalizationExpenses > 0) partAData.hospitalizationExpenses.toString() else "",
                    onValueChange = { onDataChange(partAData.copy(hospitalizationExpenses = it.toDoubleOrNull() ?: 0.0)) },
                    label = "ii. Hospitalization",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = if (partAData.postHospitalizationExpenses > 0) partAData.postHospitalizationExpenses.toString() else "",
                    onValueChange = { onDataChange(partAData.copy(postHospitalizationExpenses = it.toDoubleOrNull() ?: 0.0)) },
                    label = "iii. Post-hospitalization",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = if (partAData.healthCheckupCost > 0) partAData.healthCheckupCost.toString() else "",
                    onValueChange = { onDataChange(partAData.copy(healthCheckupCost = it.toDoubleOrNull() ?: 0.0)) },
                    label = "iv. Health Check-up",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = if (partAData.ambulanceCharges > 0) partAData.ambulanceCharges.toString() else "",
                    onValueChange = { onDataChange(partAData.copy(ambulanceCharges = it.toDoubleOrNull() ?: 0.0)) },
                    label = "v. Ambulance Charges",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = if (partAData.otherExpensesAmount > 0) partAData.otherExpensesAmount.toString() else "",
                    onValueChange = { onDataChange(partAData.copy(otherExpensesAmount = it.toDoubleOrNull() ?: 0.0)) },
                    label = "vi. Others (Amount)",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Document Checklist
            DocumentChecklist(
                availableDocuments = ClaimChecklistOptions.partADocuments,
                selectedDocuments = partAData.checklistSubmittedDocs,
                onToggleDocument = { doc ->
                    val current = partAData.checklistSubmittedDocs.toMutableSet()
                    if (current.contains(doc)) current.remove(doc) else current.add(doc)
                    onDataChange(partAData.copy(checklistSubmittedDocs = current))
                }
            )
        }

        // --- SECTION F: DETAILS OF BILLS ENCLOSED ---
        SectionCard(
            sectionLetter = "F",
            title = "Details of Bills Enclosed",
            subtitle = "Itemized bill entries and running total auto-calculator",
            icon = Icons.Default.ReceiptLong
        ) {
            BillTableManager(
                bills = partAData.enclosedBills,
                onAddBill = { newBill ->
                    val updated = partAData.enclosedBills + newBill
                    onDataChange(partAData.copy(enclosedBills = updated))
                },
                onDeleteBill = { id ->
                    val updated = partAData.enclosedBills.filterNot { it.id == id }
                    onDataChange(partAData.copy(enclosedBills = updated))
                }
            )
        }

        // --- SECTION G: DETAILS OF PRIMARY INSURED'S BANK ACCOUNT ---
        SectionCard(
            sectionLetter = "G",
            title = "Details of Primary Insured's Bank Account",
            subtitle = "Bank details for direct claim reimbursement payout",
            icon = Icons.Default.AccountBalance
        ) {
            StandardTextField(
                value = partAData.pan,
                onValueChange = { onDataChange(partAData.copy(pan = it.uppercase())) },
                label = "a) PAN Number",
                placeholder = "ABCDE1234F"
            )
            StandardTextField(
                value = partAData.accountNumber,
                onValueChange = { onDataChange(partAData.copy(accountNumber = it)) },
                label = "b) Account Number",
                keyboardType = KeyboardType.Number
            )
            StandardTextField(
                value = partAData.bankNameAndBranch,
                onValueChange = { onDataChange(partAData.copy(bankNameAndBranch = it)) },
                label = "c) Bank Name and Branch"
            )
            StandardTextField(
                value = partAData.chequeDdPayableDetails,
                onValueChange = { onDataChange(partAData.copy(chequeDdPayableDetails = it)) },
                label = "d) Cheque / DD Payable details"
            )
            StandardTextField(
                value = partAData.ifscCode,
                onValueChange = { onDataChange(partAData.copy(ifscCode = it.uppercase())) },
                label = "e) IFSC Code",
                placeholder = "HDFC0000060"
            )
        }

        // --- SECTION H: DECLARATION BY THE INSURED ---
        SectionCard(
            sectionLetter = "H",
            title = "Declaration by the Insured",
            subtitle = "Confirmation of truthfulness and digital signature sign-off",
            icon = Icons.Default.Badge
        ) {
            Text(
                text = "I hereby declare that the information furnished in this claim form is true & correct to the best of my knowledge and belief. I consent & authorize TPA / insurance company to seek necessary medical records.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerField(
                    value = partAData.declarationDate,
                    onDateSelected = { onDataChange(partAData.copy(declarationDate = it)) },
                    label = "Date",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partAData.declarationPlace,
                    onValueChange = { onDataChange(partAData.copy(declarationPlace = it)) },
                    label = "Place",
                    modifier = Modifier.weight(1f)
                )
            }

            SignaturePad(
                title = "Signature of the Insured",
                isSigned = partAData.signatureCaptured,
                onSignatureChanged = { isSigned ->
                    onDataChange(partAData.copy(signatureCaptured = isSigned))
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Part A Button
        Button(
            onClick = onSubmitPartA,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Submit Part A Details & Continue",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
