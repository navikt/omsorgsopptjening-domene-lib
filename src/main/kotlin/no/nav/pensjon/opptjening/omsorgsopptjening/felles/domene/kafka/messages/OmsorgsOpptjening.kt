package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages


data class OmsorgsOpptjening(
    val omsorgsAr: Int,
    val person: Person,
    val omsorgsmottakereInvilget: List<Person>,
    val grunnlag: OmsorgsarbeidsSnapshot,
    val omsorgsopptjeningResultater: String,
    val utfall: OpptjeningUtfall,
)

data class OmsorgsOpptjeningKey(
    val omsorgsAr: Int,
    val gjeldendeFnr: String,
    val utfall: OpptjeningUtfall
)

enum class OpptjeningUtfall{
    INVILGET,
    AVSLAG,
    SAKSBEHANDLING
}