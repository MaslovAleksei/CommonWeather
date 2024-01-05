package com.margarin.commonweather.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.margarin.commonweather.BINDING_NULL
import com.margarin.commonweather.BUNDLE_KEY
import com.margarin.commonweather.REQUEST_KEY
import com.margarin.commonweather.ViewModelFactory
import com.margarin.commonweather.di.SearchComponentProvider
import com.margarin.commonweather.ui.adapter.SearchAdapter
import com.margarin.commonweather.ui.viewmodels.CityListViewModel
import com.margarin.search.R
import com.margarin.search.databinding.FragmentCityListBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.Map
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
        viewModel.savedCityList?.observe(viewLifecycleOwner) {
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
        val controller = findNavController()

        with(adapter) {

            onItemClickListener = {
                viewModel.saveToDataStore(it.name.toString())
                setFragmentResult(it.name.toString())
                controller.navigateUp()
            }

            onButtonDeleteClickListener = {
                val searchModel = it
                viewModel.deleteSearchItem(it)
                Snackbar.make(requireView(),
                    getString(R.string.data_has_been_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)){
                        viewModel.addSearchItem(searchModel)
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
                interactWithLocation(fusedLocationClient, map)
            }

            bMap.setOnClickListener {
                if (mapContainer.isGone) {
                    MapKitFactory.getInstance().onStart()
                    mapview.onStart()
                    viewModel.configureMap(map)

                    mapContainer.visibility = View.VISIBLE
                    rvCityList.visibility = View.GONE
                    bMap.text = getString(R.string.close_map)

                } else {
                    MapKitFactory.getInstance().onStop()

                    mapContainer.visibility = View.GONE
                    rvCityList.visibility = View.VISIBLE
                    bMap.text = getString(R.string.open_map)
                }
            }

            bSavePoint.setOnClickListener {
                val latLonString =
                    "${map.cameraPosition.target.latitude}, ${map.cameraPosition.target.longitude}"
                viewModel.attemptSaveLocation(latLonString)

                MapKitFactory.getInstance().onStop()
                mapContainer.visibility = View.GONE
                rvCityList.visibility = View.VISIBLE
                bMap.text = getString(R.string.open_map)
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
            viewModel.getLocationLatLon(fusedLocationClient)
            viewModel.definiteLocation.observe(viewLifecycleOwner) {
                if (binding.mapContainer.isGone) {
                    viewModel.saveToDataStore(it?.first()?.name.toString())
                    setFragmentResult(it?.first()?.name.toString())
                    findNavController().popBackStack()
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
    }
}