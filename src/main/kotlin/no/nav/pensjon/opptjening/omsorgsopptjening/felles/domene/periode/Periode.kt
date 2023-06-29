package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode

import java.time.Month
import java.time.YearMonth

data class Periode private constructor(private val months: Set<YearMonth> = setOf()) {

    constructor(fom: YearMonth, tom: YearMonth) : this((fom..tom).toSet())

    constructor() : this(setOf())

    constructor(år: Int): this(YearMonth.of(år, Month.JANUARY), YearMonth.of(år, Month.DECEMBER))

    fun antallMoneder(): Int = alleMåneder().size

    fun overlapper(vararg ar: Int): Boolean = months.map{it.year}.any { ar.contains(it) }

    infix fun begrensTilAr(ar: Int): Periode = Periode(months.filter { it.year == ar }.toSet())

    operator fun plus(other: Periode) = Periode(this.months + other.months)

    operator fun minus(other: Periode) = Periode(this.months - other.months)

    fun overlappendeMåneder(år: Int): Periode {
        return months.filter { it.year == år }
            .let {
                if(it.isEmpty()) Periode() else Periode(it.min(), it.max())
            }
    }

    fun inneholderMåned(måned: YearMonth): Boolean {
        return months.contains(måned)
    }

    fun min(): YearMonth {
        return months.min()
    }

    fun max(): YearMonth {
        return months.max()
    }

    fun alleMåneder(): Set<YearMonth>{
        return months
    }
}
