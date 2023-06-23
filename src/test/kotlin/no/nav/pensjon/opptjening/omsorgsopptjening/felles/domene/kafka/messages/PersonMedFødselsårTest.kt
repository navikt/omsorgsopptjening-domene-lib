package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class PersonMedFødselsårTest {
    @Test
    fun alder() {
        PersonMedFødselsår(
            fnr = "01048512345",
            fodselsAr = 1985
        ).let {
            assertEquals(-1, it.alderVedUtløpAv(1984))
            assertEquals(0, it.alderVedUtløpAv(1985))
            assertEquals(1, it.alderVedUtløpAv(1986))
        }
    }

    @Test
    fun erFødt() {
        PersonMedFødselsår(
            fnr = "01048512345",
            fodselsAr = 1985
        ).let {
            assertEquals(false, it.erFødt(1984))
            assertEquals(true, it.erFødt(1985))
            assertEquals(false, it.erFødt(1986))

            assertEquals(false, it.erFødt(1985, Month.MARCH))
            assertEquals(true, it.erFødt(1985, Month.APRIL))
            assertEquals(false, it.erFødt(1985, Month.MAY))
        }
    }

    @Test
    fun fødselsdato() {
        PersonMedFødselsår(
            fnr = "01048512345",
            fodselsAr = 1985
        ).let {
            assertEquals(LocalDate.of(1985, Month.APRIL, 1), it.fødselsdato())
        }
    }
}