package com.margarin.commonweather.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
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

        viewModel.currentWeather.observe(viewLifecycleOwner) {
            binding.tvCityname.text = it?.name
            binding.tvCurrentTemp.text = it?.temp_c.toString()
            binding.textView3.text = it?.last_updated
            Picasso.get().load(it?.icon_url).into(binding.imageView)
        }

        viewModel.byDaysWeather.observe(viewLifecycleOwner) {

        }

        viewModel.byHoursWeather.observe(viewLifecycleOwner) {
            binding.textView5.text = it?.first()?.time
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

        binding.textView5.setOnClickListener {
            binding.textView5.text = viewModel.currentWeather.value?.name
        }

        binding.textView3.setOnClickListener {

        }
    }

    private fun setOnRefreshListener() {
        binding.swipeRefresh.setOnRefreshListener {
            initViewModel()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun getResultFromChildFragment() {
        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            bundle.getString(BUNDLE_KEY)?.let { viewModel.setLocationValue(it) }
        }
    }
}
