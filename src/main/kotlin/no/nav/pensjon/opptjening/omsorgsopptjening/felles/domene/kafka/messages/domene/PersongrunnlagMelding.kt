package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.CorrelationId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.InnlesingId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.RådataFraKilde
import java.time.YearMonth

data class PersongrunnlagMelding(
    val omsorgsyter: String,
    val persongrunnlag: List<Persongrunnlag>,
    val rådata: RådataFraKilde,
    val innlesingId: InnlesingId,
    val correlationId: CorrelationId,
) {
    fun hentPersoner(): Set<String> {
        return hentOmsorgsytere() + hentOmsorgsmottakere()
    }

    fun hentOmsorgsytere(): Set<String> {
        return persongrunnlag.map { it.omsorgsyter }.toSet()
    }

    fun hentOmsorgsmottakere(): Set<String> {
        return persongrunnlag.flatMap { it.hentOmsorgsmottakere() }.toSet()
    }

    data class Persongrunnlag(
        val omsorgsyter: String,
        val omsorgsperioder: List<Omsorgsperiode>
    ){
        fun hentOmsorgsmottakere(): Set<String> {
            return omsorgsperioder.map { it.omsorgsmottaker }.toSet()
        }
    }

    data class Omsorgsperiode(
        val fom: YearMonth,
        val tom: YearMonth,
        val omsorgstype: Omsorgstype,
        val omsorgsmottaker: String,
        val kilde: Kilde,
        val utbetalt: Int,
        val landstilknytning: Landstilknytning
    )
}

