package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

open class OpenFeilinformasjon

sealed class Feilinformasjon : OpenFeilinformasjon() {
    data class OverlappendeBarnetrygdperioder(
        val message: String
    ) : Feilinformasjon()

    data class Overlappendehjelpestønadperioder(
        val message: String
    ) : Feilinformasjon()

    data class UgyldigIdent(
        val message: String,
        val exceptionType: String,
        val exceptionMessage: String,
        val ident: String,
        val identRolle: IdentRolle,
    ) : Feilinformasjon()
}