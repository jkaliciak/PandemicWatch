package dev.jakal.pandemicwatch.presentation.countrylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import dev.jakal.pandemicwatch.databinding.FragmentCountryListBinding
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CountryListFragment : Fragment() {

    private val viewModel: CountryListViewModel by viewModel { parametersOf(lifecycleScope.id) }
    private lateinit var binding: FragmentCountryListBinding
    private lateinit var countriesAdapter: CountriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        setupRecyclerView()
        setupSwipeRefresh()
        observeCountries()
    }

    private fun setupRecyclerView() {
        countriesAdapter =
            CountriesAdapter { countryName, ivCountryFlag, tvCountryName, cardView ->
                findNavController().navigate(
                    CountryListFragmentDirections.showCountry(countryName),
                    FragmentNavigatorExtras(
                        ivCountryFlag to ivCountryFlag.transitionName,
                        tvCountryName to tvCountryName.transitionName,
                        cardView to cardView.transitionName
                    )
                )
            }
        postponeEnterTransition()
        binding.rvCountries.apply {
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = countriesAdapter
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refreshCountries() }
        viewModel.refreshing.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
    }

    private fun observeCountries() {
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            countriesAdapter.submitList(it.countries.toMutableList())
        })
    }
}