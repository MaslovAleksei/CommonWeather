package com.margarin.commonweather.presentation.legacy//package com.margarin.commonweather.presentation
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.setFragmentResultListener
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.flowWithLifecycle
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.margarin.commonweather.BINDING_NULL
//import com.margarin.commonweather.BUNDLE_KEY
//import com.margarin.commonweather.LOCATION
//import com.margarin.commonweather.REQUEST_KEY
//import com.margarin.commonweather.ViewModelFactory
//import com.margarin.commonweather.di.WeatherComponentProvider
//import com.margarin.commonweather.loadFromDataStore
//import com.margarin.commonweather.presentation.adapter.WeatherAdapter
//
//import com.margarin.commonweather.saveToDataStore
//import com.margarin.weather.R
//import com.margarin.weather.databinding.FragmentWeatherBinding
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//class WeatherFragment : Fragment() {
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory
//
//    private val viewModel by lazy {
//        ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]
//    }
//
//    private lateinit var adapter: WeatherAdapter
//
//    private var _binding: FragmentWeatherBinding? = null
//    private val binding: FragmentWeatherBinding
//        get() = _binding ?: throw RuntimeException(BINDING_NULL)
//
//    //////////////////////////////////////////////////////////////////////////////
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        (context.applicationContext as WeatherComponentProvider)
//            .getWeatherComponent()
//            .injectWeatherFragment(this)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        initViewModel()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setResultFromChildFragment()
//        setOnListeners()
//        observeViewModel()
//        configureRecyclerView()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun configureRecyclerView() {
//        binding.cardViewForecastByHours.recyclerView.layoutManager = LinearLayoutManager(
//            requireContext(),
//            LinearLayoutManager.HORIZONTAL,
//            false
//        )
//        adapter = WeatherAdapter()
//        binding.cardViewForecastByHours.recyclerView.adapter = adapter
//    }
//
//    private fun observeViewModel() {
//        lifecycleScope.launch {
//            viewModel.state
//                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect {
//                    binding.apply {
//                        when (it) {
//                            is WeatherState.Initial -> {
//
//                            }
//
//                            is WeatherState.Loading -> {
//                                swipeRefresh.isRefreshing = true
//                                currentCondition.root.visibility = View.VISIBLE
//                                tvLoadingError.visibility = View.GONE
//                                scrollView.visibility = View.VISIBLE
//                            }
//
//                            is WeatherState.Error -> {
//                                mainToolbar.tvCityName.visibility = View.GONE
//                                mainToolbar.tvLastUpdate.visibility = View.GONE
//                                tvLoadingError.visibility = View.VISIBLE
//                                scrollView.visibility = View.GONE
//                                currentCondition.root.visibility = View.GONE
//                                swipeRefresh.isRefreshing = false
//                            }
//
//                            is WeatherState.Success -> {
//                                mainToolbar.tvLastUpdate.visibility = View.VISIBLE
//                                swipeRefresh.isRefreshing = false
//                                currentCondition.root.visibility = View.VISIBLE
//                                tvLoadingError.visibility = View.GONE
//                                scrollView.visibility = View.VISIBLE
//                            }
//
//                            is WeatherState.Info -> {
//                               adapter.submitList(it.weather.byHoursWeatherModel)
//
//                                tvLoadingError.visibility = View.GONE
//                                scrollView.visibility = View.VISIBLE
//
//                                var tempMaxMin =
//                                    "${it.weather.byDaysWeatherModel?.get(0)?.maxtemp_c} " +
//                                            "/ ${it.weather.byDaysWeatherModel?.get(0)?.mintemp_c}"
//
//                                mainToolbar.apply {
//                                    tvCityName.visibility = View.VISIBLE
//                                    tvCityName.text = it.weather.currentWeatherModel?.name
//                                    tvLastUpdate.text = it.weather.currentWeatherModel?.last_updated
//                                    binding.swipeRefresh.isRefreshing = false
//                                    tvLastUpdate.visibility = View.GONE
//                                }
//
//                                currentCondition.apply {
//                                    root.visibility = View.VISIBLE
//                                    tvCurrentTemp.text =
//                                        it.weather.currentWeatherModel?.temp_c.toString()
//                                    tvCurrentCondition.text =
//                                        it.weather.currentWeatherModel?.condition
//                                    tvMainMaxmin.text = tempMaxMin
//                                    celsius.text = getString(R.string.celsius)
//                                    degree.text = getString(R.string.degree)
//                                }
//
//                                cardViewForecastByDays.apply {
//                                    tv1dayMaxmin.text = tempMaxMin
//                                    tempMaxMin =
//                                        "${it.weather.byDaysWeatherModel?.get(1)?.maxtemp_c} " +
//                                                "/ ${it.weather.byDaysWeatherModel?.get(1)?.mintemp_c}"
//                                    tv2dayMaxmin.text = tempMaxMin
//                                    tempMaxMin =
//                                        "${it.weather.byDaysWeatherModel?.get(2)?.maxtemp_c} " +
//                                                "/ ${it.weather.byDaysWeatherModel?.get(2)?.mintemp_c}"
//                                    tv3dayMaxmin.text = tempMaxMin
//                                    iv1dayCondition.setImageResource(
//                                        it.weather.byDaysWeatherModel?.get(0)?.icon_url!!
//                                    )
////                                    iv2dayCondition.setImageResource(
////                                        it.weather.byDaysWeatherModel[1].icon_url!!
////                                    )
////                                    iv3dayCondition.setImageResource(
////                                        it.weather.byDaysWeatherModel[2].icon_url!!
////                                    )
////                                    tv1dayName.text = getString(R.string.today)
////                                    tv2dayName.text = it.weather.byDaysWeatherModel[1].date
////                                    tv3dayName.text = it.weather.byDaysWeatherModel[2].date
////                                    tv1dayCondition.text =
////                                        it.weather.byDaysWeatherModel[0].condition
////                                    tv2dayCondition.text =
////                                        it.weather.byDaysWeatherModel[1].condition
////                                    tv3dayCondition.text =
////                                        it.weather.byDaysWeatherModel[2].condition
//                                }
//
//                                cardViewWind.apply {
//                                    cardViewWind.tvWindDirection.text =
//                                        it.weather.currentWeatherModel?.wind_dir.toString()
//                                    cardViewWind.tvWindSpeed.text =
//                                        it.weather.currentWeatherModel?.wind_kph.toString()
//                                    cardViewWind.imageView.setImageResource(
//                                        it.weather.currentWeatherModel?.wind_dir_img!!
//
//                                    )
//                                }
//
//                                cardViewDetails.apply {
//                                    cardViewDetails.tvHumidityValue.text =
//                                        it.weather.currentWeatherModel?.humidity.toString()
//                                    cardViewDetails.tvFeelsLikeValue.text =
//                                        it.weather.currentWeatherModel?.feels_like.toString()
//                                    cardViewDetails.tvUvValue.text =
//                                        it.weather.currentWeatherModel?.uv.toString()
//                                    cardViewDetails.tvPressureValue.text =
//                                        it.weather.currentWeatherModel?.pressure_mb.toString()
//                                    cardViewDetails.tvChanceOfRainValue.text =
//                                        it.weather.byDaysWeatherModel?.get(0)?.chance_of_rain.toString()
//                                }
//                            }
//                        }
//                    }
//                }
//        }
//    }
//
//    private fun setOnListeners() {
//        binding.mainToolbar.bSearch.setOnClickListener {
//            findNavController().navigate(Uri.parse(URI_CITY_LIST_FRAGMENT))
//        }
//        binding.mainToolbar.bRefresh.setOnClickListener {
//            initViewModel()
//        }
//        binding.tvWeatherApi.setOnClickListener {
//            startActivity(
//                Intent(Intent.ACTION_VIEW, Uri.parse(WEATHER_API))
//            )
//        }
//        binding.swipeRefresh.setOnRefreshListener {
//            initViewModel()
//        }
//    }
//
//    private fun initViewModel() {
//        lifecycleScope.launch {
//            val value = loadFromDataStore(requireContext(), LOCATION, getString(R.string.moscow))
//            viewModel.sendEvent(WeatherEvent.RefreshWeather(value))
//        }
//    }
//
//    private fun setResultFromChildFragment() {
//        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
//            val result = bundle.getString(BUNDLE_KEY)
//            if (result != null) {
//                lifecycleScope.launch {
//                    viewModel.sendEvent(WeatherEvent.RefreshWeather(result))
//                    saveToDataStore(requireContext(), LOCATION, result)
//                }
//            }
//        }
//    }
//
//    companion object {
//        private const val WEATHER_API = "https://www.weatherapi.com/"
//        private const val URI_CITY_LIST_FRAGMENT = "weatherApp://cityListFragment"
//    }
//}
