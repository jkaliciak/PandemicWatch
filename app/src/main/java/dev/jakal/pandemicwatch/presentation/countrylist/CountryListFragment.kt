package dev.jakal.pandemicwatch.presentation.countrylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import dev.jakal.pandemicwatch.databinding.FragmentCountryListBinding
import dev.jakal.pandemicwatch.presentation.common.adapter.CountriesAdapter
import dev.jakal.pandemicwatch.presentation.countrydetails.FavoriteCountriesComparator
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CountryListFragment : Fragment() {

    private val viewModel: CountryListViewModel by viewModel { parametersOf(lifecycleScope.id) }
    private lateinit var binding: FragmentCountryListBinding
    private lateinit var adapter: CountriesAdapter

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
        adapter = CountriesAdapter(
            onClickListener = { countryName: String, ivCountryFlag: ImageView, tvCountryName: TextView, cardView: MaterialCardView ->
                findNavController().navigate(
                    CountryListFragmentDirections.showCountry(countryName),
                    FragmentNavigatorExtras(
                        ivCountryFlag to ivCountryFlag.transitionName,
                        tvCountryName to tvCountryName.transitionName,
                        cardView to cardView.transitionName
                    )
                )
            },
            sortingComparator = FavoriteCountriesComparator()
        )
        postponeEnterTransition()
        binding.rvCountries.apply {
            adapter = this@CountryListFragment.adapter
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
            adapter.submitList(it.countries.toMutableList())
        })
    }
}