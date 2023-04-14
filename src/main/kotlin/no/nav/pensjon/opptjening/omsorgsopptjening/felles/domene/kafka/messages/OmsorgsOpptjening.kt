package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages


data class OmsorgsOpptjening(
    val omsorgsAr: Int,
    val omsorgsyter: Person,
    val omsorgsmottakereInvilget: List<Person>,
    val grunnlag: OmsorgsarbeidsSnapshot,
    val vilkarsResultat: String,
    val utfall: OpptjeningUtfall,
)

data class OmsorgsOpptjeningKey(
    val omsorgsAr: Int,
    val omsorgsyter: String,
    val utfall: OpptjeningUtfall
)

enum class OpptjeningUtfall{
    INVILGET,
    AVSLAG,
    SAKSBEHANDLING,
    MANGLER_INFORMASJON_OM_ANNEN_OMSORGSYTER
}