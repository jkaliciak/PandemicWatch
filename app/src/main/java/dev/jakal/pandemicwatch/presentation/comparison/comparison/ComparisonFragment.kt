package dev.jakal.pandemicwatch.presentation.comparison.comparison

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dev.jakal.pandemicwatch.databinding.FragmentComparisonBinding
import dev.jakal.pandemicwatch.presentation.common.chart.setupChart
import dev.jakal.pandemicwatch.presentation.common.chart.toCaseLineDataConfigs
import dev.jakal.pandemicwatch.presentation.common.chart.toDeathsLineDataConfigs
import dev.jakal.pandemicwatch.presentation.common.chart.toRecoveredLineDataConfigs
import dev.jakal.pandemicwatch.presentation.comparison.ComparisonViewModel
import dev.jakal.pandemicwatch.presentation.countrydetails.CountryDetailsFragmentDirections
import dev.jakal.pandemicwatch.presentation.linechart.LineChartConfig
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ComparisonFragment : Fragment() {

    private val viewModel: ComparisonViewModel by sharedViewModel()
    private lateinit var binding: FragmentComparisonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComparisonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        setupViews()
    }

    private fun setupViews() {
        viewModel.comparison.observe(viewLifecycleOwner, Observer {
            val caseChartConfig = LineChartConfig.caseConfig()
                .copy(legendEnabled = true, fillEnabled = false)
            val caseLineConfigs = it.comparisonCountriesHistory.toCaseLineDataConfigs()
            binding.contentCasesChart.lineChart.setupChart(caseChartConfig, *caseLineConfigs)
            binding.cvCasesChart.setOnClickListener {
                findNavController().navigate(
                    CountryDetailsFragmentDirections.showLineChart(
                        caseChartConfig.copy(touchEnabled = true),
                        caseLineConfigs
                    )
                )
            }

            val deathsChartConfig = LineChartConfig.deathConfig()
                .copy(legendEnabled = true, fillEnabled = false)
            val deathsLineConfigs = it.comparisonCountriesHistory.toDeathsLineDataConfigs()
            binding.contentDeathsChart.lineChart.setupChart(deathsChartConfig, *deathsLineConfigs)
            binding.cvDeathsChart.setOnClickListener {
                findNavController().navigate(
                    CountryDetailsFragmentDirections.showLineChart(
                        deathsChartConfig.copy(touchEnabled = true),
                        deathsLineConfigs
                    )
                )
            }

            val recoveredChartConfig = LineChartConfig.recoveredConfig()
                .copy(legendEnabled = true, fillEnabled = false)
            val recoveredLineConfigs = it.comparisonCountriesHistory.toRecoveredLineDataConfigs()
            binding.contentRecoveredChart.lineChart.setupChart(
                recoveredChartConfig,
                *recoveredLineConfigs
            )
            binding.cvRecoveredChart.setOnClickListener {
                findNavController().navigate(
                    CountryDetailsFragmentDirections.showLineChart(
                        recoveredChartConfig.copy(touchEnabled = true),
                        recoveredLineConfigs
                    )
                )
            }
        })
    }
}