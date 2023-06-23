package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

data class OmsorgsopptjeningInnvilget(
    val omsorgsAr: Int,
    val omsorgsyter: String,
    val omsorgsmottaker: String,
    val kilde: Kilde,
    val omsorgstype: Omsorgstype
)

data class OmsorgsopptjeningInnvilgetKey(
    val omsorgsAr: Int,
    val omsorgsyter: String,
)