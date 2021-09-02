package com.example.melichallenge.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.melichallenge.R
import com.example.melichallenge.databinding.FragmentSearchBinding
import com.example.melichallenge.di.SHARED_SCOPE_SEARCH_ID
import org.koin.core.context.GlobalContext
import org.koin.core.scope.Scope

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val scope: Scope
        get() = GlobalContext.get().getOrCreateScope<SearchViewModel>(SHARED_SCOPE_SEARCH_ID)
    private val viewModel: SearchViewModel by lazy { scope.get()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        initBindingArguments()
        observeState()
        return binding.root
    }

    private fun observeState() {
        viewModel.searchResult.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_searchFragment_to_resultsFragment)
        }
    }

    private fun initBindingArguments() {
        binding.viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }
}