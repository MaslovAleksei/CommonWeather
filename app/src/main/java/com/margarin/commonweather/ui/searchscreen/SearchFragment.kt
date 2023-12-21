package com.margarin.commonweather.ui.searchscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.margarin.commonweather.R
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentSearchBinding
import com.margarin.commonweather.ui.searchscreen.adapter.SearchAdapter
import com.margarin.commonweather.ui.dataStore
import com.margarin.commonweather.ui.ViewModelFactory
import com.margarin.commonweather.utils.BINDING_NULL
import com.margarin.commonweather.utils.BUNDLE_KEY
import com.margarin.commonweather.utils.CITY_LIST_FRAGMENT
import com.margarin.commonweather.utils.LOCATION
import com.margarin.commonweather.utils.REQUEST_KEY
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL)

    private lateinit var adapter: SearchAdapter

    ////////////////////////////////////////////////////////////////////////////
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
        viewModel.definiteLocation.observe(viewLifecycleOwner) {
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
        adapter.apply {
            onItemClickListener = {
                saveToDataStore(it.name.toString())
                setFragmentResult(it.name.toString())
                requireActivity().supportFragmentManager.popBackStack(
                    CITY_LIST_FRAGMENT,
                    -1
                )
            }
            onButtonAddToFavClickListener = {
                viewModel.addSearchItem(it)
                requireActivity().supportFragmentManager.popBackStack(
                    CITY_LIST_FRAGMENT,
                    0
                )
            }
        }

        binding.apply {
            bCancel.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
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
        saveToDataStore((city as TextView).text.toString())
        setFragmentResult((city).text.toString())
        requireActivity().supportFragmentManager.popBackStack(
            CITY_LIST_FRAGMENT,
            -1
        )
    }

    private fun saveToDataStore(name: String) {
        val dataStoreKey = stringPreferencesKey(LOCATION)
        runBlocking {
            requireContext().dataStore.edit { settings ->
                settings[dataStoreKey] = name
            }
        }
    }

    private fun setFragmentResult(name: String) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(BUNDLE_KEY to name)
        )
    }

    /////////////////////////////////////////////////////////////////////////////////
    companion object {
        fun newInstance() = SearchFragment()
    }
}