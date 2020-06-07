package dev.jakal.pandemicwatch.presentation.navigation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.databinding.ActivityNavigationBinding
import dev.jakal.pandemicwatch.presentation.common.ThemeHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.lifecycleScope

class NavigationActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.navHostFragment) }
    private lateinit var binding: ActivityNavigationBinding
    private val themeHelper: ThemeHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory(lifecycleScope)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation)
        setupToolbar()
        setupBottomNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSelectTheme) {
            themeHelper.showSelectNightModeDialog(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp() ||
            super.onSupportNavigateUp()

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
    }
}