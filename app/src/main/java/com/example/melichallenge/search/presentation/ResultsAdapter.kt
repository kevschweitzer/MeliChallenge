package com.example.melichallenge.search.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.melichallenge.R
import com.example.melichallenge.databinding.ItemResultBinding
import com.example.melichallenge.search.model.SearchResult

interface ResultsClickListener {
    fun onItemClicked(product: SearchResult)
}

class ResultsAdapter(
    private val clickListener: ResultsClickListener
): RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    private var resultsList = mutableListOf<SearchResult>()
    private var _binding: ItemResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        _binding = DataBindingUtil.inflate(inflater, R.layout.item_result, parent, false)
        return ResultViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(resultsList[position])
    }

    override fun getItemCount() = resultsList.size

    fun setResults(results: List<SearchResult>) {
        resultsList.clear()
        resultsList.addAll(results)
        notifyDataSetChanged()
    }

    class ResultViewHolder(
        private val binding: ItemResultBinding,
        private val clickListener: ResultsClickListener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchResult) {
            binding.result = item
            binding.listener = clickListener
        }
    }
}