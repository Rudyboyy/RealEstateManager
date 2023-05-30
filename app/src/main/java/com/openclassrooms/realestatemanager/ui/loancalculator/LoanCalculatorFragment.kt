package com.openclassrooms.realestatemanager.ui.loancalculator

import kotlin.math.pow

class LoanCalculatorFragment {

    fun calculateLoanMonthlyPayment(loanAmount: Double, interestRate: Double, loanDurationInMonths: Int): Double {
        val monthlyInterestRate = interestRate / 12.0
        val numerator = loanAmount * monthlyInterestRate * (1 + monthlyInterestRate).pow(
            loanDurationInMonths.toDouble()
        )
        val denominator = (1 + monthlyInterestRate).pow(loanDurationInMonths.toDouble()) - 1
        return numerator / denominator
    }
}