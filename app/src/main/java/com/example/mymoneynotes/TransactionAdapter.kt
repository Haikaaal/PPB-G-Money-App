package com.example.mymoneynotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance()

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryText: TextView = itemView.findViewById(R.id.transaction_category)
        val dateText: TextView = itemView.findViewById(R.id.transaction_date)
        val amountText: TextView = itemView.findViewById(R.id.transaction_amount)
        val typeText: TextView = itemView.findViewById(R.id.transaction_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.categoryText.text = transaction.category
        holder.dateText.text = dateFormat.format(transaction.date)

        // Format the amount with + or - prefix based on transaction type
        val formattedAmount = if (transaction.type == TransactionType.INCOME) {
            "+${currencyFormat.format(transaction.amount)}"
        } else {
            "-${currencyFormat.format(transaction.amount)}"
        }

        holder.amountText.text = formattedAmount
        holder.typeText.text = transaction.type.name

        // Set colors based on transaction type
        val colorRes = if (transaction.type == TransactionType.INCOME) {
            R.color.income_color
        } else {
            R.color.expense_color
        }

        val color = ContextCompat.getColor(holder.itemView.context, colorRes)
        holder.amountText.setTextColor(color)
        holder.typeText.setTextColor(color)
    }

    override fun getItemCount() = transactions.size
}