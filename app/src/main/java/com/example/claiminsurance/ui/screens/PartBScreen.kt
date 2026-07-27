package com.example.claiminsurance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.claiminsurance.model.ClaimChecklistOptions
import com.example.claiminsurance.model.PartBData
import com.example.claiminsurance.ui.components.*

@Composable
fun PartBScreen(
    partBData: PartBData,
    onDataChange: (PartBData) -> Unit,
    onSubmitPartB: () -> Unit = {}
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
            color = MaterialTheme.colorScheme.tertiaryContainer,
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
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "PART B: TO BE FILLED IN BY THE HOSPITAL",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    )
                    Text(
                        text = "Hospital registration, treating doctor info, ICD-10 diagnosis & non-network details.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // --- SECTION A: DETAILS OF HOSPITAL ---
        SectionCard(
            sectionLetter = "A",
            title = "Details of Hospital",
            subtitle = "Hospital registration, treating doctor & contact",
            icon = Icons.Default.LocalHospital
        ) {
            StandardTextField(
                value = partBData.hospitalName,
                onValueChange = { onDataChange(partBData.copy(hospitalName = it)) },
                label = "a) Name of Hospital"
            )
            StandardTextField(
                value = partBData.hospitalId,
                onValueChange = { onDataChange(partBData.copy(hospitalId = it)) },
                label = "b) Hospital ID"
            )

            RadioGroupField(
                title = "c) Type of Hospital",
                options = listOf("Network", "Non Network"),
                selectedOption = partBData.typeOfHospital,
                onOptionSelected = { onDataChange(partBData.copy(typeOfHospital = it)) }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partBData.treatingDoctorNameSurname,
                    onValueChange = { onDataChange(partBData.copy(treatingDoctorNameSurname = it)) },
                    label = "Doctor Surname",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partBData.treatingDoctorNameFirst,
                    onValueChange = { onDataChange(partBData.copy(treatingDoctorNameFirst = it)) },
                    label = "Doctor First Name",
                    modifier = Modifier.weight(1f)
                )
            }

            StandardTextField(
                value = partBData.doctorQualification,
                onValueChange = { onDataChange(partBData.copy(doctorQualification = it)) },
                label = "e) Doctor Qualification",
                placeholder = "MD, MBBS, DM"
            )
            StandardTextField(
                value = partBData.doctorRegistrationNo,
                onValueChange = { onDataChange(partBData.copy(doctorRegistrationNo = it)) },
                label = "f) Registration No. with State Code"
            )
            StandardTextField(
                value = partBData.doctorPhoneNo,
                onValueChange = { onDataChange(partBData.copy(doctorPhoneNo = it)) },
                label = "g) Doctor Phone No.",
                keyboardType = KeyboardType.Phone
            )
        }

        // --- SECTION B: DETAILS OF THE PATIENT ADMITTED ---
        SectionCard(
            sectionLetter = "B",
            title = "Details of Patient Admitted",
            subtitle = "IP registration number, admission type & status",
            icon = Icons.Default.Person
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partBData.patientNameSurname,
                    onValueChange = { onDataChange(partBData.copy(patientNameSurname = it)) },
                    label = "Surname",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partBData.patientNameFirst,
                    onValueChange = { onDataChange(partBData.copy(patientNameFirst = it)) },
                    label = "First Name",
                    modifier = Modifier.weight(1f)
                )
            }

            StandardTextField(
                value = partBData.ipRegistrationNumber,
                onValueChange = { onDataChange(partBData.copy(ipRegistrationNumber = it)) },
                label = "b) IP Registration Number"
            )

            RadioGroupField(
                title = "c) Gender",
                options = listOf("Male", "Female"),
                selectedOption = partBData.patientGender,
                onOptionSelected = { onDataChange(partBData.copy(patientGender = it)) }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerField(
                    value = partBData.dateOfAdmission,
                    onDateSelected = { onDataChange(partBData.copy(dateOfAdmission = it)) },
                    label = "Date of Admission",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partBData.timeOfAdmission,
                    onValueChange = { onDataChange(partBData.copy(timeOfAdmission = it)) },
                    label = "Time (HH:MM)",
                    placeholder = "09:30",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerField(
                    value = partBData.dateOfDischarge,
                    onDateSelected = { onDataChange(partBData.copy(dateOfDischarge = it)) },
                    label = "Date of Discharge",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partBData.timeOfDischarge,
                    onValueChange = { onDataChange(partBData.copy(timeOfDischarge = it)) },
                    label = "Time (HH:MM)",
                    placeholder = "14:00",
                    modifier = Modifier.weight(1f)
                )
            }

            RadioGroupField(
                title = "j) Type of Admission",
                options = listOf("Emergency", "Planned", "Day Care", "Maternity"),
                selectedOption = partBData.typeOfAdmission,
                onOptionSelected = { onDataChange(partBData.copy(typeOfAdmission = it)) }
            )

            RadioGroupField(
                title = "l) Status at time of discharge",
                options = listOf("Discharge to home", "Discharge to another hospital", "Deceased"),
                selectedOption = partBData.statusAtDischarge,
                onOptionSelected = { onDataChange(partBData.copy(statusAtDischarge = it)) }
            )

            StandardTextField(
                value = if (partBData.totalClaimedAmount > 0) partBData.totalClaimedAmount.toString() else "",
                onValueChange = { onDataChange(partBData.copy(totalClaimedAmount = it.toDoubleOrNull() ?: 0.0)) },
                label = "m) Total Claimed Amount (Rs.)",
                keyboardType = KeyboardType.Number
            )
        }

        // --- SECTION C: DETAILS OF AILMENT DIAGNOSED (PRIMARY) ---
        SectionCard(
            sectionLetter = "C",
            title = "Details of Ailment Diagnosed (Primary)",
            subtitle = "ICD-10 codes, procedure details & pre-authorization",
            icon = Icons.Default.MedicalInformation
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partBData.primaryDiagnosisIcd10,
                    onValueChange = { onDataChange(partBData.copy(primaryDiagnosisIcd10 = it)) },
                    label = "Primary ICD-10 Code",
                    placeholder = "e.g. K29.7",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partBData.primaryDiagnosisDesc,
                    onValueChange = { onDataChange(partBData.copy(primaryDiagnosisDesc = it)) },
                    label = "Description",
                    modifier = Modifier.weight(2f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partBData.additionalDiagnosisIcd10,
                    onValueChange = { onDataChange(partBData.copy(additionalDiagnosisIcd10 = it)) },
                    label = "Additional ICD-10",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partBData.additionalDiagnosisDesc,
                    onValueChange = { onDataChange(partBData.copy(additionalDiagnosisDesc = it)) },
                    label = "Description",
                    modifier = Modifier.weight(2f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardTextField(
                    value = partBData.procedure1IcdPcs,
                    onValueChange = { onDataChange(partBData.copy(procedure1IcdPcs = it)) },
                    label = "Procedure 1 ICD-PCS",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partBData.procedure1Desc,
                    onValueChange = { onDataChange(partBData.copy(procedure1Desc = it)) },
                    label = "Description",
                    modifier = Modifier.weight(2f)
                )
            }

            StandardTextField(
                value = partBData.procedureDetails,
                onValueChange = { onDataChange(partBData.copy(procedureDetails = it)) },
                label = "Details of Procedure",
                singleLine = false
            )

            SwitchField(
                title = "d) Pre-authorization obtained?",
                checked = partBData.preAuthObtained,
                onCheckedChange = { onDataChange(partBData.copy(preAuthObtained = it)) }
            )

            if (partBData.preAuthObtained) {
                StandardTextField(
                    value = partBData.preAuthNumber,
                    onValueChange = { onDataChange(partBData.copy(preAuthNumber = it)) },
                    label = "e) Pre-authorization Number"
                )
            } else {
                StandardTextField(
                    value = partBData.noPreAuthReason,
                    onValueChange = { onDataChange(partBData.copy(noPreAuthReason = it)) },
                    label = "Reason for not obtaining authorization"
                )
            }
        }

        // --- SECTION D: HOSPITAL CLAIM DOCUMENTS SUBMITTED ---
        SectionCard(
            sectionLetter = "D",
            title = "Hospital Claim Documents Submitted",
            subtitle = "Supporting clinical records and bill checklists",
            icon = Icons.Default.Description
        ) {
            DocumentChecklist(
                availableDocuments = ClaimChecklistOptions.partBDocuments,
                selectedDocuments = partBData.hospitalChecklistDocs,
                onToggleDocument = { doc ->
                    val current = partBData.hospitalChecklistDocs.toMutableSet()
                    if (current.contains(doc)) current.remove(doc) else current.add(doc)
                    onDataChange(partBData.copy(hospitalChecklistDocs = current))
                }
            )
        }

        // --- SECTION E: ADDITIONAL DETAILS FOR NON-NETWORK HOSPITAL ---
        if (partBData.typeOfHospital == "Non Network") {
            SectionCard(
                sectionLetter = "E",
                title = "Additional Details in Case of Non-Network Hospital",
                subtitle = "Facility infrastructure, PAN & registration info",
                icon = Icons.Default.Business
            ) {
                StandardTextField(
                    value = partBData.nonNetworkAddress,
                    onValueChange = { onDataChange(partBData.copy(nonNetworkAddress = it)) },
                    label = "a) Address of Hospital"
                )
                StandardTextField(
                    value = partBData.nonNetworkPhoneNo,
                    onValueChange = { onDataChange(partBData.copy(nonNetworkPhoneNo = it)) },
                    label = "b) Phone No.",
                    keyboardType = KeyboardType.Phone
                )
                StandardTextField(
                    value = partBData.nonNetworkRegNoWithState,
                    onValueChange = { onDataChange(partBData.copy(nonNetworkRegNoWithState = it)) },
                    label = "c) Registration No. with State Code"
                )
                StandardTextField(
                    value = partBData.hospitalPan,
                    onValueChange = { onDataChange(partBData.copy(hospitalPan = it.uppercase())) },
                    label = "d) Hospital PAN"
                )
                StandardTextField(
                    value = partBData.numberOfInpatientBeds,
                    onValueChange = { onDataChange(partBData.copy(numberOfInpatientBeds = it)) },
                    label = "e) Number of Inpatient Beds",
                    keyboardType = KeyboardType.Number
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SwitchField(
                        title = "Operation Theatre (OT)",
                        checked = partBData.otAvailable,
                        onCheckedChange = { onDataChange(partBData.copy(otAvailable = it)) },
                        modifier = Modifier.weight(1f)
                    )
                    SwitchField(
                        title = "Intensive Care (ICU)",
                        checked = partBData.icuAvailable,
                        onCheckedChange = { onDataChange(partBData.copy(icuAvailable = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- SECTION F: DECLARATION BY THE HOSPITAL ---
        SectionCard(
            sectionLetter = "F",
            title = "Declaration by the Hospital",
            subtitle = "Official sign and seal certification",
            icon = Icons.Default.Description
        ) {
            Text(
                text = "We hereby declare that the information furnished in this Claim Form is true & correct to the best of our knowledge and belief.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatePickerField(
                    value = partBData.declarationDate,
                    onDateSelected = { onDataChange(partBData.copy(declarationDate = it)) },
                    label = "Date",
                    modifier = Modifier.weight(1f)
                )
                StandardTextField(
                    value = partBData.declarationPlace,
                    onValueChange = { onDataChange(partBData.copy(declarationPlace = it)) },
                    label = "Place",
                    modifier = Modifier.weight(1f)
                )
            }

            SignaturePad(
                title = "Signature and Seal of Hospital Authority",
                isSigned = partBData.sealSignatureCaptured,
                onSignatureChanged = { isSigned ->
                    onDataChange(partBData.copy(sealSignatureCaptured = isSigned))
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Part B Button
        Button(
            onClick = onSubmitPartB,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Icon(imageVector = Icons.Default.LocalHospital, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Submit Hospital Form & Review Summary",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
