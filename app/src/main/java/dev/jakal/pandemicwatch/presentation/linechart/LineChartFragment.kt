package dev.jakal.pandemicwatch.presentation.linechart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import dev.jakal.pandemicwatch.databinding.FragmentCountryListBinding
import dev.jakal.pandemicwatch.databinding.FragmentLineChartBinding
import dev.jakal.pandemicwatch.presentation.common.chart.setupChart
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LineChartFragment : Fragment() {

    private val args: LineChartFragmentArgs by navArgs()
    private val viewModel: LineChartViewModel by viewModel {
        parametersOf(
            lifecycleScope.id,
            args.lineChartConfig,
            args.lineDataConfig
        )
    }
    private val binding get() = _binding!!
    private var _binding: FragmentLineChartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLineChartBinding.inflate(inflater, container, false)
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
        observeLineChartData()
    }

    private fun observeLineChartData() {
        viewModel.lineChart.observe(viewLifecycleOwner, Observer {
            binding.lineChart.setupChart(it.chartConfig, *it.lineConfigs)
        })
    }
}