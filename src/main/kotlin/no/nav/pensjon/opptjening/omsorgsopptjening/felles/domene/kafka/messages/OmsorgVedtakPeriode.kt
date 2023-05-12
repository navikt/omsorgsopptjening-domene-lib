package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import java.time.YearMonth


data class OmsorgsArbeidKey(val omsorgsyter: String, val omsorgsAr: Int, val omsorgsType: Omsorgstype)



data class OmsorgsGrunnlag(
    val omsorgsyter: Person,
    val omsorgsAr: Int,
    val omsorgstype: Omsorgstype,
    val kjoreHash: String,
    val kilde: Kilde,
    val omsorgsSaker: List<OmsorgsSak>
) {
    fun hentPersoner(): Set<Person> {
        return (
                omsorgsSaker.flatMap { sak ->
                    sak.omsorgVedtakPeriode.flatMap { arbeid -> arbeid.omsorgsmottakere } + sak.omsorgVedtakPeriode.flatMap { arbeid -> arbeid.omsorgsytere }
                } + omsorgsyter
                ).toSet()
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
    val omsorgVedtakPeriode: List<OmsorgVedtakPeriode>
)

data class OmsorgVedtakPeriode(
    val fom: YearMonth,
    val tom: YearMonth,
    val prosent: Int,
    val omsorgsytere: Set<Person>,
    val omsorgsmottakere: Set<Person>,
    val landstilknytning: Landstilknytning
)

enum class Landstilknytning {
    EØS,
    NASJONAL
}