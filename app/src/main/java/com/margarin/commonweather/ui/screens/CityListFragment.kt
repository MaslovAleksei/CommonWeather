package com.margarin.commonweather.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.margarin.commonweather.R
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentCityListBinding
import com.margarin.commonweather.ui.adapters.SearchAdapter
import com.margarin.commonweather.ui.adapters.setItemTouchHelper
import com.margarin.commonweather.ui.viewmodels.SearchViewModel
import com.margarin.commonweather.ui.viewmodels.ViewModelFactory
import com.margarin.commonweather.utils.launchFragment
import com.yandex.mapkit.MapKitFactory
import javax.inject.Inject


class CityListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as WeatherApp).component
    }

    private var _binding: FragmentCityListBinding? = null
    private val binding: FragmentCityListBinding
        get() = _binding ?: throw RuntimeException("binding == null")

    private lateinit var adapter: SearchAdapter

    //////////////////////////////////////////////////////////////////////
    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadSearchList()
        observeViewModel()
        configureRecyclerView()
        setOnClickListeners()
        setItemTouchHelper(requireContext(), binding.rvCityList, adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        MapKitFactory.getInstance().onStop()
    }

    private fun observeViewModel() {
        viewModel.searchList?.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun configureRecyclerView() {
        binding.rvCityList.layoutManager = LinearLayoutManager(activity)
        adapter = SearchAdapter(R.layout.city_item)
        binding.rvCityList.adapter = adapter
    }

    private fun setOnClickListeners() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val map = binding.mapview.mapWindow.map

        with(adapter) {

            onItemClickListener = {
                it.name?.let { name -> viewModel.changeSearchItem(name) }
                requireActivity().supportFragmentManager.popBackStack()
            }
            onButtonDeleteClickListener = {
                viewModel.deleteSearchItem(it)
            }
        }
        with(binding) {

            bInputLocation.setOnClickListener {
                launchFragment(SearchFragment.newInstance(), "SearchFragment")
            }

            bBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            bDefineLoc.setOnClickListener {
                if (mapContainer.isGone) {
                    viewModel.isGpsEnabled(fusedLocationClient, mapContainer.isGone, map)
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    viewModel.isGpsEnabled(fusedLocationClient, mapContainer.isGone, map)
                }
            }

            bMap.setOnClickListener {
                if (mapContainer.isGone) {
                    MapKitFactory.getInstance().onStart()
                    mapview.onStart()
                    viewModel.configureMap(map)

                    mapContainer.visibility = View.VISIBLE
                    rvCityList.visibility = View.GONE
                    bMap.text = "Close Map"

                } else {
                    MapKitFactory.getInstance().onStop()

                    mapContainer.visibility = View.GONE
                    rvCityList.visibility = View.VISIBLE
                    bMap.text = "Point at map"
                }
            }

            bSavePoint.setOnClickListener {
                val latLonString =
                    "${map.cameraPosition.target.latitude}, ${map.cameraPosition.target.longitude}"
                viewModel.getSearchLocation(latLonString)
                viewModel.searchLocation.observe(requireActivity()) {
                    viewModel.addSearchItem(it?.first() ?: return@observe)
                }
                MapKitFactory.getInstance().onStop()

                mapContainer.visibility = View.GONE
                rvCityList.visibility = View.VISIBLE
                bMap.text = "Point at map"
            }

            bZoomIn.setOnClickListener {
                viewModel.changeZoomByStep(ZOOM_STEP, map)
            }

            bZoomOut.setOnClickListener {
                viewModel.changeZoomByStep(-ZOOM_STEP, map)
            }

            bCurrentLoc.setOnClickListener {
                viewModel.isGpsEnabled(fusedLocationClient, mapContainer.isGone, map)
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    companion object {
        private const val ZOOM_STEP = 1f

        fun newInstance() =
            CityListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}