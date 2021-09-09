package com.example.melichallenge.search

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melichallenge.R
import com.example.melichallenge.databinding.FragmentSearchBinding
import com.example.melichallenge.search.model.SearchResult
import org.koin.android.ext.android.inject

enum class SortFilters {
    LOWER_PRICE,
    HIGHER_PRICE
}

class SearchFragment : Fragment(), ResultsClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val resultsAdapter = ResultsAdapter(this)
    private val viewModel: SearchViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupSearchView()
        setupFilters()
        observeState()
        getSearchQuery()
        viewModel.restoreState(savedInstanceState)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val savedState = viewModel.saveState(outState)
        super.onSaveInstanceState(savedState)
    }

    private fun setupFilters() {
        binding.sortSpinner.apply {
            adapter = ArrayAdapter.createFromResource(context, R.array.sort_options, R.layout.support_simple_spinner_dropdown_item)
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    viewModel.setSelectedSort(position)
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun setupSearchView() {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(requireContext(), SearchActivity::class.java)
        binding.searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
    }

    private fun getSearchQuery() {
        if (Intent.ACTION_SEARCH == activity?.intent?.action) {
            activity?.intent?.getStringExtra(SearchManager.QUERY)?.also { query ->
                viewModel.search(query)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchResults.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeState() {
        observeResults()
        observeSelectedSort()
    }

    private fun observeSelectedSort() {
        viewModel.selectedSort.observe(viewLifecycleOwner) {
            binding.sortSpinner.setSelection(it.ordinal,false)
        }
    }

    private fun observeResults() {
        viewModel.searchResult.observe(viewLifecycleOwner) {
            resultsAdapter.setResults(it)
        }
    }

    override fun onItemClicked(product: SearchResult) {
        val action = SearchFragmentDirections.actionResultsFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }
}