package com.margarin.commonweather.presentation.screens.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.margarin.commonweather.BINDING_NULL
import com.margarin.commonweather.BUNDLE_KEY
import com.margarin.commonweather.REQUEST_KEY
import com.margarin.commonweather.ViewModelFactory
import com.margarin.commonweather.di.SearchComponentProvider
import com.margarin.commonweather.presentation.screens.search.compose.SearchScreen
import com.margarin.commonweather.presentation.screens.search.compose.ui.theme.CommonWeatherTheme
import com.margarin.search.databinding.FragmentSearchBinding
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as SearchComponentProvider)
            .getSearchComponent()
            .injectSearchFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            CommonWeatherTheme {
                SearchScreen (
                    viewModel = viewModel,
                    onChipClick = {
                        switchCity(it)
                    },
                    onCardClickListener = {
                        switchCity(it)
                    },
                    onButtonAddClickListener = {
                        viewModel.sendEvent(SearchEvent.AddSearchItem(it))
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun switchCity(city: String) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(BUNDLE_KEY to city)
        )
        findNavController().popBackStack(ROUTE_WEATHER_FRAGMENT, false)
    }

    companion object {
        private const val ROUTE_WEATHER_FRAGMENT = "weatherFragment"
    }
}