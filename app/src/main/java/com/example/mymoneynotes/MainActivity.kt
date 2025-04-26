package com.example.mymoneynotes

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val transactions = mutableListOf<Transaction>()
    private val categories = listOf("Food", "Transport", "Salary", "Entertainment", "Others")

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var recyclerView: RecyclerView

    // ① Balance variable and formatter
    private var balance: Double = 0.0
    private lateinit var balanceTextView: TextView
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ② Find the balance TextView
        balanceTextView = findViewById(R.id.balance_text_view)
        updateBalanceDisplay()

        setupTransactionList()
        setupTransactionInput()
    }

    private fun setupTransactionList() {
        recyclerView = findViewById(R.id.transactions_recycler_view)
        transactionAdapter = TransactionAdapter(transactions)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = transactionAdapter
        }
    }

    private fun setupTransactionInput() {
        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.transaction_type_toggle)
        val amountInput = findViewById<TextInputEditText>(R.id.amount_input)
        val categorySpinner = findViewById<Spinner>(R.id.category_spinner)
        val addButton = findViewById<MaterialButton>(R.id.add_transaction_button)

        // Spinner setup
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        categorySpinner.adapter = spinnerAdapter

        addButton.setOnClickListener {
            val selectedId = toggleGroup.checkedButtonId
            if (selectedId == -1) {
                Toast.makeText(this, getString(R.string.select_transaction_type), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amountText = amountInput.text.toString()
            if (amountText.isEmpty()) {
                amountInput.error = getString(R.string.amount_required)
                return@setOnClickListener
            }

            try {
                val amount = amountText.toDouble()
                val category = categorySpinner.selectedItem as String
                val transactionType = if (selectedId == R.id.income_button)
                    TransactionType.INCOME else TransactionType.EXPENSE

                // ③ Update balance
                balance += if (transactionType == TransactionType.INCOME) amount else -amount
                updateBalanceDisplay()

                // ④ Add to list and notify adapter
                val transaction = Transaction(
                    type     = transactionType,
                    amount   = amount,
                    category = category
                )
                transactions.add(0, transaction)
                transactionAdapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)

                // Reset inputs
                amountInput.text?.clear()
                categorySpinner.setSelection(0)
                toggleGroup.clearChecked()

                Toast.makeText(this, getString(R.string.transaction_added), Toast.LENGTH_SHORT).show()
            } catch (e: NumberFormatException) {
                amountInput.error = getString(R.string.invalid_amount)
            }
        }
    }

    // Helper to refresh the balance TextView
    private fun updateBalanceDisplay() {
        balanceTextView.text = getString(
            R.string.balance_format,
            currencyFormat.format(balance)
        )
    }
}
