package dev.jakal.pandemicwatch.presentation.countrydetails

import dev.jakal.pandemicwatch.domain.model.Country

class FavoriteCountriesComparator : Comparator<Country> {
    override fun compare(c1: Country?, c2: Country?): Int {
        return if (c1 == null || c2 == null) {
            if (c1 != null && c2 == null) {
                -1
            } else if (c1 == null && c2 != null) {
                1
            } else {
                0
            }
        } else {
            if (c1.favorite && c2.favorite) {
                c1.country.compareTo(c2.country)
            } else if (c1.favorite && !c2.favorite) {
                -1
            } else if (!c1.favorite && c2.favorite) {
                1
            } else {
                c1.country.compareTo(c2.country)
            }
        }
    }
}