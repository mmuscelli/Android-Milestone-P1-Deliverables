package com.example.advancedui_deliverable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainRecyclerViewAdapter : RecyclerView.Adapter<MainViewHolder>() {
    // Dataset
    var dataset = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_view_element, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}

class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun onBind(data: String) {
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        contentTextView.text = data
    }
}