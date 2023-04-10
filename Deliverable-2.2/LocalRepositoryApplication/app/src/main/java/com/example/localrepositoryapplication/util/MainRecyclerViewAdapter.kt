package com.example.localrepositoryapplication.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.localrepositoryapplication.R
import com.example.localrepositoryapplication.db.Student


class MainRecyclerViewAdapter(dataset: List<Student>) : RecyclerView.Adapter<MainRecyclerViewHolder>() {
    // RecyclerView data
    var dataset: List<Student> = dataset

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return MainRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        holder.onBind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}

class MainRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val view = view

    fun onBind(data: Student) {
        val studentPhoto: ImageView = view.findViewById(R.id.photoImageView)
        val fullNameTextView: TextView = view.findViewById(R.id.studentNameTextView)
        val puidTextView: TextView = view.findViewById(R.id.studentPuidTextView)

        studentPhoto.setImageResource(R.drawable.ic_baseline_person_24)
        fullNameTextView.text = "${data.firstName} ${data.lastName}"
        puidTextView.text = "PUID: ${data.puid}"

    }

}