package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import java.time.YearMonth


data class OmsorgsArbeidKey(val omsorgsyter: String, val omsorgsAr: Int, val omsorgsType: Omsorgstype)

data class OmsorgsarbeidSnapshot(
    val omsorgsyter: Person,
    val omsorgsAr: Int,
    val omsorgstype: Omsorgstype,
    val kjoreHash: String,
    val kilde: Kilde,
    val omsorgsarbeidSaker: List<OmsorgsarbeidSak>
) {
    fun hentPersoner(): Set<Person> {
        return (
                omsorgsarbeidSaker.flatMap { sak ->
                    sak.omsorgsarbeidPerioder.flatMap { arbeid -> arbeid.omsorgsmottakere } + sak.omsorgsarbeidPerioder.flatMap { arbeid -> arbeid.omsorgsytere }
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

data class OmsorgsarbeidSak(
    val omsorgsarbeidPerioder: List<OmsorgsarbeidPeriode>
)

data class OmsorgsarbeidPeriode(
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