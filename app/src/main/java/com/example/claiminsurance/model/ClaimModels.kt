package com.example.claiminsurance.model

import androidx.compose.ui.geometry.Offset

// --- Enclosed Bill Item ---
data class EnclosedBill(
    val id: String = java.util.UUID.randomUUID().toString(),
    val slNo: Int = 1,
    val billNo: String = "",
    val date: String = "",
    val issuedBy: String = "",
    val towards: String = "",
    val amount: Double = 0.0
)

// --- Part A: Details of Insured ---
data class PartAData(
    // Section A: Primary Insured
    val policyNo: String = "",
    val slNoCertificateNo: String = "",
    val companyTpaIdNo: String = "",
    val insuredNameSurname: String = "",
    val insuredNameFirst: String = "",
    val insuredNameMiddle: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val pinCode: String = "",
    val phoneNo: String = "",
    val emailId: String = "",

    // Section B: Insurance History
    val coveredOtherMediclaim: Boolean = false,
    val dateCommencementFirstInsurance: String = "",
    val otherInsuranceCompany: String = "",
    val otherPolicyNo: String = "",
    val sumInsured: String = "",
    val hospitalizedLastFourYears: Boolean = false,
    val hospitalizationDate: String = "",
    val diagnosisHistory: String = "",
    val previouslyCoveredMediclaim: Boolean = false,
    val previousCompany: String = "",

    // Section C: Insured Person Hospitalized
    val patientSurname: String = "",
    val patientFirst: String = "",
    val patientMiddle: String = "",
    val gender: String = "Male", // Male / Female
    val ageYears: String = "",
    val ageMonths: String = "",
    val dateOfBirth: String = "",
    val relationshipToPrimary: String = "Self", // Self, Spouse, Child, Father, Mother, Other
    val relationshipOther: String = "",
    val occupation: String = "Service", // Service, Self Employed, Homemaker, Student, Retired, Other
    val occupationOther: String = "",
    val patientAddressSameAsPrimary: Boolean = true,
    val patientAddress: String = "",
    val patientCity: String = "",
    val patientState: String = "",
    val patientPinCode: String = "",
    val patientPhoneNo: String = "",
    val patientEmailId: String = "",

    // Section D: Hospitalization Details
    val hospitalName: String = "",
    val roomCategory: String = "Single occupancy", // Day care, Single occupancy, Twin sharing, 3 or more beds
    val hospitalizationDueTo: String = "Illness", // Injury, Illness, Maternity
    val dateOfInjuryDisease: String = "",
    val dateOfAdmission: String = "",
    val timeOfAdmission: String = "",
    val dateOfDischarge: String = "",
    val timeOfDischarge: String = "",
    val injuryCause: String = "", // Self inflicted, Road Traffic Accident, Substance Abuse / Alcohol
    val isMedicoLegal: Boolean = false,
    val reportedToPolice: Boolean = false,
    val mlcReportFirAttached: Boolean = false,
    val systemOfMedicine: String = "Allopathy",

    // Section E: Details of Claim
    val preHospitalizationExpenses: Double = 0.0,
    val hospitalizationExpenses: Double = 0.0,
    val postHospitalizationExpenses: Double = 0.0,
    val healthCheckupCost: Double = 0.0,
    val ambulanceCharges: Double = 0.0,
    val otherExpensesCode: String = "",
    val otherExpensesAmount: Double = 0.0,
    val preHospitalizationPeriodDays: String = "",
    val postHospitalizationPeriodDays: String = "",
    val claimDomiciliaryHospitalization: Boolean = false,
    val hospitalDailyCashClaim: Double = 0.0,
    val surgicalCashClaim: Double = 0.0,
    val criticalIllnessClaim: Double = 0.0,
    val convalescenceClaim: Double = 0.0,
    val prePostLumpSumBenefit: Double = 0.0,
    val lumpSumOthers: Double = 0.0,
    val checklistSubmittedDocs: Set<String> = emptySet(),

    // Section F: Enclosed Bills
    val enclosedBills: List<EnclosedBill> = emptyList(),

    // Section G: Bank Account Details
    val pan: String = "",
    val accountNumber: String = "",
    val bankNameAndBranch: String = "",
    val chequeDdPayableDetails: String = "",
    val ifscCode: String = "",

    // Section H: Declaration
    val declarationDate: String = "",
    val declarationPlace: String = "",
    val signatureCaptured: Boolean = false
) {
    val totalTreatmentExpensesClaimed: Double
        get() = preHospitalizationExpenses + hospitalizationExpenses + postHospitalizationExpenses +
                healthCheckupCost + ambulanceCharges + otherExpensesAmount

    val totalLumpSumClaimed: Double
        get() = hospitalDailyCashClaim + surgicalCashClaim + criticalIllnessClaim +
                convalescenceClaim + prePostLumpSumBenefit + lumpSumOthers

    val totalBillsAmount: Double
        get() = enclosedBills.sumOf { it.amount }
}

