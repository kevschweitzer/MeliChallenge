package com.example.melichallenge.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.melichallenge.R
import com.example.melichallenge.databinding.FragmentProductDetailsBinding
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProductDetailsFragment : Fragment() {

    private val args: ProductDetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by inject { parametersOf(args.product) }
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        binding.viewModel = viewModel
        loadThumbnail()
        return binding.root
    }

    private fun loadThumbnail() {
        Picasso.get().load(viewModel.product.thumbnailUrl).into(binding.thumbnail)
    }
}