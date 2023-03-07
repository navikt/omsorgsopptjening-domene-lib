package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import java.time.YearMonth


data class OmsorgsArbeidKey(val omsorgsYter: String, val omsorgsAr: Int, val omsorgsType: Omsorgstype)

data class OmsorgsarbeidsSnapshot(
    val omsorgsYter: Person,
    val omsorgsAr: Int,
    val omsorgstype: Omsorgstype,
    val kjoreHash: String,
    val kilde: Kilde,
    val omsorgsArbeidSaker: List<OmsorgsArbeidSak>
)

enum class Omsorgstype {
    BARNETRYGD,
    HJELPESTØNAD_SATS_3,
    HJELPESTØNAD_SATS_4
}

enum class Kilde {
    BA, IT
}

data class OmsorgsArbeidSak(
    val omsorgsarbedUtfort: List<OmsorgsArbeid>
)

data class OmsorgsArbeid(
    val omsorgsyter: Person,
    val omsorgsArbeidsUtbetalinger: OmsorgsArbeidsUtbetalinger
)

data class OmsorgsArbeidsUtbetalinger(
    val fom: YearMonth,
    val tom: YearMonth
)