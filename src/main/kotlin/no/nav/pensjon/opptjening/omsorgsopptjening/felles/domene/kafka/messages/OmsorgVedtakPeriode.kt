package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.Periode
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.RådataFraKilde
import java.time.YearMonth


data class OmsorgsArbeidKey(val omsorgsyter: String, val omsorgsType: Omsorgstype)



data class OmsorgsGrunnlag(
    val omsorgsyter: String,
    val omsorgstype: Omsorgstype,
    val kjoreHash: String,
    val kilde: Kilde,
    val omsorgsSaker: List<OmsorgsSak>,
    val rådata: RådataFraKilde,
) {
    fun hentPersoner(): Set<String> {
        return omsorgsSaker.map { it.omsorgsyter }.toSet() + omsorgsSaker.flatMap { it.omsorgVedtakPeriode }.map { it.omsorgsmottaker }.toSet()
    }
}

enum class Omsorgstype {
    BARNETRYGD,
    HJELPESTØNAD_SATS_3,
    HJELPESTØNAD_SATS_4
}

enum class Kilde {
    BARNETRYGD, INFOTRYGD
}

data class OmsorgsSak(
    val omsorgsyter: String,
    val omsorgVedtakPeriode: List<OmsorgVedtakPeriode>
)

data class OmsorgVedtakPeriode(
    val fom: YearMonth,
    val tom: YearMonth,
    val prosent: Int,
    val omsorgsmottaker: String
){
    val periode = Periode(fom, tom)
}

enum class Landstilknytning {
    EØS,
    NASJONAL
}