package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages


data class OmsorgsOpptjening(
    val omsorgsAr: Int,
    val omsorgsyter: Person,
    val omsorgsmottakereInvilget: List<Person>,
    val grunnlag: OmsorgsGrunnlag,
    val vilkarsResultat: String,
    val utfall: Utfall,
)

data class OmsorgsOpptjeningKey(
    val omsorgsAr: Int,
    val omsorgsyter: String,
    val utfall: Utfall
)

enum class Utfall{
    INVILGET,
    AVSLAG,
    SAKSBEHANDLING,
    MANGLER_ANNEN_OMSORGSYTER
}