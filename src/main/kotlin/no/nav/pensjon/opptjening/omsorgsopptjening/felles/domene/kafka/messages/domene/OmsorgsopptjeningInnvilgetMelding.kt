package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

data class OmsorgsopptjeningInnvilget(
    val omsorgsAr: Int,
    val omsorgsyter: String,
    val omsorgsmottaker: String,
    val kilde: Kilde,
    val omsorgstype: Omsorgstype
) {
    data class KafkaKey(
        val omsorgsAr: Int,
        val omsorgsyter: String,
    )
}