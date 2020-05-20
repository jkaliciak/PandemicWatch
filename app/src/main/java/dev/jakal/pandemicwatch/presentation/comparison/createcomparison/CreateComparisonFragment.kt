package dev.jakal.pandemicwatch.presentation.comparison.createcomparison

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.jakal.pandemicwatch.R
import dev.jakal.pandemicwatch.databinding.FragmentCreateComparisonBinding
import dev.jakal.pandemicwatch.presentation.common.adapter.CountriesAdapter
import dev.jakal.pandemicwatch.presentation.common.adapter.SpacingItemDecoration
import dev.jakal.pandemicwatch.presentation.common.adapter.setupSwipeDelete
import dev.jakal.pandemicwatch.presentation.comparison.ComparisonViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreateComparisonFragment : Fragment() {

    private val viewModel: ComparisonViewModel by sharedViewModel()
    private lateinit var binding: FragmentCreateComparisonBinding
    private lateinit var adapter: CountriesAdapter
    private var addToComparisonMenuItem: MenuItem? = null
    private var resetComparison: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateComparisonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        setupViews()
        observeComparison()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.create_comparison_menu, menu)
        addToComparisonMenuItem = menu.findItem(R.id.menuAddToComparison)
        resetComparison = menu.findItem(R.id.menuResetComparison)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuAddToComparison -> startAddCountryToComparisonScreen()
            R.id.menuResetComparison -> showResetComparisonDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        adapter = CountriesAdapter(
            onClickListener = { _: String, _: ImageView, _: TextView, _: MaterialCardView ->
                // nothing
            },
            sortingComparator = Comparator { c1, c2 ->
                c1.country.compareTo(c2.country)
            }
        )

        binding.rvCountries.apply {
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.spacing_medium)))
            adapter = this@CreateComparisonFragment.adapter
            setupSwipeDelete(this@CreateComparisonFragment.adapter) { deletedCountryName ->
                viewModel.removeCountryFromComparison(deletedCountryName)
            }
        }
        binding.fabAdd.setOnClickListener { startAddCountryToComparisonScreen() }
        binding.fabCompare.setOnClickListener {
            findNavController().navigate(
                CreateComparisonFragmentDirections.showComparison()
            )
        }
    }

    private fun observeComparison() {
        viewModel.comparison.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.comparisonCountries.toMutableList())
            updateMenu(it.comparisonCountries.size)
        })
    }

    private fun startAddCountryToComparisonScreen() {
        findNavController().navigate(
            CreateComparisonFragmentDirections.showAddCountryToComparison()
        )
    }

    private fun showResetComparisonDialog() {
        MaterialAlertDialogBuilder(context)
            .setMessage(R.string.create_comparison_reset_dialog_message)
            .setPositiveButton(R.string.alert_dialog_positive_button) { _, _ ->
                viewModel.resetComparisonCountries()
            }.setNegativeButton(R.string.alert_dialog_negative_button, null)
            .show()
    }

    private fun updateMenu(comparisonItemsCount: Int) {
        addToComparisonMenuItem?.isVisible = comparisonItemsCount < 4
        resetComparison?.isVisible = comparisonItemsCount > 0
    }
}