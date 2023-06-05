package no.nav.pensjon.opptjening.omsorgsopptjening.felles

import java.time.YearMonth

operator fun YearMonth.rangeTo(other: YearMonth) = YearMonthProgression(this, other)