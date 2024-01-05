package com.margarin.commonweather.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.margarin.commonweather.BINDING_NULL
import com.margarin.commonweather.BUNDLE_KEY
import com.margarin.commonweather.REQUEST_KEY
import com.margarin.commonweather.ViewModelFactory
import com.margarin.commonweather.di.SearchComponentProvider
import com.margarin.commonweather.ui.adapter.SearchAdapter
import com.margarin.commonweather.ui.viewmodels.SearchViewModel
import com.margarin.search.R
import com.margarin.search.databinding.FragmentSearchBinding
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL)

    private lateinit var adapter: SearchAdapter



    ////////////////////////////////////////////////////////////////////////////
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as SearchComponentProvider)
            .getSearchComponent()
            .injectSearchFragment(this)
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
        viewModel.requestLocation.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun configureRecyclerView() {
        binding.rvSearch.layoutManager = LinearLayoutManager(activity)
        adapter = SearchAdapter(R.layout.search_item)
        binding.rvSearch.adapter = adapter
    }

    private fun addTextChangeListeners() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.changeDefiniteLocation(it) }
                if (newText?.isNotBlank() == true) {
                    binding.svPopularCities.visibility = View.GONE
                    binding.rvSearch.visibility = View.VISIBLE
                } else {
                    binding.svPopularCities.visibility = View.VISIBLE
                    binding.rvSearch.visibility = View.GONE
                }
                return true
            }
        })
    }

    private fun setOnClickListeners() {
        val controller = findNavController()

        adapter.apply {
            onItemClickListener = {
                viewModel.saveToDataStore(it.name.toString())
                setFragmentResult(it.name.toString())
                controller.popBackStack(ROUTE_WEATHER_FRAGMENT, false)
            }
            onButtonAddToFavClickListener = {
                viewModel.addSearchItem(it)
                controller.popBackStack()
            }
        }

        binding.apply {
            bCancel.setOnClickListener {
                controller.navigateUp()
            }

            tvAlmaty.setOnClickListener { clickOnPopularCity(it) }
            tvVoronezh.setOnClickListener { clickOnPopularCity(it) }
            tvVolgograd.setOnClickListener { clickOnPopularCity(it) }
            tvVilnius.setOnClickListener { clickOnPopularCity(it) }
            tvTyumen.setOnClickListener { clickOnPopularCity(it) }
            tvTbilisi.setOnClickListener { clickOnPopularCity(it) }
            tvTashkent.setOnClickListener { clickOnPopularCity(it) }
            tvTallinn.setOnClickListener { clickOnPopularCity(it) }
            tvSamara.setOnClickListener { clickOnPopularCity(it) }
            tvRiga.setOnClickListener { clickOnPopularCity(it) }
            tvPerm.setOnClickListener { clickOnPopularCity(it) }
            tvPetersburg.setOnClickListener { clickOnPopularCity(it) }
            tvOmsk.setOnClickListener { clickOnPopularCity(it) }
            tvNovosibirsk.setOnClickListener { clickOnPopularCity(it) }
            tvMoscow.setOnClickListener { clickOnPopularCity(it) }
            tvMinsk.setOnClickListener { clickOnPopularCity(it) }
            tvKrasnoyarsk.setOnClickListener { clickOnPopularCity(it) }
            tvKyiv.setOnClickListener { clickOnPopularCity(it) }
            tvKazan.setOnClickListener { clickOnPopularCity(it) }
            tvErevan.setOnClickListener { clickOnPopularCity(it) }
            tvEkaterinburg.setOnClickListener { clickOnPopularCity(it) }
            tvBucharest.setOnClickListener { clickOnPopularCity(it) }
            tvChelyabinsk.setOnClickListener { clickOnPopularCity(it) }
            tvBishkek.setOnClickListener { clickOnPopularCity(it) }
            tvBaku.setOnClickListener { clickOnPopularCity(it) }
            tvAstana.setOnClickListener { clickOnPopularCity(it) }
            tvAshkhabad.setOnClickListener { clickOnPopularCity(it) }
        }
    }

    private fun clickOnPopularCity(city: View) {
        viewModel.saveToDataStore((city as TextView).text.toString())
        setFragmentResult((city).text.toString())
        findNavController().popBackStack(ROUTE_WEATHER_FRAGMENT, false)
    }

    private fun setFragmentResult(name: String) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(BUNDLE_KEY to name)
        )
    }

    companion object {
        private const val ROUTE_WEATHER_FRAGMENT = "weatherFragment"
    }
}