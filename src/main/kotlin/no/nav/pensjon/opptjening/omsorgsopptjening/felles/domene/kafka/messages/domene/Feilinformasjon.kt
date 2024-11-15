package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
open class OpenFeilinformasjon

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
sealed class Feilinformasjon : OpenFeilinformasjon() {

    @JsonTypeName("OverlappendeBarnetrygdperioder")
    data class OverlappendeBarnetrygdperioder(
        val message: String
    ) : Feilinformasjon()

    @JsonTypeName("OverlappendeHjelpestønadperioder")
    data class OverlappendeHjelpestønadperioder(
        val message: String
    ) : Feilinformasjon()

    @JsonTypeName("UgyldigIdent")
    data class UgyldigIdent(
        val message: String,
        val exceptionType: String,
        val exceptionMessage: String,
        val ident: String,
        val identRolle: IdentRolle,
    ) : Feilinformasjon()
}