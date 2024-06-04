package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.deserializeList
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.mapToClass
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.mapToJson
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.serializeList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JsonTest {

    @Test
    fun `kan serialisere og deserialisere en string`() {
        val hello = "hello"
        val lestHello = hello.mapToJson().mapToClass(String::class.java)
        assertThat(lestHello).isEqualTo(hello)
    }

    @Test
    fun `kan serialisere og deserialisere en data-klasse`() {
        val person = Person("Ola Nordmann", 42)
        val lestPerson = person.mapToJson().mapToClass(Person::class.java)
        assertThat(lestPerson).isEqualTo(person)
    }

    @Test
    fun `kan serialisere og deserialisere en liste av data-klasse`() {
        val person1 = Person("Ola Nordmann", 42)
        val person2 = Person("Kari Nordmann", 42)
        val inputList = listOf(person1,person2)
        val lestePersoner = inputList.serializeList().deserializeList<Person>()
        assertThat(lestePersoner).containsExactlyElementsOf(inputList)
    }


    data class Person(val navn:String, val alder:Int)
}