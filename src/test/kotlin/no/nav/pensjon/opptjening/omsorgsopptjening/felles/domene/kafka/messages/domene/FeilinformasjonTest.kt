package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.deserializeList
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.serializeList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert

class FeilinformasjonTest {

    @Test
    fun `kan serialisere og deserialisere`() {
        val obt = Feilinformasjon.OverlappendeBarnetrygdperioder(
            "bt",
            exceptionType = "et",
            exceptionMessage = "em",
            perioder = emptyList(),
        )
        val ohs = Feilinformasjon.OverlappendeHjelpestønadperioder(
            "hs",
            exceptionType = "et",
            exceptionMessage = "em",
            perioder = emptyList(),
        )
        val fidg = Feilinformasjon.FeilIDataGrunnlag(
            "fidg",
            exceptionType = "et",
            exceptionMessage = "em",
        )
        val ui = Feilinformasjon.UgyldigIdent(
            message = "alterum",
            exceptionType = "gloriatur",
            exceptionMessage = "hinc",
            ident = "cu",
            identRolle = IdentRolle.OMSORGSMOTTAKER_BARNETRYGD
        )

        val feilinformasjon = listOf(obt, ohs, ui, fidg)

        val expected = """
            [{"type":"OverlappendeBarnetrygdperioder","message":"bt","exceptionType":"et","exceptionMessage":"em","perioder":[]},{"type":"OverlappendeHjelpestønadperioder","message":"hs","exceptionType":"et","exceptionMessage":"em","perioder":[]},{"type":"UgyldigIdent","message":"alterum","exceptionType":"gloriatur","exceptionMessage":"hinc","ident":"cu","identRolle":"OMSORGSMOTTAKER_BARNETRYGD"},{"type":"FeilIDatagrunnlag","message":"fidg","exceptionType":"et","exceptionMessage":"em"}]
        """.trimIndent()

        val serialized = feilinformasjon.serializeList()

        JSONAssert.assertEquals(expected, serialized, true)

        val deserialized = serialized.deserializeList<Feilinformasjon>()

        assertThat(deserialized[0]).isEqualTo(obt)
        assertThat(deserialized[1]).isEqualTo(ohs)
        assertThat(deserialized[2]).isEqualTo(ui)
        assertThat(deserialized[3]).isEqualTo(fidg)
    }
}