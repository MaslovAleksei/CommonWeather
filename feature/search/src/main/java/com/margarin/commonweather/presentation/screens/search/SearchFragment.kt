package com.margarin.commonweather.presentation.screens.search

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.margarin.commonweather.BINDING_NULL
import com.margarin.commonweather.BUNDLE_KEY
import com.margarin.commonweather.REQUEST_KEY
import com.margarin.commonweather.ViewModelFactory
import com.margarin.commonweather.di.SearchComponentProvider
import com.margarin.commonweather.presentation.adapter.SearchAdapter
import com.margarin.search.R
import com.margarin.search.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch
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
        lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.apply {
                        when (it) {
                            is SearchState.StartedQueryText -> {
                                adapter.submitList(it.queryList)
                                svPopularCities.visibility = View.GONE
                                rvSearch.visibility = View.VISIBLE
                            }

                            is SearchState.StoppedQueryText -> {
                                svPopularCities.visibility = View.VISIBLE
                                rvSearch.visibility = View.GONE
                            }

                            is SearchState.Initial -> {

                            }
                        }
                    }
                }
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
                if (newText?.isNotBlank() == true) {
                    viewModel.sendEvent(SearchEvent.StartQuery(newText))
                } else {
                    viewModel.sendEvent(SearchEvent.StopQuery)
                }
                return true
            }
        })
    }

    private fun setOnClickListeners() {
        val controller = findNavController()

        adapter.apply {
            onItemClickListener = {
                setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to it.name.toString()))
                controller.popBackStack(ROUTE_WEATHER_FRAGMENT, false)
            }
            onButtonAddToFavClickListener = {
                viewModel.sendEvent(SearchEvent.AddSearchItem(it))
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
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(BUNDLE_KEY to (city as TextView).text.toString())
        )
        findNavController().popBackStack(ROUTE_WEATHER_FRAGMENT, false)
    }

    companion object {
        private const val ROUTE_WEATHER_FRAGMENT = "weatherFragment"
    }
}