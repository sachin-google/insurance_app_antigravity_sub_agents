package com.example.claiminsurance.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.claiminsurance.model.PartAData
import com.example.claiminsurance.model.PartBData
import com.example.claiminsurance.model.EnclosedBill
import com.example.claiminsurance.model.DemoClaimDataGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State for Claim Processing with complete null-safety guarantees.
 */
data class ClaimUiState(
    val partA: PartAData = PartAData(),
    val partB: PartBData = PartBData(),
    val isLoading: Boolean = false,
    val validationErrors: List<String> = emptyList(),
    val isSubmitted: Boolean = false
)

/**
 * Refactored Claims Processing ViewModel enforcing Kotlin null safety,
 * safe type conversions, non-null default fallbacks, and state management.
 */
class ClaimViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ClaimUiState())
    val uiState: StateFlow<ClaimUiState> = _uiState.asStateFlow()

    /**
     * Safely updates Part A data with null-safety fallback to default PartAData if null.
     */
    fun updatePartA(data: PartAData?) {
        val safeData = data ?: PartAData()
        _uiState.update { currentState ->
            currentState.copy(
                partA = safeData,
                validationErrors = validatePartA(safeData)
            )
        }
    }

    /**
     * Safely updates Part B data with null-safety fallback to default PartBData if null.
     */
    fun updatePartB(data: PartBData?) {
        val safeData = data ?: PartBData()
        _uiState.update { currentState ->
            currentState.copy(
                partB = safeData,
                validationErrors = validatePartB(safeData)
            )
        }
    }

    /**
     * Safely adds an enclosed bill with null-safe input parsing.
     */
    fun addEnclosedBill(billNo: String?, date: String?, issuedBy: String?, towards: String?, amountStr: String?) {
        val safeBillNo = billNo.orEmpty().trim()
        val safeDate = date.orEmpty().trim()
        val safeIssuedBy = issuedBy.orEmpty().trim()
        val safeTowards = towards.orEmpty().trim()
        // Safely parse double without crashing on invalid or null input
        val safeAmount = amountStr?.toDoubleOrNull() ?: 0.0

        val currentBills = _uiState.value.partA.enclosedBills.orEmpty()
        val newBill = EnclosedBill(
            slNo = currentBills.size + 1,
            billNo = safeBillNo,
            date = safeDate,
            issuedBy = safeIssuedBy,
            towards = safeTowards,
            amount = safeAmount
        )

        val updatedBills = currentBills + newBill
        val updatedPartA = _uiState.value.partA.copy(enclosedBills = updatedBills)
        updatePartA(updatedPartA)
    }

    /**
     * Removes an enclosed bill safely by ID.
     */
    fun removeEnclosedBill(billId: String?) {
        if (billId.isNull_or_blank()) return
        val currentBills = _uiState.value.partA.enclosedBills.orEmpty()
        val updatedBills = currentBills.filterNot { it.id == billId }
        val updatedPartA = _uiState.value.partA.copy(enclosedBills = updatedBills)
        updatePartA(updatedPartA)
    }

    /**
     * Helper extension for safe string blank check.
     */
    private fun String?.isNull_or_blank(): Boolean {
        return this == null || this.isBlank()
    }

    /**
     * Autofills demo claim data safely.
     */
    fun loadDemoData() {
        val demoPartA = DemoClaimDataGenerator.generatePartA()
        val demoPartB = DemoClaimDataGenerator.generatePartB()
        _uiState.update { currentState ->
            currentState.copy(
                partA = demoPartA,
                partB = demoPartB,
                validationErrors = emptyList()
            )
        }
    }

    /**
     * Performs null-safe validation on Part A fields.
     */
    private fun validatePartA(partA: PartAData?): List<String> {
        if (partA == null) return listOf("Part A data is missing")
        val errors = mutableListOf<String>()

        if (partA.policyNo.isBlank()) errors.add("Policy Number is required.")
        if (partA.insuredNameSurname.isBlank() && partA.insuredNameFirst.isBlank()) {
            errors.add("Primary Insured Name is required.")
        }
        if (partA.phoneNo.isBlank()) errors.add("Phone Number is required.")
        
        return errors
    }

    /**
     * Performs null-safe validation on Part B fields.
     */
    private fun validatePartB(partB: PartBData?): List<String> {
        if (partB == null) return listOf("Part B data is missing")
        val errors = mutableListOf<String>()

        if (partB.hospitalName.isBlank()) errors.add("Hospital Name is required.")
        if (partB.hospitalId.isBlank()) errors.add("Hospital ID is required.")

        return errors
    }

    /**
     * Submits the claim safely.
     */
    fun submitClaim() {
        val partAErrors = validatePartA(_uiState.value.partA)
        val partBErrors = validatePartB(_uiState.value.partB)
        val allErrors = partAErrors + partBErrors

        if (allErrors.isNotEmpty()) {
            _uiState.update { it.copy(validationErrors = allErrors) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulate network processing
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isLoading = false, isSubmitted = true) }
        }
    }
}
