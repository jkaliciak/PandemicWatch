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
import dev.jakal.pandemicwatch.presentation.linechart.LineChartConfig
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModel { parametersOf(lifecycleScope.id) }
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
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
        viewModel.globalHistorical.observe(viewLifecycleOwner, Observer {
            LineChartConfig.caseConfig().copy(entries = it.casesHistory.toEntries())
                .also { config ->
                    binding.contentCasesChart.lineChart.setupChart(config)
                    binding.cvCasesChart.setOnClickListener {
                        findNavController().navigate(
                            OverviewFragmentDirections.showLineChart(
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
                            OverviewFragmentDirections.showLineChart(
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
                            OverviewFragmentDirections.showLineChart(
                                config.copy(touchEnabled = true)
                            )
                        )
                    }
                }
        })
    }
}