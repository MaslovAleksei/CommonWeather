package com.margarin.commonweather.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.margarin.commonweather.R
import com.margarin.commonweather.app.WeatherApp
import com.margarin.commonweather.databinding.FragmentCityListBinding
import com.margarin.commonweather.ui.adapters.SearchAdapter
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
        setupSwipeListener(binding.rvCityList)
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

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        //TODO make a well-working swipe
        val callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeFlag(ACTION_STATE_IDLE, RIGHT) or
                        makeFlag(ACTION_STATE_SWIPE, LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                if (!item.isMenuShown) {
                    viewModel.changeIsMenuShown(item)
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                val item = adapter.currentList[viewHolder.adapterPosition]
                if (!item.isMenuShown) {
                    viewModel.changeIsMenuShown(item)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
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