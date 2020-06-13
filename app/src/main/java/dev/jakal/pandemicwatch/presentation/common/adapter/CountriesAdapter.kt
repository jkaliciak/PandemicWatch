package dev.jakal.pandemicwatch.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dev.jakal.pandemicwatch.databinding.ItemCountryBinding
import dev.jakal.pandemicwatch.domain.model.Country
import dev.jakal.pandemicwatch.presentation.common.load

class CountriesAdapter(
    private val onClickListener: ((countryName: String, ivCountryFlag: ImageView, tvCountryName: TextView, cardView: MaterialCardView) -> Unit)?,
    private val sortingComparator: Comparator<Country>? = null
) : ListAdapter<Country, CountryViewHolder>(CountryDiffCallback()), Filterable {

    private var unfilteredCountries: List<Country>? = null
    private val filter = object : Filter() {
        override fun performFiltering(query: CharSequence?) = FilterResults().apply {
            values = if (!query.isNullOrEmpty()) {
                unfilteredCountries?.let { countries ->
                    val filteredList = countries.filter { country ->
                        country.country.contains(other = query, ignoreCase = true)
                    }
                    filteredList.sortWithSortingComparator()
                }
            } else unfilteredCountries
        }

        override fun publishResults(query: CharSequence?, results: FilterResults?) {
            submitList((results?.values as List<Country>).toMutableList())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder =
        CountryViewHolder(
            ItemCountryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onClickListener
        )

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getFilter(): Filter = filter

    fun submitCountries(list: MutableList<Country>?) {
        unfilteredCountries = list
        submitList(list.sortWithSortingComparator())
    }

    private fun List<Country>?.sortWithSortingComparator() = this?.let {
        sortingComparator?.let { sortedWith(it) } ?: this
    } ?: this
}

class CountryViewHolder(
    private val binding: ItemCountryBinding,
    private val onClickListener: ((countryName: String, ivCountryFlag: ImageView, tvCountryName: TextView, cardView: MaterialCardView) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(country: Country) {
        onClickListener?.let { listener ->
            binding.root.setOnClickListener {
                listener(
                    country.country,
                    binding.ivCountryFlag,
                    binding.tvCountryName,
                    binding.cardView
                )
            }
        }
        binding.country = country
        binding.ivCountryFlag.load(country.countryInfo.flagUrl)
        binding.executePendingBindings()
    }
}

internal class CountryDiffCallback() : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean =
        oldItem.country == newItem.country

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean =
        oldItem == newItem
}