package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

sealed class Feilinformasjon {
    data class OverlappendeBarnetrygdperioder(
        val message: String
    ): Feilinformasjon()
    data class Overlappendehjelpestønadperioder(
        val message: String
    )
    data class UgyldigIdent(
        val message: String,
        val ident: String,
        val identRolle: IdentRolle,
    )
}