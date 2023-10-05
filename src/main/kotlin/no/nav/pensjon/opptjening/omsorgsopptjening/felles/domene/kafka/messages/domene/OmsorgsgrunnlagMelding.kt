package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.CorrelationId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.InnlesingId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.RådataFraKilde
import java.time.YearMonth

data class OmsorgsgrunnlagMelding(
    val omsorgsyter: String,
    val saker: List<Sak>,
    val rådata: RådataFraKilde,
    val innlesingId: InnlesingId,
    val correlationId: CorrelationId,
) {
    fun hentPersoner(): Set<String> {
        return hentOmsorgsytere() + hentOmsorgsmottakere()
    }

    fun hentOmsorgsytere(): Set<String> {
        return saker.map { it.omsorgsyter }.toSet()
    }

    fun hentOmsorgsmottakere(): Set<String> {
        return saker.flatMap { it.hentOmsorgsmottakere() }.toSet()
    }

    data class Sak(
        val omsorgsyter: String,
        val vedtaksperioder: List<VedtakPeriode>
    ){
        fun hentOmsorgsmottakere(): Set<String> {
            return vedtaksperioder.map { it.omsorgsmottaker }.toSet()
        }

        fun leggTilVedtaksperiode(vedtakPeriode: VedtakPeriode): Sak {
            return copy(vedtaksperioder = this.vedtaksperioder + vedtakPeriode)
        }
    }

    data class VedtakPeriode(
        val fom: YearMonth,
        val tom: YearMonth,
        val omsorgstype: Omsorgstype,
        val omsorgsmottaker: String,
        val kilde: Kilde,
    )
}

