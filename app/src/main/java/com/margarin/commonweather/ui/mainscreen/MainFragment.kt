package com.margarin.commonweather.ui.mainscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.margarin.commonweather.BINDING_NULL
import com.margarin.commonweather.BUNDLE_KEY
import com.margarin.commonweather.EMPTY_STRING
import com.margarin.commonweather.LOCATION
import com.margarin.commonweather.R
import com.margarin.commonweather.REQUEST_KEY
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentMainBinding
import com.margarin.commonweather.ui.ViewModelFactory
import com.margarin.commonweather.ui.dataStore
import com.margarin.commonweather.ui.mainscreen.adapter.WeatherAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    private lateinit var adapter: WeatherAdapter

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL)

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

        viewModel.location.observe(viewLifecycleOwner) { result ->
            result?.let { name -> viewModel.initViewModel(name, getString(R.string.lang)) }
        }

        binding.apply {
            viewModel.currentWeather.observe(viewLifecycleOwner) {
                mainToolbar.tvCityName.text = it?.name
                mainToolbar.tvLastUpdate.text = it?.last_updated
                currentCondition.tvCurrentTemp.text = it?.temp_c.toString()
                currentCondition.tvCurrentCondition.text = it?.condition
                cardViewWind.tvWindDirection.text = it?.wind_dir.toString()
                cardViewWind.tvWindSpeed.text = it?.wind_kph.toString()
                cardViewDetails.tvHumidityValue.text = it?.humidity.toString()
                cardViewDetails.tvFeelsLikeValue.text = it?.feels_like.toString()
                cardViewDetails.tvUvValue.text = it?.uv.toString()
                cardViewDetails.tvPressureValue.text = it?.pressure_mb.toString()
                cardViewWind.imageView.setImageResource(it?.wind_dir_img ?: R.drawable.ic_loading)
            }

            viewModel.byDaysWeather.observe(viewLifecycleOwner) {
                cardViewForecastByDays.apply {
                    if (it?.isNotEmpty() == true) {
                        var tempMaxMin = "${it[0].maxtemp_c} / ${it[0].mintemp_c}"
                        currentCondition.tvMainMaxmin.text = tempMaxMin
                        tv1dayMaxmin.text = tempMaxMin
                        tempMaxMin = "${it[1].maxtemp_c} / ${it[1].mintemp_c}"
                        tv2dayMaxmin.text = tempMaxMin
                        tempMaxMin = "${it[2].maxtemp_c} / ${it[2].mintemp_c}"
                        tv3dayMaxmin.text = tempMaxMin
                        iv1dayCondition.setImageResource(it[0].icon_url ?: R.drawable.ic_loading)
                        iv2dayCondition.setImageResource(it[1].icon_url ?: R.drawable.ic_loading)
                        iv3dayCondition.setImageResource(it[2].icon_url ?: R.drawable.ic_loading)
                        tv1dayName.text = getString(R.string.today)
                        tv2dayName.text = it[1].date
                        tv3dayName.text = it[2].date
                        tv1dayCondition.text = it[0].condition
                        tv2dayCondition.text = it[1].condition
                        tv3dayCondition.text = it[2].condition
                        cardViewDetails.tvChanceOfRainValue.text = it[0].chance_of_rain.toString()
                    }
                }
            }
            viewModel.byHoursWeather.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding.mainToolbar) {
                var name = EMPTY_STRING
                tvStatus.visibility = View.VISIBLE
                tvStatus.text = getString(R.string.loading)
                bSearch.isEnabled = false
                binding.swipeRefresh.isRefreshing = true

                lifecycleScope.launch(Dispatchers.IO) {
                    val dataStoreKey = stringPreferencesKey(LOCATION)
                    val preferences = (requireContext().dataStore.data.first())
                    name = preferences[dataStoreKey] ?: EMPTY_STRING
                }.join()

                if (name == EMPTY_STRING) {
                    viewModel.initViewModel(getString(R.string.moscow), getString(R.string.lang))
                } else {
                    viewModel.initViewModel(name, getString(R.string.lang))
                }
                delay(500)
                binding.swipeRefresh.isRefreshing = false
                tvStatus.visibility = View.GONE
                tvLastUpdate.visibility = View.VISIBLE
                delay(1000)
                tvLastUpdate.visibility = View.GONE
                bSearch.isEnabled = true
            }
        }
    }

    private fun setOnClickListeners() {
        binding.mainToolbar.bSearch.setOnClickListener {
            val controller = findNavController()
            controller.navigate(R.id.action_mainFragment_to_cityListFragment)
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

    private fun getResultFromChildFragment() {
        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            bundle.getString(BUNDLE_KEY)?.let { viewModel.setLocationValue(it) }
        }
    }

    companion object {
        private const val WEATHER_API = "https://www.weatherapi.com/"
    }
}
