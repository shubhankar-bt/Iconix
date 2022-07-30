package com.shubhankaranku.iconix.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.snackbar.Snackbar
import com.shubhankaranku.iconix.R
import com.shubhankaranku.iconix.adapters.IconSetAdapter
import com.shubhankaranku.iconix.repository.IconifyRepository
import com.shubhankaranku.iconix.utils.Resource
import com.shubhankaranku.iconix.viewModel.iconSet.PublicIconSetViewModel
import com.shubhankaranku.iconix.viewModel.iconSet.PublicIconSetViewModelFactory
import kotlinx.android.synthetic.main.fragment_icon_set.*


class IconSetFragment : Fragment(R.layout.fragment_icon_set) {

    lateinit var publicIconSetViewModel: PublicIconSetViewModel
    lateinit var iconSetAdapter: IconSetAdapter
    lateinit var snackbar: Snackbar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpRecyclerView()
        snackbar = Snackbar.make(requireParentFragment().requireView(), "Loading...", Snackbar.LENGTH_INDEFINITE)


        val repository = IconifyRepository()
        val publicIconSetViewModelFactory =
            PublicIconSetViewModelFactory(requireActivity().application, repository)

        publicIconSetViewModel = ViewModelProvider(
            requireActivity(),
            publicIconSetViewModelFactory
        ).get(PublicIconSetViewModel::class.java)


        //Observing changes
        publicIconSetViewModel.publicIconSets.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { iconSetResponse ->
                        snackbar.dismiss()
                        iconSetAdapter.differ.submitList(iconSetResponse.iconsets.toList())
                    }
                }

                is Resource.Error -> {
                    snackbar.dismiss()
                    Snackbar.make(
                        view,
                        "Failed due to ${response.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    snackbar.show()
                }
            }

        })


        //Handling Clicks
        iconSetAdapter.setOnItemClickListener { iconset ->

            val bundle = Bundle()
            bundle.putInt("iconset_id", iconset.iconset_id)
            bundle.putString("iconSet", iconset.name)
            bundle.putString("type", iconset.type)
            bundle.putString("author", iconset.author.name)
            bundle.putInt("price", iconset.prices[0].price)
            bundle.putString("license", iconset.prices[0].license.name)
            bundle.putInt("user_id", iconset.author.user_id)
            findNavController().navigate(
                R.id.action_viewPagerFragment_to_iconSetDetailsFragment,
                bundle
            )
        }

    }


    private fun setUpRecyclerView() {
        iconSetAdapter = IconSetAdapter()
        rv_icon_set.apply {
            adapter = iconSetAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }


}