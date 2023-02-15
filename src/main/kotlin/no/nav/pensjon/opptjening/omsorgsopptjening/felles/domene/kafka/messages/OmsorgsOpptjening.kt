package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages


data class OmsorgsOpptjening(
    val omsorgsAr: Int,
    val person: Person,
    val grunnlag: OmsorgsarbeidsSnapshot,
    val omsorgsopptjeningResultater: String,
    val invilget: Boolean
)

data class OmsorgsOpptjeningKey(
    val omsorgsAr: Int,
    val gjeldendeFnr: String,
    val invilget: Boolean
)