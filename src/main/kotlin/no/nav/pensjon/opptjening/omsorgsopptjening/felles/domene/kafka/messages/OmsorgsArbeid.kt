package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import java.time.YearMonth


data class OmsorgsArbeid(val omsorgsyter: Omsorgsyter, val omsorgsAr: String, val hash: String)
data class Omsorgsyter(val fnr: String, val utbetalingsperioder: List<UtbetalingsPeriode>)
data class UtbetalingsPeriode(val fom: YearMonth, val tom: YearMonth, val omsorgsmottaker: OmsorgsMottaker)
data class OmsorgsMottaker(val fnr: String)
data class OmsorgsArbeidKey(val omsorgsyterFnr: String, val omsorgsAr: String)