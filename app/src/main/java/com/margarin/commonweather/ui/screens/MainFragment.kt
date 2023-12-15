package com.margarin.commonweather.ui.screens

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentMainBinding
import com.margarin.commonweather.ui.viewmodels.MainViewModel
import com.margarin.commonweather.ui.viewmodels.ViewModelFactory
import com.margarin.commonweather.utils.launchFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
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

        setOnClickListeners()
        observeViewModel()
        Log.d("tag", "onViewCreated ${viewModel.currentWeather.value?.name}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {

        viewModel.currentWeather.observe(requireActivity()) {
            Log.d("tag", "observe ${it?.name}")
            binding.tvCityname.text = it?.name
            binding.tvCurrentTemp.text = it?.temp_c.toString()
            Picasso.get().load(it?.icon_url).into(binding.imageView)
        }

        viewModel.byDaysWeather?.observe(viewLifecycleOwner) {
            binding.tvTommorowDate.text = it[1].date
        }

        viewModel.byHoursWeather?.observe(viewLifecycleOwner) {
            binding.tvHourDate.text = it[0].time
        }
    }

    private fun initViewModel() {
        var latLon: String

        runBlocking {
            val dataStoreKey = stringPreferencesKey(LOCATION)
            val preferences = (requireContext().dataStore.data.first())
            latLon = preferences[dataStoreKey] ?: UNDEFINED_LOCATION
        }

        if (latLon == UNDEFINED_LOCATION) {
            viewModel.initViewModel(DEFAULT_LOCATION)
        } else {
            viewModel.initViewModel(latLon)
        }
    }

    private fun setOnClickListeners() {
        binding.bSearch.setOnClickListener {
            launchFragment(CityListFragment.newInstance(), "CityListFragment")
        }

        binding.textView5.setOnClickListener {
            binding.textView5.text = viewModel.currentWeather.value?.name
        }

        binding.textView3.setOnClickListener {
            binding.textView3.text = viewModel.test()
        }
    }

    ////////////////////////////////////////////////////////////////////
    companion object {

        const val LOCATION = "location"
        private const val UNDEFINED_LOCATION = ""
        private const val DEFAULT_LOCATION = "Москва"
    }
}
