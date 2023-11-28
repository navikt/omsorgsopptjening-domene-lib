package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene.KanSlåsSammen
import java.time.Month
import java.time.YearMonth

data class Periode private constructor(private val months: Set<YearMonth> = setOf()) : KanSlåsSammen<Periode> {

    init {
        require(months.sammenhengende()) { "Perioden er ikke sammenhengende" }
    }

    constructor(fom: YearMonth, tom: YearMonth) : this((fom..tom).toSet())

    constructor() : this(setOf())

    constructor(år: Int) : this(YearMonth.of(år, Month.JANUARY), YearMonth.of(år, Month.DECEMBER))

    fun antallMoneder(): Int = alleMåneder().size

    fun overlapper(vararg ar: Int): Boolean = months.map { it.year }.any { ar.contains(it) }

    infix fun begrensTilAr(ar: Int): Periode = Periode(months.filter { it.year == ar }.toSet())

    operator fun plus(other: Periode) = Periode(this.months + other.months)

    operator fun minus(other: Periode) = Periode(this.months - other.months)

    fun overlappendeMåneder(år: Int): Periode {
        return months.filter { it.year == år }
            .let {
                if (it.isEmpty()) Periode() else Periode(it.min(), it.max())
            }
    }

    fun overlapper(other: Periode): Boolean {
        return overlappendeMåneder(other.months).isNotEmpty()
    }

    fun tilstøter(other: Periode): Boolean {
        return min().minusMonths(1) == other.max() || max().plusMonths(1) == other.min()
    }

    fun overlappendeMåneder(måneder: Set<YearMonth>): Set<YearMonth> {
        return months.intersect(måneder)
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

    fun alleMåneder(): Set<YearMonth> {
        return months
    }

    companion object {
        fun Set<YearMonth>.sammenhengende(): Boolean {
            return windowed(size = 2, step = 1, partialWindows = false)
                .map { window ->
                    val første = window.first().let { Periode(it, it) }
                    val neste = window.last().let { Periode(it, it) }
                    første.tilstøter(neste)
                }
                .all { it }
        }

        fun januar(år: Int): YearMonth {
            return YearMonth.of(år, Month.JANUARY)
        }

        fun februar(år: Int): YearMonth {
            return YearMonth.of(år, Month.FEBRUARY)
        }

        fun mars(år: Int): YearMonth {
            return YearMonth.of(år, Month.MARCH)
        }

        fun april(år: Int): YearMonth {
            return YearMonth.of(år, Month.APRIL)
        }

        fun mai(år: Int): YearMonth {
            return YearMonth.of(år, Month.MAY)
        }

        fun juni(år: Int): YearMonth {
            return YearMonth.of(år, Month.JUNE)
        }

        fun juli(år: Int): YearMonth {
            return YearMonth.of(år, Month.JULY)
        }

        fun august(år: Int): YearMonth {
            return YearMonth.of(år, Month.AUGUST)
        }

        fun september(år: Int): YearMonth {
            return YearMonth.of(år, Month.SEPTEMBER)
        }

        fun oktober(år: Int): YearMonth {
            return YearMonth.of(år, Month.OCTOBER)
        }

        fun november(år: Int): YearMonth {
            return YearMonth.of(år, Month.NOVEMBER)
        }

        fun desember(år: Int): YearMonth {
            return YearMonth.of(år, Month.DECEMBER)
        }

        fun YearMonth.periode(): Periode {
            return Periode(this, this)
        }
    }

    fun periode(): Periode {
        return this
    }

    override fun slåSammen(other: Periode): Periode {
        require(kanSlåsSammen(other)) { "Perioder kan ikke slås sammen" }
        return Periode(minOf(min(), other.min()), maxOf(max(), other.max()))
    }

    override fun kanSlåsSammen(other: Periode): Boolean {
        return overlapper(other) || tilstøter(other)
    }


    override fun sorteringVedSammenslåing(): Comparator<Periode> {
        return compareBy { min() }
    }
}
