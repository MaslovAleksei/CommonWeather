package com.margarin.commonweather.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentMainBinding
import com.margarin.commonweather.ui.viewmodels.MainViewModel
import com.margarin.commonweather.ui.viewmodels.ViewModelFactory
import com.margarin.commonweather.utils.launchFragment
import com.margarin.commonweather.utils.readFromDataStore
import com.squareup.picasso.Picasso
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

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private var location: String = UNDEFINED_LOCATION

    //////////////////////////////////////////////////////////////////////////////
    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            location = readFromDataStore(LOCATION) ?: UNDEFINED_LOCATION
        }
        initViewModel(location)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {

        viewModel.currentWeather?.observe(viewLifecycleOwner) {
            binding.tvCityname.text = it.location
            binding.tvCurrentTemp.text = it.temp_c.toString()
        }

        viewModel.byDaysWeather?.observe(viewLifecycleOwner) {
            binding.tvTommorowDate.text = it[1].date
        }

        viewModel.byHoursWeather?.observe(viewLifecycleOwner) {
            binding.tvHourDate.text = it[0].time
        }

        viewModel.currentWeather?.observe(viewLifecycleOwner) {
            Picasso.get().load(it.icon_url).into(binding.imageView)
        }
    }

    private fun initViewModel(location: String) {
        if (location == UNDEFINED_LOCATION) {
            viewModel.initViewModel(DEFAULT_LOCATION)
        } else {
            viewModel.initViewModel(location)
        }

    }

    private fun setOnClickListeners() {
        binding.bSearch.setOnClickListener {
            launchFragment(CityListFragment.newInstance(), "CityListFragment")
        }
    }

    ////////////////////////////////////////////////////////////////////
    companion object {

        const val LOCATION = "location"
        private const val UNDEFINED_LOCATION = ""
        private const val DEFAULT_LOCATION = "Москва"
    }
}
