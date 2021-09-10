package com.example.melichallenge.welcome

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.melichallenge.R
import com.example.melichallenge.search.presentation.SearchActivity
import com.example.melichallenge.databinding.FragmentWelcomeBinding
import com.example.melichallenge.utils.observeInLifecycle
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WelcomeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        binding.viewModel = viewModel
        observeEvents()
        return binding.root
    }

    private fun observeEvents() {
        viewModel.eventsFlow.onEach {
            when(it) {
                is WelcomeUIEvent.OnSearchCorrect -> doSearch(it.query)
                WelcomeUIEvent.OnSearchFailure -> searchFailure()
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun searchFailure() {
        Toast.makeText(context, getString(R.string.search_error_text), Toast.LENGTH_SHORT).show()
    }

    private fun doSearch(query: String) {
        with(Intent(context, SearchActivity::class.java)) {
            action = Intent.ACTION_SEARCH
            putExtra(SearchManager.QUERY, query)
            startActivity(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}