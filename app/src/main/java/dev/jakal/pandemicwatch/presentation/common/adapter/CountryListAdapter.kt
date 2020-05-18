package dev.jakal.pandemicwatch.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
    private val onClickListener: (countryName: String, ivCountryFlag: ImageView, tvCountryName: TextView, cardView: MaterialCardView) -> Unit,
    private val sortingComparator: Comparator<Country>? = null
) : ListAdapter<Country, CountryViewHolder>(CountryDiffCallback()) {

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

    override fun submitList(list: MutableList<Country>?) {
        super.submitList(
            sortingComparator?.let {
                list?.sortedWith(it)
            } ?: list
        )
    }
}

class CountryViewHolder(
    private val binding: ItemCountryBinding,
    private val onClickListener: (countryName: String, ivCountryFlag: ImageView, tvCountryName: TextView, cardView: MaterialCardView) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(country: Country) {
        binding.root.setOnClickListener {
            onClickListener(
                country.country,
                binding.ivCountryFlag,
                binding.tvCountryName,
                binding.cardView
            )
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