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

class TransactionAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance()

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryText: TextView   = itemView.findViewById(R.id.transaction_category)
        val dateText: TextView       = itemView.findViewById(R.id.transaction_date)
        val amountText: TextView     = itemView.findViewById(R.id.transaction_amount)
        val typeText: TextView       = itemView.findViewById(R.id.transaction_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val tx = transactions[position]

        // ➜ Display category as plain text
        holder.categoryText.text = tx.category

        // ➜ Date
        holder.dateText.text = dateFormat.format(tx.date)

        // ➜ Amount with +/-
        val formattedAmount = if (tx.type == TransactionType.INCOME) {
            "+${currencyFormat.format(tx.amount)}"
        } else {
            "-${currencyFormat.format(tx.amount)}"
        }
        holder.amountText.text = formattedAmount
        holder.typeText.text = tx.type.name

        // ➜ Coloring
        val colorRes = if (tx.type == TransactionType.INCOME)
            R.color.income_color else R.color.expense_color
        val color = ContextCompat.getColor(holder.itemView.context, colorRes)
        holder.amountText.setTextColor(color)
        holder.typeText.setTextColor(color)
    }

    override fun getItemCount(): Int = transactions.size
}
