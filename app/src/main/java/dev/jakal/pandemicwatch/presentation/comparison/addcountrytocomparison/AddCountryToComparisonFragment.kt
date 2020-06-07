package dev.jakal.pandemicwatch.presentation.comparison.addcountrytocomparison

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.databinding.FragmentAddCountryToComparisonBinding
import dev.jakal.pandemicwatch.databinding.FragmentCountryListBinding
import dev.jakal.pandemicwatch.presentation.common.KeyboardHelper
import dev.jakal.pandemicwatch.presentation.common.adapter.CountriesAdapter
import dev.jakal.pandemicwatch.presentation.common.adapter.SpacingItemDecoration
import dev.jakal.pandemicwatch.presentation.comparison.ComparisonViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddCountryToComparisonFragment : Fragment() {

    private val viewModel: ComparisonViewModel by sharedViewModel()
    private val binding get() = _binding!!
    private var _binding: FragmentAddCountryToComparisonBinding? = null
    private lateinit var adapter: CountriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCountryToComparisonBinding.inflate(inflater, container, false)
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
        setupRecyclerView()
        observeCountries()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_country_to_comparison_menu, menu)
        (menu.findItem(R.id.menuSearch).actionView as SearchView).apply {
            queryHint = getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    KeyboardHelper.hideKeyboardFrom(context, binding.root)
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    adapter.filter.filter(query)
                    return true
                }
            })
        }
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

        binding.rvCountries.apply {
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.spacing_medium)))
            adapter = this@AddCountryToComparisonFragment.adapter
        }
    }

    private fun observeCountries() {
        viewModel.comparison.observe(viewLifecycleOwner, Observer {
            adapter.submitCountries(it.availableCountries.toMutableList())
        })
    }
}