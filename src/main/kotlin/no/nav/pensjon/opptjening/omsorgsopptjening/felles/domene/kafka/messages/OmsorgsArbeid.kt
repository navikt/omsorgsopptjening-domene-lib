package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import java.time.YearMonth


data class OmsorgsArbeidKey(val omsorgsyter: String, val omsorgsAr: Int, val omsorgsType: OmsorgsarbeidsType)

data class OmsorgsarbeidsSnapshot(
    val omsorgsyter: Person,
    val omsorgsAr: Int,
    val omsorgstype: OmsorgsarbeidsType,
    val kjoreHash: String,
    val kilde: OmsorgsarbeidsKilde,
    val omsorgsArbeidSaker: List<OmsorgsArbeidSak>
)

enum class OmsorgsarbeidsType {
    BARNETRYGD,
    HJELPESTØNAD_SATS_3,
    HJELPESTØNAD_SATS_4
}

enum class OmsorgsarbeidsKilde {
    BA, IT
}

data class OmsorgsArbeidSak(
    val omsorgsarbedUtfort: List<OmsorgsArbeid>
)

data class OmsorgsArbeid(
    val fom: YearMonth,
    val tom: YearMonth,
    val prosent: Int,
    val omsorgsyter: Person,
    val omsorgsmottaker: List<Person>
)