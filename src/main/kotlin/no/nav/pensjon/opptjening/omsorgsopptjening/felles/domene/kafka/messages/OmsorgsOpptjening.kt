package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages



class OmsorgsOpptjening(
    val omsorgsAr: Int,
    val person: Person,
    val grunnlag: OmsorgsArbeid,
    val omsorgsopptjeningResultater: String,
    val invilget: Boolean
)

data class Person(val gjeldendeFnr: String)