// --- Part B: Details of Hospital ---
data class PartBData(
    // Section A: Hospital Details
    val hospitalName: String = "",
    val hospitalId: String = "",
    val typeOfHospital: String = "Network", // Network / Non Network
    val treatingDoctorNameSurname: String = "",
    val treatingDoctorNameFirst: String = "",
    val treatingDoctorNameMiddle: String = "",
    val doctorQualification: String = "",
    val doctorRegistrationNo: String = "",
    val doctorPhoneNo: String = "",

    // Section B: Patient Details
    val patientNameSurname: String = "",
    val patientNameFirst: String = "",
    val patientNameMiddle: String = "",
    val ipRegistrationNumber: String = "",
    val patientGender: String = "Male",
    val patientAgeYears: String = "",
    val patientAgeMonths: String = "",
    val patientDateOfBirth: String = "",
    val dateOfAdmission: String = "",
    val timeOfAdmission: String = "",
    val dateOfDischarge: String = "",
    val timeOfDischarge: String = "",
    val typeOfAdmission: String = "Planned", // Emergency, Planned, Day Care, Maternity
    val maternityDateOfDelivery: String = "",
    val gravidaStatus: String = "",
    val statusAtDischarge: String = "Discharge to home", // Discharge to home, Discharge to another hospital, Deceased
    val totalClaimedAmount: Double = 0.0,

    // Section C: Ailment Diagnosed (Primary)
    val primaryDiagnosisIcd10: String = "",
    val primaryDiagnosisDesc: String = "",
    val additionalDiagnosisIcd10: String = "",
    val additionalDiagnosisDesc: String = "",
    val comorbiditiesIcd10: String = "",
    val comorbiditiesDesc: String = "",
    val procedure1IcdPcs: String = "",
    val procedure1Desc: String = "",
    val procedure2IcdPcs: String = "",
    val procedure2Desc: String = "",
    val procedure3IcdPcs: String = "",
    val procedure3Desc: String = "",
    val procedureDetails: String = "",
    val preAuthObtained: Boolean = true,
    val preAuthNumber: String = "",
    val noPreAuthReason: String = "",
    val injuryHospitalization: Boolean = false,
    val injuryCause: String = "",
    val alcoholSubstanceTestConducted: Boolean = false,
    val medicoLegal: Boolean = false,
    val reportedToPolice: Boolean = false,
    val firNo: String = "",
    val notReportedToPoliceReason: String = "",

    // Section D: Hospital Checklist
    val hospitalChecklistDocs: Set<String> = emptySet(),

    // Section E: Non-Network Hospital Details
    val nonNetworkAddress: String = "",
    val nonNetworkPhoneNo: String = "",
    val nonNetworkRegNoWithState: String = "",
    val hospitalPan: String = "",
    val numberOfInpatientBeds: String = "",
    val otAvailable: Boolean = true,
    val icuAvailable: Boolean = true,
    val otherFacilities: String = "",

    // Section F: Declaration by Hospital
    val declarationDate: String = "",
    val declarationPlace: String = "",
    val sealSignatureCaptured: Boolean = false
)

enum class FormTab {
    PART_A, // Insured / Policyholder
    PART_B, // Hospital
    SUMMARY, // Claim Receipt & Export
    PAYOUT_CONFIRMATION // Reimbursement Payout Status
}

// Checklist Options
object ClaimChecklistOptions {
    val partADocuments = listOf(
        "Claim Form Duly signed",
        "Copy of the claim intimation, if any",
        "Hospital Main Bill",
        "Hospital Break-up Bill",
        "Hospital Bill Payment Receipt",
        "Hospital Discharge Summary",
        "Pharmacy Bill",
        "Operation Theatre Notes",
        "ECG",
        "Doctor's request for investigation",
        "Investigation Reports (Including CT / MRI / USG / HPE)",
        "Doctor's Prescriptions",
        "Others"
    )

    val partBDocuments = listOf(
        "Claim Form duly signed",
        "Original Pre-authorization request",
        "Copy of the Pre-authorization approval letter",
        "Copy of photo ID card of patient verified by hospital",
        "Hospital Discharge summary",
        "Operation Theatre notes",
        "Hospital main bill",
        "Hospital break-up bill",
        "Investigation reports",
        "CT/MR/USG/HPE investigation reports",
        "Doctor's reference slip for investigation",
        "ECG",
        "Pharmacy bills",
        "MLC report & Police FIR",
        "Original death summary from hospital where applicable",
        "Any other, please specify"
    )
}

