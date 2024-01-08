package com.margarin.commonweather.ui.screens.citylist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.margarin.commonweather.BINDING_NULL
import com.margarin.commonweather.BUNDLE_KEY
import com.margarin.commonweather.REQUEST_KEY
import com.margarin.commonweather.ViewModelFactory
import com.margarin.commonweather.di.SearchComponentProvider
import com.margarin.commonweather.ui.adapter.SearchAdapter
import com.margarin.commonweather.utils.YandexMapManager
import com.margarin.search.R
import com.margarin.search.databinding.FragmentCityListBinding
import com.yandex.mapkit.MapKitFactory
import javax.inject.Inject


class CityListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CityListViewModel::class.java]
    }

    private var _binding: FragmentCityListBinding? = null
    private val binding: FragmentCityListBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL)

    private lateinit var adapter: SearchAdapter

    private val yandexMapManager by lazy { YandexMapManager(requireContext()) }

    //////////////////////////////////////////////////////////////////////
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as SearchComponentProvider)
            .getSearchComponent()
            .injectCityListFragment(this)
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
        viewModel.send(GetSavedCityList)
        observeViewModel()
        configureRecyclerView()
        setOnClickListeners()
        initMap()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        MapKitFactory.getInstance().onStop()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is CityList -> {
                        adapter.submitList(it.cityList)
                        tvStateFragment.visibility = View.GONE
                        rvCityList.visibility = View.VISIBLE
                        mapContainer.visibility = View.GONE
                        bMap.text = getString(R.string.open_map)
                        bDefineLoc.text = getString(R.string.locate)
                    }

                    is EmptyList -> {
                        tvStateFragment.visibility = View.VISIBLE
                        rvCityList.visibility = View.GONE
                        mapContainer.visibility = View.GONE
                        bMap.text = getString(R.string.open_map)
                        bDefineLoc.text = getString(R.string.locate)
                    }

                    is OpenedMap -> {
                        rvCityList.visibility = View.GONE
                        mapContainer.visibility = View.VISIBLE
                        bMap.text = getString(R.string.close_map)
                        bDefineLoc.text = getString(R.string.locate)
                    }

                    is Locating -> {
                        bDefineLoc.text = getString(R.string.locating)
                    }
                }
            }
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
        val controller = findNavController()

        with(adapter) {

            onItemClickListener = {
                setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to it.name.toString()))
                controller.navigateUp()
            }

            onButtonDeleteClickListener = {
                val searchModel = it
                viewModel.send(DeleteSearchItem(it))
                Snackbar.make(
                    requireView(),
                    getString(R.string.data_has_been_deleted), Snackbar.LENGTH_LONG
                )
                    .setAction(getString(R.string.undo)) {
                        viewModel.send(AddSearchItem(searchModel))
                    }
                    .show()
            }
        }
        with(binding) {

            bInputLocation.setOnClickListener {
                controller.navigate(R.id.action_cityListFragment_to_searchFragment)
            }

            bBack.setOnClickListener {
                controller.navigateUp()
            }

            bDefineLoc.setOnClickListener {
                viewModel.send(
                    UseGps(
                        fusedLocationClient = fusedLocationClient,
                        isMapGone = binding.mapContainer.isGone,
                        yandexMapManager = yandexMapManager,
                        map = map
                    )
                )
            }

            bMap.setOnClickListener {
                if (mapContainer.isGone) {
                    viewModel.send(OpenMap)
                } else {
                    viewModel.send(GetSavedCityList)
                }
            }

            bSavePoint.setOnClickListener {
                val latLonString =
                    "${map.cameraPosition.target.latitude}, ${map.cameraPosition.target.longitude}"
                viewModel.send(RequestSearchLocation(latLonString))
            }

            bZoomIn.setOnClickListener {
                yandexMapManager.changeZoomByStep(ZOOM_STEP, map)
            }

            bZoomOut.setOnClickListener {
                yandexMapManager.changeZoomByStep(-ZOOM_STEP, map)
            }

            bCurrentLoc.setOnClickListener {
                viewModel.send(
                    UseGps(
                        fusedLocationClient = fusedLocationClient,
                        isMapGone = binding.mapContainer.isGone,
                        yandexMapManager = yandexMapManager,
                        map = map
                    )
                )
            }
        }
    }

    private fun initMap() {
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
        yandexMapManager.configureMap(binding.mapview.mapWindow.map)
    }

    companion object {
        private const val ZOOM_STEP = 1f
    }
}