package com.margarin.commonweather.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.margarin.commonweather.BINDING_NULL
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.ViewModelFactory
import com.margarin.commonweather.dataStore
import com.margarin.commonweather.di.WeatherComponentProvider
import com.margarin.commonweather.ui.adapter.WeatherAdapter
import com.margarin.weather.R
import com.margarin.weather.databinding.FragmentWeatherBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class WeatherFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]
    }

    private lateinit var adapter: WeatherAdapter

    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL)

    //////////////////////////////////////////////////////////////////////////////
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as WeatherComponentProvider)
            .getWeatherComponent()
            .injectWeatherFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setOnClickListeners()
        observeViewModel()
        setOnRefreshListener()
        configureRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configureRecyclerView() {
        binding.cardViewForecastByHours.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter = WeatherAdapter()
        binding.cardViewForecastByHours.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {

        binding.apply {
            viewModel.weather.observe(viewLifecycleOwner) {
                if (it?.byDaysWeatherModel?.isNotEmpty() == true &&
                    it.byHoursWeatherModel?.isNotEmpty() == true &&
                    it.currentWeatherModel?.name?.isNotEmpty() == true
                ) {
                    var tempMaxMin = "${it.byDaysWeatherModel[0].maxtemp_c} " +
                            "/ ${it.byDaysWeatherModel[0].mintemp_c}"

                    adapter.submitList(it.byHoursWeatherModel)

                    mainToolbar.apply {
                        tvCityName.text = it.currentWeatherModel.name
                        tvLastUpdate.text = it.currentWeatherModel.last_updated
                    }

                    currentCondition.apply {
                        tvCurrentTemp.text = it.currentWeatherModel.temp_c.toString()
                        tvCurrentCondition.text = it.currentWeatherModel.condition
                        tvMainMaxmin.text = tempMaxMin
                    }

                    cardViewForecastByDays.apply {
                        tv1dayMaxmin.text = tempMaxMin
                        tempMaxMin = "${it.byDaysWeatherModel[1].maxtemp_c} " +
                                "/ ${it.byDaysWeatherModel[1].mintemp_c}"
                        tv2dayMaxmin.text = tempMaxMin
                        tempMaxMin = "${it.byDaysWeatherModel[2].maxtemp_c} " +
                                "/ ${it.byDaysWeatherModel[2].mintemp_c}"
                        tv3dayMaxmin.text = tempMaxMin
                        iv1dayCondition.setImageResource(
                            it.byDaysWeatherModel[0].icon_url ?: R.drawable.ic_loading
                        )
                        iv2dayCondition.setImageResource(
                            it.byDaysWeatherModel[1].icon_url ?: R.drawable.ic_loading
                        )
                        iv3dayCondition.setImageResource(
                            it.byDaysWeatherModel[2].icon_url ?: R.drawable.ic_loading
                        )
                        tv1dayName.text = getString(R.string.today)
                        tv2dayName.text = it.byDaysWeatherModel[1].date
                        tv3dayName.text = it.byDaysWeatherModel[2].date
                        tv1dayCondition.text = it.byDaysWeatherModel[0].condition
                        tv2dayCondition.text = it.byDaysWeatherModel[1].condition
                        tv3dayCondition.text = it.byDaysWeatherModel[2].condition
                    }

                    cardViewWind.apply {
                        cardViewWind.tvWindDirection.text =
                            it.currentWeatherModel.wind_dir.toString()
                        cardViewWind.tvWindSpeed.text = it.currentWeatherModel.wind_kph.toString()
                        cardViewWind.imageView.setImageResource(
                            it.currentWeatherModel.wind_dir_img ?: R.drawable.ic_loading
                        )
                    }

                    cardViewDetails.apply {
                        cardViewDetails.tvHumidityValue.text =
                            it.currentWeatherModel.humidity.toString()
                        cardViewDetails.tvFeelsLikeValue.text =
                            it.currentWeatherModel.feels_like.toString()
                        cardViewDetails.tvUvValue.text = it.currentWeatherModel.uv.toString()
                        cardViewDetails.tvPressureValue.text =
                            it.currentWeatherModel.pressure_mb.toString()
                        cardViewDetails.tvChanceOfRainValue.text =
                            it.byDaysWeatherModel[0].chance_of_rain.toString()
                    }
                }


            }
        }
    }

    private fun initViewModel() {
        /*
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding.mainToolbar) {
                var name = EMPTY_STRING

                tvStatus.visibility = View.VISIBLE
                tvStatus.text = getString(R.string.loading)
                bSearch.isEnabled = false
                binding.swipeRefresh.isRefreshing = true

         */
        var name: String
        runBlocking {
            val dataStoreKey = stringPreferencesKey(LOCATION)
            val preferences = (requireContext().dataStore.data.first())
            name = preferences[dataStoreKey] ?: EMPTY_STRING
        }

        if (name == EMPTY_STRING) {
            viewModel.initViewModel(getString(R.string.moscow), getString(R.string.lang))
        } else {
            viewModel.initViewModel(name, getString(R.string.lang))
        }
        /*

                delay(500)
                binding.swipeRefresh.isRefreshing = false
                tvStatus.visibility = View.GONE
                tvLastUpdate.visibility = View.VISIBLE
                delay(1000)
                tvLastUpdate.visibility = View.GONE
                bSearch.isEnabled = true
            }
        }

         */
    }

    private fun setOnClickListeners() {
        binding.mainToolbar.bSearch.setOnClickListener {
            findNavController().navigate(Uri.parse(URI_CITY_LIST_FRAGMENT))
        }
        binding.mainToolbar.bRefresh.setOnClickListener {
            binding.swipeRefresh.isRefreshing = true
            initViewModel()
            binding.swipeRefresh.isRefreshing = false
        }
        binding.tvWeatherApi.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(WEATHER_API)
                )
            )
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

    companion object {
        private const val WEATHER_API = "https://www.weatherapi.com/"
        private const val URI_CITY_LIST_FRAGMENT = "weatherApp://cityListFragment"
        const val EMPTY_STRING = ""
    }
}
