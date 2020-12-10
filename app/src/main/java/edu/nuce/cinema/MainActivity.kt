package edu.nuce.cinema

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.nuce.base.delegates.viewBinding
import edu.nuce.base.extensions.gone
import edu.nuce.base.extensions.visible
import edu.nuce.cinema.databinding.ActivityMainBinding
import edu.nuce.cinema.ui.base.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main), NavController.OnDestinationChangedListener {

    private var navController: NavController? = null

    private val binding: ActivityMainBinding by viewBinding(R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController(R.id.nav_host_fragment)
        navController?.addOnDestinationChangedListener(this)
        binding.bottomNav.setupWithNavController(navController!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        navController = null
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.splash -> {
                binding.bottomNav.gone()
            }
            R.id.manga->{
                binding.bottomNav.gone()
            }
            else -> {
                binding.bottomNav.visible()
            }

        }
    }
}