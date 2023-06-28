package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.Periode
import java.time.YearMonth


/*
 * Finnes barna til personen det spørres på i flere fagsaker vil det være flere elementer i listen
 * Ett element pr. fagsak barnet er knyttet til.
 * Kan være andre personer enn mor og far.
 */
data class BarnetrygdMelding(val ident: String, val list: List<BarnetrygdSak>)

data class BarnetrygdSak(
    val fagsakId: String,
    val fagsakEiersIdent: String,
    val barnetrygdPerioder: List<BarnetrygdPeriode>,
)

data class BarnetrygdPeriode(
    val personIdent: String,
    val delingsprosentYtelse: Int,
    val utbetaltPerMnd: Int,
    val stønadFom: YearMonth,
    val stønadTom: YearMonth
){
    @JsonIgnore
    val periode: Periode = Periode(stønadFom, stønadTom)
}