package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.*
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene.Feilinformasjon
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene.IdentRolle
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
        val inputList = listOf(person1, person2)
        val lestePersoner = inputList.serializeList().deserializeList<Person>()
        assertThat(lestePersoner).containsExactlyElementsOf(inputList)
    }

    @Test
    fun `kan serialsere og deserialse en liste av feilinformasjon`() {
        val ugyldigIdent = Feilinformasjon.UgyldigIdent(
            message = "m",
            exceptionType = "et1",
            exceptionMessage = "em",
            ident = "ident",
            identRolle = IdentRolle.BARNETRYGDMOTTAKER,
        )
        val overlappendeBarnetrygdperioder = Feilinformasjon.OverlappendeBarnetrygdperioder(
            message = "obpm",
            exceptionType = "et",
            exceptionMessage = "em",
            omsorgsmottaker = "om",
        )
        val list = listOf(ugyldigIdent, overlappendeBarnetrygdperioder)
        val serialized = list.serializeList()
        val newList = serialized.deserializeList<Feilinformasjon>()
        assertThat(newList).isEqualTo(list)
    }


    data class Person(val navn: String, val alder: Int)
}