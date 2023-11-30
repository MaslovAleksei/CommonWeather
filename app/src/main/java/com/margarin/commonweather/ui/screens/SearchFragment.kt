package com.margarin.commonweather.ui.screens

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentMainBinding
import com.margarin.commonweather.databinding.FragmentSearchBinding
import com.margarin.commonweather.ui.adapters.SearchAdapter
import com.margarin.commonweather.ui.viewmodels.MainViewModel
import com.margarin.commonweather.ui.viewmodels.ViewModelFactory
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private lateinit var adapter: SearchAdapter

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initViewModel("Tyumen")
        observeViewModel()
        configureRecyclerView()
        addTextChangeListeners()
        setOnClickListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.searchLocation.observe(viewLifecycleOwner){
            adapter.submitList(viewModel.searchLocation.value)
        }
    }

    private fun configureRecyclerView() {
        binding.rvSearch.layoutManager = LinearLayoutManager(activity)
        adapter = SearchAdapter()
        binding.rvSearch.adapter = adapter

    }

    private fun addTextChangeListeners() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let { viewModel.getSearchLocation(it) }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                newText?.let { viewModel.getSearchLocation(it) }

                return true
            }

        })
    }

    private fun setOnClickListeners(){
        adapter.onItemClickListener = {
            viewModel.loadDataFromApi(it.name)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object{
        fun newInstance() = SearchFragment()
    }


}