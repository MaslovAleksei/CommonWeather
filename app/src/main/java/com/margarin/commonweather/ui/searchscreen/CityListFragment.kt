package com.margarin.commonweather.ui.searchscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.margarin.commonweather.R
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentCityListBinding
import com.margarin.commonweather.ui.searchscreen.adapter.SearchAdapter
import com.margarin.commonweather.ui.ViewModelFactory
import com.margarin.commonweather.utils.BUNDLE_KEY
import com.margarin.commonweather.utils.REQUEST_KEY
import com.margarin.commonweather.utils.SEARCH_FRAGMENT
import com.margarin.commonweather.utils.launchFragment
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.Map
import kotlinx.coroutines.runBlocking
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
            viewModel.getSearchList()
            observeViewModel()
            configureRecyclerView()
            setOnClickListeners()

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
                viewModel.saveToDataStore(it.name.toString())
                setFragmentResult(it.name.toString())
                requireActivity().supportFragmentManager.popBackStack()
            }

            onButtonDeleteClickListener = {
                val searchModel = it
                viewModel.deleteSearchItem(it)
                Snackbar.make(requireView(), "delete", Snackbar.LENGTH_LONG)
                    .setAction("undo"){
                        viewModel.addSearchItem(searchModel)
                    }
                    .show()
            }
        }
        with(binding) {

            bInputLocation.setOnClickListener {
                launchFragment(SearchFragment.newInstance(), SEARCH_FRAGMENT)
            }

            bBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            bDefineLoc.setOnClickListener {
                interactWithLocation(fusedLocationClient, map)
            }

            bMap.setOnClickListener {
                if (mapContainer.isGone) {
                    MapKitFactory.getInstance().onStart()
                    mapview.onStart()
                    viewModel.configureMap(map)

                    mapContainer.visibility = View.VISIBLE
                    rvCityList.visibility = View.GONE
                    bMap.text = "Close map"

                } else {
                    MapKitFactory.getInstance().onStop()

                    mapContainer.visibility = View.GONE
                    rvCityList.visibility = View.VISIBLE
                    bMap.text = "Open map"
                }
            }

            bSavePoint.setOnClickListener {
                val latLonString =
                    "${map.cameraPosition.target.latitude}, ${map.cameraPosition.target.longitude}"
                viewModel.changeSavedLocation(latLonString)
                viewModel.savedLocation.observe(viewLifecycleOwner) {
                    if (it?.isNotEmpty() == true) {
                        viewModel.addSearchItem(it.first())
                    } else {
                        Toast.makeText(requireContext(), "No city detected", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                MapKitFactory.getInstance().onStop()
                mapContainer.visibility = View.GONE
                rvCityList.visibility = View.VISIBLE
                bMap.text = "Open map"
            }

            bZoomIn.setOnClickListener {
                viewModel.changeZoomByStep(ZOOM_STEP, map)
            }

            bZoomOut.setOnClickListener {
                viewModel.changeZoomByStep(-ZOOM_STEP, map)
            }

            bCurrentLoc.setOnClickListener {
                interactWithLocation(fusedLocationClient, map)
            }
        }
    }

    private fun setFragmentResult(name: String) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(BUNDLE_KEY to name)
        )
    }

    private fun interactWithLocation(fusedLocationClient: FusedLocationProviderClient, map: Map) {
        if (viewModel.isGpsEnabled()) {
            runBlocking {viewModel.getLocationLatLon(fusedLocationClient)}
            viewModel.definiteLocation.observe(viewLifecycleOwner) {
                if (binding.mapContainer.isGone) {
                    viewModel.saveToDataStore(it?.first()?.name.toString())
                    setFragmentResult(it?.first()?.name.toString())
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    viewModel.mapMoveToPosition(
                        map,
                        it?.first()?.lat.toString(),
                        it?.first()?.lon.toString()
                    )
                }
            }
        } else {
            requireContext().startActivity(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
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