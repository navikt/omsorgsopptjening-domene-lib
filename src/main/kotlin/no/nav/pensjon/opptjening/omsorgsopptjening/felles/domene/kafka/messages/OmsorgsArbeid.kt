package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import java.time.YearMonth


data class OmsorgsArbeidKey(val sentralPerson: String, val omsorgsAr: String)

data class OmsorgsarbeidsSnapshot(
    val sentralPerson: Person,
    val omsorgsAr: String,
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
    BA,
}

data class OmsorgsArbeidSak(
    val omsorgsarbedUtfort: List<OmsorgsArbeid>
)

data class OmsorgsArbeid(
    val omsorgsyter: Person,
    val hash: String,
    val omsorgsArbeidsUtbetalinger: OmsorgsArbeidsUtbetalinger
)

data class OmsorgsArbeidsUtbetalinger(
    val fom: YearMonth,
    val tom: YearMonth
)