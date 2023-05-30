package com.openclassrooms.realestatemanager.ui.loancalculator

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.LoanCalculatorFragmentBinding
import com.openclassrooms.realestatemanager.utils.viewBinding
import java.text.DecimalFormat
import kotlin.math.pow

class LoanCalculatorFragment : Fragment(R.layout.loan_calculator_fragment) {

    private val binding by viewBinding { LoanCalculatorFragmentBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.seekBarLoanAmount.max = (5000000 - 100000) / 5000
        binding.seekBarLoanTerm.max = 25 - 2
        binding.seekBarInterestRate.max = 500

        binding.seekBarLoanAmount.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateMonthlyPayment()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.seekBarLoanTerm.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateMonthlyPayment()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.seekBarInterestRate.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateMonthlyPayment()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun formatAmount(amount: Double): String {
        val decimalFormat = DecimalFormat("#,##0")
        return decimalFormat.format(amount)
    }

    private fun updateMonthlyPayment() {
        val loanAmount = (binding.seekBarLoanAmount.progress * 5000).toDouble() + 100000
        val loanTerm = binding.seekBarLoanTerm.progress + 2
        val currentInterestRate = binding.seekBarInterestRate.progress.toDouble() / 100
        val interestRate = if (binding.seekBarLoanTerm.isPressed) {
            when (loanTerm) {
                in 2..9 -> 3.12
                in 10..12 -> 3.21
                in 13..19 -> 3.27
                in 20..24 -> 3.41
                25 -> 3.58
                else -> currentInterestRate
            }
        } else {
            currentInterestRate
        }
        val loanAmountFormatted = formatAmount(loanAmount)
        val monthlyPayment = calculateLoanMonthlyPayment(loanAmount, interestRate, loanTerm)
        val monthlyPaymentFormatted = if (monthlyPayment.isNaN()) {
            formatAmount(loanAmount / (loanTerm * 12))
        } else {
            formatAmount(monthlyPayment)
        }
        val loanDurationInMonths = loanTerm * 12
        val totalInterest = (monthlyPayment * loanDurationInMonths) - loanAmount
        val totalInterestFormatted = if (monthlyPayment.isNaN()) {
            formatAmount(0.0)
        } else {
            formatAmount(totalInterest)
        }


        binding.monthlyPaymentValue.text =
            getString(R.string.payment_per_month, monthlyPaymentFormatted)
        binding.loanAmountValue.text = getString(R.string.price_format_dollar, loanAmountFormatted)
        binding.loanTermValue.text = getString(R.string.loan_term, loanTerm)
        binding.interestRateValue.text = getString(R.string.interest_rate_value, interestRate)
        binding.seekBarInterestRate.progress = (interestRate * 100).toInt()
        binding.totalInterestValue.text = getString(R.string.total_interest, totalInterestFormatted)
    }

    private fun calculateLoanMonthlyPayment(
        loanAmount: Double,
        interestRate: Double,
        loanDurationInYears: Int
    ): Double {
        val loanDurationInMonths = loanDurationInYears * 12
        val monthlyInterestRate = interestRate / 100 / 12.0
        val numerator = loanAmount * monthlyInterestRate * (1 + monthlyInterestRate).pow(
            loanDurationInMonths.toDouble()
        )
        val denominator = (1 + monthlyInterestRate).pow(loanDurationInMonths.toDouble()) - 1
        return numerator / denominator
    }
}
