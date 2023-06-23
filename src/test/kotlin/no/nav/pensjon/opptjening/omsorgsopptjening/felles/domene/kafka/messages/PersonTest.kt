package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PersonTest {

    @Test
    fun `Gitt en person med lik fnr så skal equals være true`() {
        assertEquals(PersonMedFødselsår(fnr = "12345678910", fodselsAr = 1956), PersonMedFødselsår("12345678910", 1956))
    }

    @Test
    fun `Gitt samme person objekt så skal equals være true`() {
        val person1 = PersonMedFødselsår(fnr = "12345678910", fodselsAr = 1956)
        assertEquals(person1, person1)
    }

    @Test
    fun `Gitt en person med samme fødsels nummer men annet fødelsdato enn person 2 så skal equals være true`() {
        assertEquals(PersonMedFødselsår("12345678910", 1956), PersonMedFødselsår("12345678910", 1957))
    }

    @Test
    fun `Gitt en person med annet fødsels nummer enn person 2 så skal equals være false`() {
        assertNotEquals(PersonMedFødselsår("12345678910", 1956), PersonMedFødselsår("12345678911", 1956))
    }

    @Test
    fun `Gitt en person equals null skal equals være false`() {
        assertNotEquals(PersonMedFødselsår("12345678910", 1956), null)
    }

    @Test
    fun `Gitt to personer med samme fnr så skal hashcode være lik`() {
        assertEquals(PersonMedFødselsår("12345678910", 1956).hashCode(), PersonMedFødselsår("12345678910", 1956).hashCode())
    }
}