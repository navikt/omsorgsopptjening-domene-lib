package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PersonTest {

    @Test
    fun `Gitt en person med lik fnr så skal equals være true`() {
        assertEquals(Person("12345678910"), Person("12345678910"))
    }

    @Test
    fun `Gitt samme person objekt så skal equals være true`() {
        val person1 = Person("12345678910")
        assertEquals(person1, person1)
    }

    @Test
    fun `Gitt en person med samme fødsels nummer men annet fødelsdato enn person 2 så skal equals være true`() {
        assertEquals(Person("12345678910", "2020"), Person("12345678910", "2021"))
    }

    @Test
    fun `Gitt en person med annet fødsels nummer enn person 2 så skal equals være false`() {
        assertNotEquals(Person("12345678910", "2020"), Person("12345678911", "2021"))
    }

    @Test
    fun `Gitt en person equals null skal equals være false`() {
        assertNotEquals(Person("12345678910", "2020"), null)
    }

    @Test
    fun `Gitt to personer med samme fnr så skal hashcode være lik`() {
        assertEquals(Person("12345678910", "2020").hashCode(), Person("12345678910", "2022").hashCode())
    }
}