package com.example.mymoneynotes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private val transactions = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val categoryInput = findViewById<TextInputEditText>(R.id.category_input)
        val addButton = findViewById<MaterialButton>(R.id.add_transaction_button)

        addButton.setOnClickListener {
            val selectedId = toggleGroup.checkedButtonId

            // Validate inputs
            if (selectedId == -1) {
                Toast.makeText(this, getString(R.string.select_transaction_type), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amountText = amountInput.text.toString()
            if (amountText.isEmpty()) {
                amountInput.error = getString(R.string.amount_required)
                return@setOnClickListener
            }

            val category = categoryInput.text.toString()
            if (category.isEmpty()) {
                categoryInput.error = getString(R.string.category_required)
                return@setOnClickListener
            }

            try {
                val amount = amountText.toDouble()

                // Determine transaction type
                val transactionType = if (selectedId == R.id.income_button) {
                    TransactionType.INCOME
                } else {
                    TransactionType.EXPENSE
                }

                // Create and add transaction
                val transaction = Transaction(
                    type = transactionType,
                    amount = amount,
                    category = category
                )

                transactions.add(0, transaction) // Add to beginning of list
                transactionAdapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0) // Scroll to top

                // Reset input fields
                amountInput.text?.clear()
                categoryInput.text?.clear()

                Toast.makeText(
                    this,
                    getString(R.string.transaction_added),
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: NumberFormatException) {
                amountInput.error = getString(R.string.invalid_amount)
            }
        }
    }
}