package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode

import java.time.YearMonth

class YearMonthProgression(override val start: YearMonth, override val endInclusive: YearMonth) : Iterable<YearMonth>, ClosedRange<YearMonth> {

    override fun iterator(): Iterator<YearMonth> = YearMonthIterator(start, endInclusive)

}


