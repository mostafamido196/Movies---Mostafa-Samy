package com.samy.mostafasamy.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    val navController: NavController by lazy {
        findNavController()
    }

    val mFragmentManager: FragmentManager by lazy {
        parentFragmentManager
    }

    val mContext: Context by lazy {
        requireContext()
    }

    val mActivity: FragmentActivity by lazy {
        requireActivity()
    }

    val mView: View by lazy {
        requireView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val window: Window = mActivity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = mContext.getColor(R.color.purple_500)
*/
    }

}