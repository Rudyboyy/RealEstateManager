package com.openclassrooms.realestatemanager.ui.loancalculator

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.LoanCalculatorFragmentBinding
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import java.text.DecimalFormat
import kotlin.math.pow

class LoanCalculatorFragment : Fragment(R.layout.loan_calculator_fragment) {

    private val binding by viewBinding { LoanCalculatorFragmentBinding.bind(it) }
    private val viewModel: RealEstateViewModel by activityViewModels {
        Injection.provideViewModelFactory(requireContext())
    }
    private val actionFragment: Int = R.id.action_global_to_PropertyListFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.seekBarLoanAmount.max = (5000000 - 100000) / 5000
        binding.seekBarLoanTerm.max = 25 - 2
        binding.seekBarInterestRate.max = 500

        setupSeekBarListeners()
        updateCurrency()
        setBackButton()
    }

    private fun setBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(actionFragment)
        }
    }

    private fun setupSeekBarListeners() {
        val seekBarListeners = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateMonthlyPayment()
                updateCurrency()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        binding.seekBarLoanAmount.setOnSeekBarChangeListener(seekBarListeners)
        binding.seekBarLoanTerm.setOnSeekBarChangeListener(seekBarListeners)
        binding.seekBarInterestRate.setOnSeekBarChangeListener(seekBarListeners)
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

    private fun updateCurrency() {
        viewModel.currencyLiveData.observe(viewLifecycleOwner) { isDollar ->
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
            val monthlyPayment = calculateLoanMonthlyPayment(loanAmount, interestRate, loanTerm)
            val loanDurationInMonths = loanTerm * 12
            val totalInterest = (monthlyPayment * loanDurationInMonths) - loanAmount

            val loanAmountFormatted = if (isDollar) {
                loanAmount
            } else {
                Utils.convertDollarToEuro(loanAmount.toInt()).toDouble()
            }

            val priceFormatResId = if (isDollar) {
                R.string.price_format_dollar
            } else {
                R.string.price_format_eur
            }

            var monthlyPaymentFormatted = if (isDollar) {
                monthlyPayment
            } else {
                Utils.convertDollarToEuro(monthlyPayment.toInt()).toDouble()
            }

            val priceFormatResId2 = if (isDollar) {
                R.string.payment_per_month
            } else {
                R.string.payment_per_month_euro
            }

            var totalInterestFormatted = if (isDollar) {
                totalInterest
            } else {
                Utils.convertDollarToEuro(totalInterest.toInt()).toDouble()
            }

            val priceFormatResId3 = if (isDollar) {
                R.string.total_interest
            } else {
                R.string.total_interest_euro
            }

            if (monthlyPayment.isNaN()) {
                totalInterestFormatted = 0.0
                monthlyPaymentFormatted = loanAmountFormatted / (loanTerm * 12)
            }

            binding.loanAmountValue.text =
                getString(priceFormatResId, formatAmount(loanAmountFormatted))
            binding.monthlyPaymentValue.text =
                getString(priceFormatResId2, formatAmount(monthlyPaymentFormatted))
            binding.totalInterestValue.text =
                getString(priceFormatResId3, formatAmount(totalInterestFormatted))
        }
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
