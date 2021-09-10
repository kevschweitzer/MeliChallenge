package com.example.melichallenge.search.presentation

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

class SearchFragment : Fragment(), ResultsClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val resultsAdapter = ResultsAdapter(this)
    private val viewModel: SearchViewModel by inject()
    private var priceFilterSetUp: Boolean = false
    private var sortSelectionSetUp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSearchQuery()
        viewModel.restoreState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val savedState = viewModel.saveState(outState)
        super.onSaveInstanceState(savedState)
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
        observeState()
        setupSearchView()
        setupResultsList()
    }

    private fun setupResultsList() {
        binding.searchResults.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeState() {
        observePriceFilter()
        observeSort()
        observeResults()
    }

    private fun observeSort() {
        viewModel.sortOptions.observe(viewLifecycleOwner) {
            binding.sortSpinner.apply {
                setSelection(viewModel.selectedSortPosition, false)
                adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, it)
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                        if(sortSelectionSetUp) viewModel.setSelectedSort(position) else sortSelectionSetUp = true
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
            }
        }
    }

    private fun observePriceFilter() {
        viewModel.filterOptions.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                binding.priceFilterSpinner.apply {
                    setSelection(viewModel.selectedPriceFilterPosition, false)
                    adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, it)
                    onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                            if(priceFilterSetUp) viewModel.filterBy(position) else priceFilterSetUp = true
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                    }
                }
            } else {
                binding.priceFilterSpinner.visibility = View.GONE
            }
        }
    }

    private fun observeResults() {
        viewModel.results.observe(viewLifecycleOwner) {
            resultsAdapter.setResults(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        priceFilterSetUp = false
        sortSelectionSetUp = false
    }

    override fun onItemClicked(product: SearchResult) {
        val action = SearchFragmentDirections.actionResultsFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }
}