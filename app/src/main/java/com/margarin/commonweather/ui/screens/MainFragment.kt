package com.margarin.commonweather.ui.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentMainBinding
import com.margarin.commonweather.ui.viewmodels.MainViewModel
import com.margarin.commonweather.ui.viewmodels.ViewModelFactory
import com.margarin.commonweather.utils.BUNDLE_KEY
import com.margarin.commonweather.utils.CITY_LIST_FRAGMENT
import com.margarin.commonweather.utils.DEFAULT_LOCATION
import com.margarin.commonweather.utils.LOCATION
import com.margarin.commonweather.utils.REQUEST_KEY
import com.margarin.commonweather.utils.EMPTY_STRING
import com.margarin.commonweather.utils.launchFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    //////////////////////////////////////////////////////////////////////////////
    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getResultFromChildFragment()
        setOnClickListeners()
        observeViewModel()
        setOnRefreshListener()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {

        viewModel.location.observe(viewLifecycleOwner) { result ->
            result?.let { name -> viewModel.initViewModel(name) }
        }

        binding.apply {
            viewModel.currentWeather.observe(viewLifecycleOwner) {
                tvCityName.text = it?.name
                tvLastUpdate.text = it?.last_updated
                Picasso.get().load(it?.icon_url).into(ivCurrentCondition)
                tvCurrentTemp.text = it?.temp_c.toString()
                tvCurrentCondition.text = it?.condition


            }

            viewModel.byDaysWeather.observe(viewLifecycleOwner) {
                if (it?.isNotEmpty() == true) {
                    var tempMaxMin = "${it[0].maxtemp_c} / ${it[0].mintemp_c}"
                    tvMainMaxmin.text = tempMaxMin
                    Picasso.get().load(it[0].icon_url).into(cardViewForecast.iv1dayCondition)
                    Picasso.get().load(it[1].icon_url).into(cardViewForecast.iv2dayCondition)
                    Picasso.get().load(it[2].icon_url).into(cardViewForecast.iv3dayCondition)
                    cardViewForecast.tv1dayName.text = "Today"
                    cardViewForecast.tv2dayName.text = it[1].day_of_week
                    cardViewForecast.tv3dayName.text = it[2].day_of_week
                    cardViewForecast.tv1dayCondition.text = it[0].condition
                    cardViewForecast.tv2dayCondition.text = it[1].condition
                    cardViewForecast.tv3dayCondition.text = it[2].condition
                    cardViewForecast.tv1dayMaxmin.text = tempMaxMin
                    tempMaxMin = "${it[1].maxtemp_c} / ${it[1].mintemp_c}"
                    cardViewForecast.tv2dayMaxmin.text = tempMaxMin
                    tempMaxMin = "${it[2].maxtemp_c} / ${it[2].mintemp_c}"
                    cardViewForecast.tv3dayMaxmin.text = tempMaxMin
                }

            }

            viewModel.byHoursWeather.observe(viewLifecycleOwner) {

            }
        }
    }

    private fun initViewModel() {
        var name: String

        runBlocking {
            val dataStoreKey = stringPreferencesKey(LOCATION)
            val preferences = (requireContext().dataStore.data.first())
            name = preferences[dataStoreKey] ?: EMPTY_STRING
        }

        if (name == EMPTY_STRING) {
            viewModel.initViewModel(DEFAULT_LOCATION)
        } else {
            viewModel.initViewModel(name)
        }
    }

    private fun setOnClickListeners() {
        binding.bSearch.setOnClickListener {
            launchFragment(CityListFragment.newInstance(), CITY_LIST_FRAGMENT)
        }
        binding.bRefresh.setOnClickListener {
                binding.swipeRefresh.isRefreshing = true
                initViewModel()
                binding.swipeRefresh.isRefreshing = false
        }


    }

    private fun setOnRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                initViewModel()
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun getResultFromChildFragment() {
        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            bundle.getString(BUNDLE_KEY)?.let { viewModel.setLocationValue(it) }
        }
    }
}
