package com.openclassrooms.realestatemanager.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.MainActivityBinding
import com.openclassrooms.realestatemanager.ui.dialog.FilterDialogFragment
import com.openclassrooms.realestatemanager.utils.Constants.REQUEST_CODE_UPDATE_LOCATION
import com.openclassrooms.realestatemanager.utils.viewBinding
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }

    private val viewModel by viewModels<RealEstateViewModel>()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private var toolbar: Toolbar? = null
    private var isCurrencyDollar = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        initUi()
    }

    private fun initUi() {
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.PropertyListFragment,
            R.id.MapFragment,
            R.id.AddFragment,
            R.id.LoanCalculatorFragment
        )
            .setOpenableLayout(binding.drawerLayout)
            .build()
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(binding.navigationView, navController)
        initDrawerLayout()
        initNavigationView()
        requestLocationPermission()
    }

    private fun initToolbar() {
        this.toolbar = binding.toolbar
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val filterDialogFragment = FilterDialogFragment()
                filterDialogFragment.show(supportFragmentManager, "FilterDialogFragment")
                true
            }
            R.id.action_currency -> {
                isCurrencyDollar = !isCurrencyDollar
                viewModel.setCurrency(isCurrencyDollar)
                if (isCurrencyDollar) {
                    item.setIcon(R.drawable.ic_baseline_euro_24)
                } else {
                    item.setIcon(R.drawable.ic_baseline_attach_money_24)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initDrawerLayout() {
        drawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initNavigationView() {
        val navView = binding.navigationView
        val currentDestination = navController.currentDestination?.id
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_map -> {
                    if (currentDestination != R.id.MapFragment) {
                        navController.navigate(R.id.action_global_to_MapFragment)
                    }
                    true
                }
                R.id.menu_item_add_property -> {
                    if (currentDestination != R.id.AddFragment) {
                        navController.navigate(R.id.action_global_to_AddFragment)
                    }
                    true
                }
                R.id.menu_item_mortgage_calculator -> {
                    if (currentDestination != R.id.LoanCalculatorFragment) {
                        navController.navigate(R.id.action_global_to_loanCalculatorFragment)
                    }
                    true
                }
                else -> false
            }.also {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE_UPDATE_LOCATION
            )
        }
    }
}