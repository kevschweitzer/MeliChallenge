package com.example.melichallenge.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melichallenge.R
import com.example.melichallenge.databinding.FragmentResultsBinding
import com.example.melichallenge.di.SHARED_SCOPE_SEARCH_ID
import com.example.melichallenge.search.presentation.SearchViewModel
import org.koin.core.context.GlobalContext
import org.koin.core.scope.Scope

class ResultsFragment : Fragment(), ResultsClickListener {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private val resultsAdapter = ResultsAdapter(this)

    private val scope: Scope
        get() = GlobalContext.get().getOrCreateScope<SearchViewModel>(SHARED_SCOPE_SEARCH_ID)
    private val viewModel: SearchViewModel = scope.get()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_results, container, false)
        observeState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchResults.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeState() {
        viewModel.searchResult.observe(viewLifecycleOwner) {
            resultsAdapter.setResults(it)
        }
    }

    override fun onItemClicked(productId: String) {
        val action = ResultsFragmentDirections.actionResultsFragmentToProductDetailsFragment(productId)
        findNavController().navigate(action)
    }
}