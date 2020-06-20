package dev.jakal.pandemicwatch.presentation.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dev.jakal.pandemicwatch.databinding.FragmentOverviewBinding
import dev.jakal.pandemicwatch.presentation.common.chart.setupChart
import dev.jakal.pandemicwatch.presentation.common.chart.toEntries
import dev.jakal.pandemicwatch.presentation.countrydetails.CountryDetailsFragmentDirections
import dev.jakal.pandemicwatch.presentation.linechart.LineChartConfig
import dev.jakal.pandemicwatch.presentation.linechart.LineDataConfig
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModel { parametersOf(lifecycleScope.id) }
    private val binding get() = _binding!!
    private var _binding: FragmentOverviewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
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
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshGlobalStats() }
        viewModel.refreshing.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
        viewModel.globalHistory.observe(viewLifecycleOwner, Observer {
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
}