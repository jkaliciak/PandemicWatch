package dev.jakal.pandemicwatch.presentation.comparison.addcountrytocomparison

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import dev.jakal.pandemicwatch.databinding.FragmentAddCountryToComparisonBinding
import dev.jakal.pandemicwatch.presentation.common.adapter.CountriesAdapter
import dev.jakal.pandemicwatch.presentation.comparison.ComparisonViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddCountryToComparisonFragment : Fragment() {

    private val viewModel: ComparisonViewModel by sharedViewModel()
    private lateinit var binding: FragmentAddCountryToComparisonBinding
    private lateinit var adapter: CountriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCountryToComparisonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        setupRecyclerView()
        observeCountries()
    }

    private fun setupRecyclerView() {
        adapter =
            CountriesAdapter(
                onClickListener = { countryName: String, _: ImageView, _: TextView, _: MaterialCardView ->
                    viewModel.addCountryToComparison(countryName)
                    findNavController().popBackStack()
                },
                sortingComparator = Comparator { c1, c2 ->
                    c1.country.compareTo(c2.country)
                }
            )
        binding.rvCountries.adapter = this@AddCountryToComparisonFragment.adapter
    }

    private fun observeCountries() {
        viewModel.comparison.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.availableCountries.toMutableList())
        })
    }
}