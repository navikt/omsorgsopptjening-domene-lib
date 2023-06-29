package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

data class OmsorgsopptjeningInnvilgetMelding(
    val omsorgsAr: Int,
    val omsorgsyter: String,
    val omsorgsmottaker: String,
    val kilde: Kilde,
    val omsorgstype: Omsorgstype
)