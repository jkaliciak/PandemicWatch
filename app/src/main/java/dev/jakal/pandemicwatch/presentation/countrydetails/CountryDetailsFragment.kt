package dev.jakal.pandemicwatch.presentation.countrydetails

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.databinding.FragmentCountryDetailsBinding
import dev.jakal.pandemicwatch.presentation.common.chart.setupChart
import dev.jakal.pandemicwatch.presentation.common.chart.toEntries
import dev.jakal.pandemicwatch.presentation.common.load
import dev.jakal.pandemicwatch.presentation.linechart.LineChartConfig
import dev.jakal.pandemicwatch.presentation.linechart.LineDataConfig
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
    private val binding get() = _binding!!
    private var _binding: FragmentCountryDetailsBinding? = null
    private var addToFavoritesMenuItem: MenuItem? = null
    private var removeFromFavoritesMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postponeEnterTransition()
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel.countryHistory.observe(viewLifecycleOwner, Observer {
            val caseChartConfig = LineChartConfig.caseConfig()
            val caseLineConfig = LineDataConfig.caseConfig(
                entries = it.casesHistory.toEntries()
            )
            binding.contentCasesChart.lineChart.setupChart(caseChartConfig, caseLineConfig)
            binding.cvCasesChart.setOnClickListener {
                findNavController().navigate(
                    CountryDetailsFragmentDirections.showLineChart(
                        caseChartConfig.copy(touchEnabled = true),
                        arrayOf(caseLineConfig)
                    )
                )
            }

            val deathsChartConfig = LineChartConfig.deathConfig()
            val deathsLineConfig = LineDataConfig.deathConfig(
                entries = it.deathsHistory.toEntries()
            )
            binding.contentDeathsChart.lineChart.setupChart(deathsChartConfig, deathsLineConfig)
            binding.cvDeathsChart.setOnClickListener {
                findNavController().navigate(
                    CountryDetailsFragmentDirections.showLineChart(
                        deathsChartConfig.copy(touchEnabled = true),
                        arrayOf(deathsLineConfig)
                    )
                )
            }

            val recoveredChartConfig = LineChartConfig.recoveredConfig()
            val recoveredLineConfig = LineDataConfig.recoveredConfig(
                entries = it.recoveredHistory.toEntries()
            )
            binding.contentRecoveredChart.lineChart.setupChart(
                recoveredChartConfig,
                recoveredLineConfig
            )
            binding.cvRecoveredChart.setOnClickListener {
                findNavController().navigate(
                    CountryDetailsFragmentDirections.showLineChart(
                        recoveredChartConfig.copy(touchEnabled = true),
                        arrayOf(recoveredLineConfig)
                    )
                )
            }
        })
    }

    private fun updateMenu(favorite: Boolean) {
        removeFromFavoritesMenuItem?.isVisible = favorite
        addToFavoritesMenuItem?.isVisible = !favorite
    }
}