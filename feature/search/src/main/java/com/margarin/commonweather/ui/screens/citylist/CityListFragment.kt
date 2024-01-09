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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
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
        viewModel.send(CityListEvent.GetSavedCityList)
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
        lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                binding.apply {
                    when (it) {
                        is CityListState.Content -> {
                            adapter.submitList(it.cityList)
                            tvStateFragment.visibility = View.GONE
                            rvCityList.visibility = View.VISIBLE
                            mapContainer.visibility = View.GONE
                            bMap.text = getString(R.string.open_map)
                            bDefineLoc.text = getString(R.string.locate)
                        }

                        is CityListState.EmptyList -> {
                            tvStateFragment.visibility = View.VISIBLE
                            rvCityList.visibility = View.GONE
                            mapContainer.visibility = View.GONE
                            bMap.text = getString(R.string.open_map)
                            bDefineLoc.text = getString(R.string.locate)
                        }

                        is CityListState.OpenedMap -> {
                            tvStateFragment.visibility = View.GONE
                            rvCityList.visibility = View.GONE
                            mapContainer.visibility = View.VISIBLE
                            bMap.text = getString(R.string.close_map)
                            bDefineLoc.text = getString(R.string.locate)
                        }

                        is CityListState.Locating -> {
                            bDefineLoc.text = getString(R.string.locating)
                        }
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
                viewModel.send(CityListEvent.DeleteSearchItem(it))
                Snackbar.make(
                    requireView(),
                    getString(R.string.data_has_been_deleted), Snackbar.LENGTH_LONG
                )
                    .setAction(getString(R.string.undo)) {
                        viewModel.send(CityListEvent.AddSearchItem(searchModel))
                    }.show()
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
                    CityListEvent.UseGps(
                        fusedLocationClient = fusedLocationClient,
                        isMapGone = binding.mapContainer.isGone,
                        yandexMapManager = yandexMapManager,
                        map = map
                    )
                )
            }
            bMap.setOnClickListener {
                if (mapContainer.isGone) {
                    viewModel.send(CityListEvent.OpenMap)
                } else {
                    viewModel.send(CityListEvent.GetSavedCityList)
                }
            }
            bSavePoint.setOnClickListener {
                val latLonString =
                    "${map.cameraPosition.target.latitude}, ${map.cameraPosition.target.longitude}"
                viewModel.send(CityListEvent.RequestSearchLocation(latLonString))
            }
            bZoomIn.setOnClickListener {
                yandexMapManager.changeZoomByStep(ZOOM_STEP, map)
            }
            bZoomOut.setOnClickListener {
                yandexMapManager.changeZoomByStep(-ZOOM_STEP, map)
            }
            bCurrentLoc.setOnClickListener {
                viewModel.send(
                    CityListEvent.UseGps(
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