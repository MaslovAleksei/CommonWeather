package com.margarin.commonweather.presentation.screens.citylist.legacy//package com.margarin.commonweather.presentation.screens.citylist
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import com.margarin.commonweather.BINDING_NULL
//import com.margarin.commonweather.di.SearchComponentProvider
//import com.margarin.commonweather.presentation.adapter.SearchAdapter
//import com.margarin.commonweather.ui.theme.CommonWeatherTheme
//import com.margarin.search.databinding.FragmentCityListBinding
//
//
//class CityListFragment : Fragment() {
//
//
//    private val viewModel by lazy {
//       // ViewModelProvider(this, viewModelFactory)[CityListViewModel::class.java]
//    }
//
//    private var _binding: FragmentCityListBinding? = null
//    private val binding: FragmentCityListBinding
//        get() = _binding ?: throw RuntimeException(BINDING_NULL)
//
//    private lateinit var adapter: SearchAdapter
//
//    //////////////////////////////////////////////////////////////////////
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        (context.applicationContext as SearchComponentProvider)
//            .getSearchComponent()
//            .injectCityListFragment(this)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentCityListBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        //viewModel.sendEvent(CityListEvent.GetSavedCityList)
//        val controller = findNavController()
//        binding.composeView.setContent {
//            CommonWeatherTheme {
////                CityListScreen(
////                    viewModel = viewModel,
////                    onButtonBackClickListener = {
////                        controller.navigateUp()
////                    },
////                    onButtonSearchClickListener = {
////                        controller.navigate(R.id.action_cityListFragment_to_searchFragment)
////                    },
////                    onCityItemClickListener = {
////                        setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to it.name.toString()))
////                        controller.navigateUp()
////                    }
////                )
//            }
//        }
////        observeViewModel()
////        configureRecyclerView()
////        setOnClickListeners()
////        initMap()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
////        MapKitFactory.getInstance().onStop()
//    }
//
////    private fun observeViewModel() {
////        lifecycleScope.launch {
////            viewModel.state
////                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
////                .collect {
////                    binding.apply {
////                        when (it) {
////                            is CityListState.Content -> {
////                                adapter.submitList(it.cityList)
////                                tvStateFragment.visibility = View.GONE
////                                rvCityList.visibility = View.VISIBLE
////                                mapContainer.visibility = View.GONE
////                                bMap.text = getString(R.string.open_map)
////                                bDefineLoc.text = getString(R.string.locate)
////                            }
////
////                            is CityListState.EmptyList -> {
////                                tvStateFragment.visibility = View.VISIBLE
////                                rvCityList.visibility = View.GONE
////                                mapContainer.visibility = View.GONE
////                                bMap.text = getString(R.string.open_map)
////                                bDefineLoc.text = getString(R.string.locate)
////                            }
////
////                            is CityListState.Map -> {
////                                tvStateFragment.visibility = View.GONE
////                                rvCityList.visibility = View.GONE
////                                mapContainer.visibility = View.VISIBLE
////                                bMap.text = getString(R.string.close_map)
////                                bDefineLoc.text = getString(R.string.locate)
////                            }
////
////                            is CityListState.Locating -> {
////                                bDefineLoc.text = getString(R.string.locating)
////                            }
////
////                            is CityListState.Initial -> {
////
////                            }
////                        }
////                    }
////                }
////        }
////    }
////
////    private fun configureRecyclerView() {
////        binding.rvCityList.layoutManager = LinearLayoutManager(activity)
////        adapter = SearchAdapter(R.layout.city_item)
////        binding.rvCityList.adapter = adapter
////    }
////
////    private fun setOnClickListeners() {
////        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
////        val map = binding.mapview.mapWindow.map
////        val controller = findNavController()
////
////        with(adapter) {
////
////            onItemClickListener = {
////                setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to it.name.toString()))
////                controller.navigateUp()
////            }
////            onButtonDeleteClickListener = {
////                val searchModel = it
////                viewModel.sendEvent(CityListEvent.DeleteSearchItem(it))
////                Snackbar.make(
////                    requireView(),
////                    getString(R.string.data_has_been_deleted), Snackbar.LENGTH_LONG
////                )
////                    .setAction(getString(R.string.undo)) {
////                        viewModel.sendEvent(CityListEvent.AddSearchItem(searchModel))
////                    }.show()
////            }
////        }
////
////        with(binding) {
////            bInputLocation.setOnClickListener {
////                controller.navigate(R.id.action_cityListFragment_to_searchFragment)
////            }
////            bBack.setOnClickListener {
////                controller.navigateUp()
////            }
////            bDefineLoc.setOnClickListener {
////                viewModel.sendEvent(
////                    CityListEvent.UseGps(
////                        fusedLocationClient = fusedLocationClient,
////                        isMapGone = binding.mapContainer.isGone,
////                        yandexMapManager = yandexMapManager,
////                        map = map
////                    )
////                )
////            }
////            bMap.setOnClickListener {
////                if (mapContainer.isGone) {
////                    viewModel.sendEvent(CityListEvent.OpenMap)
////                } else {
////                    viewModel.sendEvent(CityListEvent.GetSavedCityList)
////                }
////            }
////            bSavePoint.setOnClickListener {
////                val latLonString =
////                    "${map.cameraPosition.target.latitude}, ${map.cameraPosition.target.longitude}"
////                viewModel.sendEvent(CityListEvent.RequestSearchLocation(latLonString))
////            }
////            bZoomIn.setOnClickListener {
////                yandexMapManager.changeZoomByStep(ZOOM_STEP, map)
////            }
////            bZoomOut.setOnClickListener {
////                yandexMapManager.changeZoomByStep(-ZOOM_STEP, map)
////            }
////            bCurrentLoc.setOnClickListener {
////                viewModel.sendEvent(
////                    CityListEvent.UseGps(
////                        fusedLocationClient = fusedLocationClient,
////                        isMapGone = binding.mapContainer.isGone,
////                        yandexMapManager = yandexMapManager,
////                        map = map
////                    )
////                )
////            }
////        }
////    }
//
////    private fun initMap() {
////        MapKitFactory.getInstance().onStart()
////        binding.mapview.onStart()
////        yandexMapManager.configureMap(binding.mapview.mapWindow.map)
////    }
//
//    companion object {
//        private const val ZOOM_STEP = 1f
//    }
//}