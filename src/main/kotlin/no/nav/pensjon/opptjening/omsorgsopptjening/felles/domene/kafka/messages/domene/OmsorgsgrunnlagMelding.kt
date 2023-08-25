package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.CorrelationId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.InnlesingId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.RådataFraKilde
import java.time.YearMonth

data class OmsorgsgrunnlagMelding(
    val omsorgsyter: String,
    val omsorgstype: Omsorgstype,
    val kilde: Kilde,
    val saker: List<Sak>,
    val rådata: RådataFraKilde,
    val innlesingId: InnlesingId,
    val correlationId: CorrelationId,
) {
    fun hentPersoner(): Set<String> {
        return saker.map { it.omsorgsyter }.toSet() + saker.flatMap { it.vedtaksperioder }.map { it.omsorgsmottaker }
            .toSet()
    }

    data class Sak(
        val omsorgsyter: String,
        val vedtaksperioder: List<VedtakPeriode>
    )

    data class VedtakPeriode(
        val fom: YearMonth,
        val tom: YearMonth,
        val prosent: Int,
        val omsorgsmottaker: String
    ) {
        val periode = Periode(fom, tom)
    }
}

