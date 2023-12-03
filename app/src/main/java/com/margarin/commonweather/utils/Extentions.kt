package com.margarin.commonweather.utils

import androidx.fragment.app.Fragment
import com.margarin.commonweather.R

fun Fragment.launchFragment(fragment: Fragment, fragmentName: String){
    requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.holder, fragment)
        .addToBackStack(fragmentName)
        .commit()
}