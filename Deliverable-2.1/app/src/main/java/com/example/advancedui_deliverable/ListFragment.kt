package com.example.advancedui_deliverable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Test dataset
        val dataset = mutableListOf<String>()
        for (i in 1 .. 30) {
            dataset.add("Data -> Number: $i")
        }

        // RecyclerView
        val mRecyclerView: RecyclerView = view.findViewById(R.id.mRecyclerView)
        val mAdapter = MainRecyclerViewAdapter()
        mAdapter.dataset = dataset
        mRecyclerView.adapter = mAdapter
    }

}