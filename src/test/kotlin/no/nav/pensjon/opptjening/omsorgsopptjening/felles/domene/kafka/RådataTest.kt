package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.skyscreamer.jsonassert.JSONAssert
import kotlin.test.Test

class RådataTest {
    @Test
    fun `serialisering og deserialisering`() {
        val rådata = Rådata(
            listOf(
                RådataFraKilde(
                    mapOf(
                        "a" to "b",
                        "aa" to "bb"
                    )
                ),
                RådataFraKilde(
                    mapOf("c" to "d")
                )
            )
        )

        val serialisert = jacksonObjectMapper().writeValueAsString(rådata)
        JSONAssert.assertEquals(
            """[{"a":"b","aa":"bb"},{"c":"d"}]""",
            serialisert,
            false,
        )

        val deserialisert = jacksonObjectMapper().readValue<Rådata>(serialisert)
        assertEquals(rådata, deserialisert)
    }
}