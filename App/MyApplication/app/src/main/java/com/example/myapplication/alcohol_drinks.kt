package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.placeholder.PlaceholderContent

/**
 * A simple [Fragment] subclass.
 * Use the [alcohol_drinks.newInstance] factory method to
 * create an instance of this fragment.
 */

class alcohol_drinks : Fragment() {
    private var columnCount = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = DrinkViewAdapter(PlaceholderContent.alcoholDrinks)
            }
        }
        return view
    }



}