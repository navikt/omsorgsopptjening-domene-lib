package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class RådataTest {
    @Test
    fun `serialisering og deserialisering`() {
        val rådata = Rådata().apply {
            leggTil(
                RådataFraKilde(
                    mapOf(
                        "a" to "b",
                        "aa" to "bb"
                    )
                )
            )
            leggTil(
                RådataFraKilde(
                    mapOf("c" to "d")
                )
            )
        }

        val serialisert = jacksonObjectMapper().writeValueAsString(rådata)

        assertEquals("""[{"a":"b","aa":"bb"},{"c":"d"}]""", serialisert)

        val deserialisert = jacksonObjectMapper().readValue<Rådata>(serialisert)

        assertEquals(rådata, deserialisert)
    }
}