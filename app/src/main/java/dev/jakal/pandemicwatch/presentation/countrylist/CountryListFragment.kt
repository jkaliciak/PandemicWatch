package dev.jakal.pandemicwatch.presentation.countrylist

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.databinding.FragmentCountryListBinding
import dev.jakal.pandemicwatch.presentation.common.KeyboardHelper
import dev.jakal.pandemicwatch.presentation.common.adapter.CountriesAdapter
import dev.jakal.pandemicwatch.presentation.common.adapter.SpacingItemDecoration
import dev.jakal.pandemicwatch.presentation.countrydetails.FavoriteCountriesComparator
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CountryListFragment : Fragment() {

    private val viewModel: CountryListViewModel by viewModel { parametersOf(lifecycleScope.id) }
    private val binding get() = _binding!!
    private var _binding: FragmentCountryListBinding? = null
    private lateinit var adapter: CountriesAdapter
    private val onPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        startPostponedEnterTransition()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryListBinding.inflate(inflater, container, false)
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
        setupSwipeRefresh()
        observeCountries()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.country_list_menu, menu)
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
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.spacing_medium)))
            adapter = this@CountryListFragment.adapter
            viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(p0: View?) {
                }

                override fun onViewDetachedFromWindow(p0: View?) {
                    viewTreeObserver.removeOnPreDrawListener(onPreDrawListener)
                }
            })
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
            adapter.submitCountries(it.countries.toMutableList())
        })
    }
}