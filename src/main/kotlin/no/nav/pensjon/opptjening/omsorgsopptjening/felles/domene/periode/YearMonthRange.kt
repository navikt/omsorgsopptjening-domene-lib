package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode

import java.time.YearMonth

operator fun YearMonth.rangeTo(other: YearMonth) = YearMonthProgression(this, other)