// Quick Sample Generator
object DemoClaimDataGenerator {
    fun generatePartA(): PartAData {
        return PartAData(
            policyNo = "512300/34/24/0009841",
            slNoCertificateNo = "CERT-99812",
            companyTpaIdNo = "TPA-MD-INDIA-88",
            insuredNameSurname = "SHARMA",
            insuredNameFirst = "RAHUL",
            insuredNameMiddle = "KUMAR",
            address = "42, Green Park Avenue, Bandra West",
            city = "Mumbai",
            state = "Maharashtra",
            pinCode = "400050",
            phoneNo = "+91 9876543210",
            emailId = "rahul.sharma@example.com",
            coveredOtherMediclaim = false,
            dateCommencementFirstInsurance = "15/04/2018",
            sumInsured = "500000",
            hospitalizedLastFourYears = false,

            patientSurname = "SHARMA",
            patientFirst = "RAHUL",
            patientMiddle = "KUMAR",
            gender = "Male",
            ageYears = "38",
            ageMonths = "4",
            dateOfBirth = "12/03/1988",
            relationshipToPrimary = "Self",
            occupation = "Service",
            patientAddressSameAsPrimary = true,

            hospitalName = "Apollo Multi-Specialty Hospital",
            roomCategory = "Single occupancy",
            hospitalizationDueTo = "Illness",
            dateOfInjuryDisease = "10/07/2026",
            dateOfAdmission = "12/07/2026",
            timeOfAdmission = "09:30",
            dateOfDischarge = "16/07/2026",
            timeOfDischarge = "14:00",
            systemOfMedicine = "Allopathy",

            preHospitalizationExpenses = 4500.0,
            hospitalizationExpenses = 78000.0,
            postHospitalizationExpenses = 3200.0,
            healthCheckupCost = 1500.0,
            ambulanceCharges = 2000.0,
            otherExpensesAmount = 0.0,
            preHospitalizationPeriodDays = "15",
            postHospitalizationPeriodDays = "30",

            hospitalDailyCashClaim = 2000.0,
            surgicalCashClaim = 0.0,
            criticalIllnessClaim = 0.0,
            convalescenceClaim = 1000.0,

            checklistSubmittedDocs = setOf(
                "Claim Form Duly signed",
                "Hospital Main Bill",
                "Hospital Break-up Bill",
                "Hospital Bill Payment Receipt",
                "Hospital Discharge Summary",
                "Pharmacy Bill",
                "Doctor's Prescriptions"
            ),

            enclosedBills = listOf(
                EnclosedBill(slNo = 1, billNo = "B-9021", date = "16/07/2026", issuedBy = "Apollo Hospital", towards = "Hospital Main Bill", amount = 78000.0),
                EnclosedBill(slNo = 2, billNo = "PH-4421", date = "10/07/2026", issuedBy = "Apollo Pharmacy", towards = "Pre-hospitalization Bills", amount = 4500.0),
                EnclosedBill(slNo = 3, billNo = "PH-4890", date = "18/07/2026", issuedBy = "Apollo Pharmacy", towards = "Post-hospitalization Bills", amount = 3200.0),
                EnclosedBill(slNo = 4, billNo = "AMB-102", date = "12/07/2026", issuedBy = "Quick Ambulance", towards = "Ambulance Charges", amount = 2000.0)
            ),

            pan = "ABCDE1234F",
            accountNumber = "91802004581299",
            bankNameAndBranch = "HDFC Bank, Fort Branch",
            chequeDdPayableDetails = "Rahul Kumar Sharma",
            ifscCode = "HDFC0000060",

            declarationDate = "20/07/2026",
            declarationPlace = "Mumbai",
            signatureCaptured = true
        )
    }

    fun generatePartB(): PartBData {
        return PartBData(
            hospitalName = "Apollo Multi-Specialty Hospital",
            hospitalId = "HOSP-MUM-8841",
            typeOfHospital = "Network",
            treatingDoctorNameSurname = "DESHMUKH",
            treatingDoctorNameFirst = "ANAND",
            treatingDoctorNameMiddle = "V",
            doctorQualification = "MD, DM (Cardiology)",
            doctorRegistrationNo = "MMC-2005/04/1234",
            doctorPhoneNo = "+91 9820198201",

            patientNameSurname = "SHARMA",
            patientNameFirst = "RAHUL",
            patientNameMiddle = "KUMAR",
            ipRegistrationNumber = "IPN-2026-88741",
            patientGender = "Male",
            patientAgeYears = "38",
            patientAgeMonths = "4",
            patientDateOfBirth = "12/03/1988",
            dateOfAdmission = "12/07/2026",
            timeOfAdmission = "09:30",
            dateOfDischarge = "16/07/2026",
            timeOfDischarge = "14:00",
            typeOfAdmission = "Emergency",
            statusAtDischarge = "Discharge to home",
            totalClaimedAmount = 89200.0,

            primaryDiagnosisIcd10 = "K29.7",
            primaryDiagnosisDesc = "Gastritis, unspecified with acute pain",
            additionalDiagnosisIcd10 = "E11.9",
            additionalDiagnosisDesc = "Type 2 diabetes mellitus without complications",
            procedure1IcdPcs = "45.13",
            procedure1Desc = "Other endoscopy of small intestine",
            procedureDetails = "Diagnostic endoscopy performed under mild sedation.",
            preAuthObtained = true,
            preAuthNumber = "PA-APO-991823",

            hospitalChecklistDocs = setOf(
                "Claim Form duly signed",
                "Original Pre-authorization request",
                "Copy of the Pre-authorization approval letter",
                "Copy of photo ID card of patient verified by hospital",
                "Hospital Discharge summary",
                "Hospital main bill",
                "Hospital break-up bill"
            ),

            declarationDate = "16/07/2026",
            declarationPlace = "Mumbai",
            sealSignatureCaptured = true
        )
    }
}
