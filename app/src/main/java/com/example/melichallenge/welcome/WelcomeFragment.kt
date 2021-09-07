package com.example.melichallenge.welcome

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.melichallenge.R
import com.example.melichallenge.search.SearchActivity
import com.example.melichallenge.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.searchButton.setOnClickListener {
           with(Intent(context, SearchActivity::class.java)) {
               action = Intent.ACTION_SEARCH
               putExtra(SearchManager.QUERY, binding.searchEditText.text.toString())
               startActivity(this)
           }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}