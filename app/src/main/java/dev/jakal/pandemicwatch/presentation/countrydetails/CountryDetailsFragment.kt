package dev.jakal.pandemicwatch.presentation.countrydetails

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.databinding.FragmentCountryDetailsBinding
import dev.jakal.pandemicwatch.presentation.common.chart.setupChart
import dev.jakal.pandemicwatch.presentation.common.chart.toEntries
import dev.jakal.pandemicwatch.presentation.common.load
import dev.jakal.pandemicwatch.presentation.linechart.LineChartConfig
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CountryDetailsFragment : Fragment() {

    private val args: CountryDetailsFragmentArgs by navArgs()
    private val viewModel: CountryDetailsViewModel by viewModel {
        parametersOf(
            lifecycleScope.id,
            args.countryName
        )
    }
    private lateinit var binding: FragmentCountryDetailsBinding
    private var addToFavoritesMenuItem: MenuItem? = null
    private var removeFromFavoritesMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postponeEnterTransition()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.executePendingBindings()
        setupSwipeRefresh()
        observeCountry()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.country_details_menu, menu)
        addToFavoritesMenuItem = menu.findItem(R.id.menuAddToFavorites)
        removeFromFavoritesMenuItem = menu.findItem(R.id.menuRemoveFromFavorites)
        viewModel.country.value?.favorite?.let { updateMenu(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuAddToFavorites -> viewModel.addToFavorites()
            R.id.menuRemoveFromFavorites -> viewModel.removeFromFavorites()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshCountry() }
        viewModel.refreshing.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
    }

    private fun observeCountry() {
        viewModel.country.observe(viewLifecycleOwner, Observer {
            binding.contentCountryInfo.ivCountryFlag.load(it.flagUrl) {
                this@CountryDetailsFragment.startPostponedEnterTransition()
            }
            updateMenu(it.favorite)
        })
        viewModel.countryHistorical.observe(viewLifecycleOwner, Observer {
            LineChartConfig.caseConfig().copy(entries = it.casesHistory.toEntries())
                .also { config ->
                    binding.contentCasesChart.lineChart.setupChart(config)
                    binding.cvCasesChart.setOnClickListener {
                        findNavController().navigate(
                            CountryDetailsFragmentDirections.showLineChart(
                                config.copy(touchEnabled = true)
                            )
                        )
                    }
                }
            LineChartConfig.deathConfig().copy(entries = it.deathsHistory.toEntries())
                .also { config ->
                    binding.contentDeathsChart.lineChart.setupChart(config)
                    binding.cvDeathsChart.setOnClickListener {
                        findNavController().navigate(
                            CountryDetailsFragmentDirections.showLineChart(
                                config.copy(touchEnabled = true)
                            )
                        )
                    }
                }

            LineChartConfig.recoveredConfig().copy(entries = it.recoveredHistory.toEntries())
                .also { config ->
                    binding.contentRecoveredChart.lineChart.setupChart(config)
                    binding.cvRecoveredChart.setOnClickListener {
                        findNavController().navigate(
                            CountryDetailsFragmentDirections.showLineChart(
                                config.copy(touchEnabled = true)
                            )
                        )
                    }
                }
        })
    }

    private fun updateMenu(favorite: Boolean) {
        removeFromFavoritesMenuItem?.isVisible = favorite
        addToFavoritesMenuItem?.isVisible = !favorite
    }
}