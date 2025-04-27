package com.example.mymoneynotes

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val transactions = mutableListOf<Transaction>()
    private val categories   = listOf("Food", "Transport", "Salary", "Entertainment", "Others")

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var balanceTextView: TextView
    private lateinit var barChart: BarChart

    private var balance = 0.0
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    private val chartValueFormatter = object : ValueFormatter() {
        override fun getBarLabel(entry: BarEntry?): String = entry?.y?.let {
            "${currencyFormat.format(it.toDouble())}"
        } ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        balanceTextView = findViewById(R.id.balance_text_view)
        barChart        = findViewById(R.id.bar_chart)
        recyclerView    = findViewById(R.id.transactions_recycler_view)

        updateBalanceDisplay()
        setupBarChart()

        setupTransactionList()
        setupTransactionInput()
        updateChartData()
    }

    private fun setupTransactionList() {
        transactionAdapter = TransactionAdapter(transactions)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter        = transactionAdapter
        }
    }

    private fun setupTransactionInput() {
        val toggleGroup     = findViewById<MaterialButtonToggleGroup>(R.id.transaction_type_toggle)
        val amountInput     = findViewById<TextInputEditText>(R.id.amount_input)
        val categorySpinner = findViewById<Spinner>(R.id.category_spinner)
        val addButton       = findViewById<MaterialButton>(R.id.add_transaction_button)

        // spinner for categories
        categorySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        addButton.setOnClickListener {
            val selId = toggleGroup.checkedButtonId
            if (selId == -1) {
                Toast.makeText(this, getString(R.string.select_transaction_type), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amtText = amountInput.text.toString()
            val amount  = amtText.toDoubleOrNull()
            if (amount == null) {
                amountInput.error = getString(R.string.invalid_amount)
                return@setOnClickListener
            }

            val category = categorySpinner.selectedItem as String
            val type = if (selId == R.id.income_button)
                TransactionType.INCOME else TransactionType.EXPENSE

            // update balance
            balance += if (type == TransactionType.INCOME) amount else -amount
            updateBalanceDisplay()

            // record transaction
            transactions.add(0, Transaction(type = type, amount = amount, category = category))
            transactionAdapter.notifyItemInserted(0)
            recyclerView.scrollToPosition(0)

            // update chart
            updateChartData()

            // reset inputs
            amountInput.text?.clear()
            categorySpinner.setSelection(0)
            toggleGroup.clearChecked()

            Toast.makeText(this, getString(R.string.transaction_added), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateBalanceDisplay() {
        balanceTextView.text = getString(R.string.balance_format, currencyFormat.format(balance))
    }

    private fun setupBarChart() {
        barChart.apply {
            description.isEnabled = false
            xAxis.apply {
                granularity        = 1f
                position           = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                valueFormatter     = IndexAxisValueFormatter(listOf("Income", "Expense"))
            }
            axisRight.isEnabled  = false
            axisLeft.axisMinimum = 0f
            legend.isEnabled     = false
        }
    }

    private fun updateChartData() {
        val incomeTotal  = transactions.filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
            .toFloat()

        val expenseTotal = transactions.filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
            .toFloat()

        val entries = listOf(
            BarEntry(0f, incomeTotal),
            BarEntry(1f, expenseTotal)
        )

        val set = BarDataSet(entries, "Totals").apply {
            setColors(
                ContextCompat.getColor(this@MainActivity, R.color.income_color),
                ContextCompat.getColor(this@MainActivity, R.color.expense_color)
            )
            valueTextSize = 16f
            valueFormatter = chartValueFormatter
        }

        barChart.data = BarData(set).apply { barWidth = 0.5f }
        barChart.invalidate()
    }
}
