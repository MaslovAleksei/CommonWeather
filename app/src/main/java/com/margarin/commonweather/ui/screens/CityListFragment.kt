package com.margarin.commonweather.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.margarin.commonweather.R
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentCityListBinding
import com.margarin.commonweather.ui.adapters.SearchAdapter
import com.margarin.commonweather.ui.viewmodels.SearchViewModel
import com.margarin.commonweather.ui.viewmodels.ViewModelFactory
import com.margarin.commonweather.utils.launchFragment
import javax.inject.Inject


class CityListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private var _binding: FragmentCityListBinding? = null
    private val binding: FragmentCityListBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private lateinit var adapter: SearchAdapter

    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadSearchList()
        observeViewModel()
        configureRecyclerView()
        setOnClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
       viewModel.searchList?.observe(viewLifecycleOwner){
           adapter.submitList(it)
       }
    }

    private fun configureRecyclerView() {
        binding.rvCityList.layoutManager = LinearLayoutManager(activity)
        adapter = SearchAdapter(R.layout.city_item)
        binding.rvCityList.adapter = adapter
    }

    private fun setOnClickListeners(){
        adapter.onItemClickListener = {
            launchFragment(MainFragment.newInstance(it.name))
        }
        binding.bInputLocation.setOnClickListener {
            launchFragment(SearchFragment.newInstance())
        }
        binding.bBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    companion object {

        fun newInstance() =
            CityListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}