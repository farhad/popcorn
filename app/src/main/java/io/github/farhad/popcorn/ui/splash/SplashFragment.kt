package io.github.farhad.popcorn.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import io.github.farhad.popcorn.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()

        val navController = requireActivity().findNavController(R.id.container)

        Handler().postDelayed({
            navController.navigate(R.id.action_splashFragment_to_trendingMoviesFragment)
        }, 2500)
    }

}