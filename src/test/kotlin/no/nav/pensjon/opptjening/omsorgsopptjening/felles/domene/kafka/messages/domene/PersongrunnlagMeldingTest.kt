package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.CorrelationId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.InnlesingId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.deserialize
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.Rådata
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.RådataFraKilde
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.april
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.desember
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.februar
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.januar
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.juli
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.juni
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.mai
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.mars
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.november
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.serialize
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.skyscreamer.jsonassert.JSONAssert

class PersongrunnlagMeldingTest {

    @Test
    fun `kan serialisere og deserialisere melding med persongrunnlag`() {
        val m = PersongrunnlagMelding(
            omsorgsyter = "o",
            persongrunnlag = listOf(
                PersongrunnlagMelding.Persongrunnlag(
                    omsorgsyter = "o",
                    omsorgsperioder = listOf(
                        PersongrunnlagMelding.Omsorgsperiode(
                            fom = januar(2022),
                            tom = april(2022),
                            omsorgsmottaker = "b",
                            omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                            kilde = Kilde.BARNETRYGD,
                            utbetalt = 2000,
                            landstilknytning = Landstilknytning.NORGE,
                        ),
                    ),
                    hjelpestønadsperioder = listOf(
                        PersongrunnlagMelding.Hjelpestønadperiode(
                            fom = januar(2022),
                            tom = april(2022),
                            omsorgsmottaker = "b",
                            omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_4,
                            kilde = Kilde.INFOTRYGD,
                        ),
                    )
                )
            ),
            feilinfo = emptyList(),
            rådata = Rådata(
                data = listOf(
                    RådataFraKilde(data = mapOf("a" to "b")),
                )
            ),
            innlesingId = InnlesingId.fromString("ecbfa0ee-da70-4abd-a8f3-b84319b36bf1"),
            correlationId = CorrelationId.fromString("3b16c8bf-4682-442d-975e-8be450cf3877")
        )

        val expected = """
          {"omsorgsyter":"o","persongrunnlag":[{"omsorgsyter":"o","omsorgsperioder":[{"fom":"2022-01","tom":"2022-04","omsorgstype":"FULL_BARNETRYGD","omsorgsmottaker":"b","kilde":"BARNETRYGD","utbetalt":2000,"landstilknytning":"NORGE"}],"hjelpestønadsperioder":[{"fom":"2022-01","tom":"2022-04","omsorgstype":"HJELPESTØNAD_FORHØYET_SATS_4","omsorgsmottaker":"b","kilde":"INFOTRYGD"}]}],"feilinfo":[],"rådata":[{"a":"b"}],"innlesingId":"ecbfa0ee-da70-4abd-a8f3-b84319b36bf1","correlationId":"3b16c8bf-4682-442d-975e-8be450cf3877"}
        """.trimIndent()

        val serialized = serialize(m)

        JSONAssert.assertEquals(expected, serialized, true)

        val deserialized = deserialize<PersongrunnlagMelding>(serialized)

        assertThat(deserialized).isEqualTo(m)
    }

    @Test
    fun `kan serialisere og deserialisere melding med feilinfo`() {
        val m = PersongrunnlagMelding(
            omsorgsyter = "o",
            persongrunnlag = emptyList(),
            feilinfo = listOf(
                Feilinformasjon.OverlappendeBarnetrygdperioder(
                    "bt",
                    exceptionType = "et",
                    exceptionMessage = "em",
                )
            ),
            rådata = Rådata(
                data = listOf(
                    RådataFraKilde(data = mapOf("a" to "b")),
                )
            ),
            innlesingId = InnlesingId.fromString("ecbfa0ee-da70-4abd-a8f3-b84319b36bf1"),
            correlationId = CorrelationId.fromString("3b16c8bf-4682-442d-975e-8be450cf3877")
        )

        val expected = """
          {"omsorgsyter":"o","persongrunnlag":[],"feilinfo":[{"type":"OverlappendeBarnetrygdperioder","message":"bt","exceptionType":"et","exceptionMessage":"em"}],"rådata":[{"a":"b"}],"innlesingId":"ecbfa0ee-da70-4abd-a8f3-b84319b36bf1","correlationId":"3b16c8bf-4682-442d-975e-8be450cf3877"}
        """.trimIndent()

        val serialized = serialize(m)

        JSONAssert.assertEquals(expected, serialized, true)

        val deserialized = deserialize<PersongrunnlagMelding>(serialized)

        assertThat(deserialized).isEqualTo(m)
    }


    @Test
    fun `deserialisering er bakoverkompatibel for manglende feilinformasjon`() {
        val serialized = """
          {"omsorgsyter":"o","persongrunnlag":[{"omsorgsyter":"o","omsorgsperioder":[{"fom":"2022-01","tom":"2022-04","omsorgstype":"FULL_BARNETRYGD","omsorgsmottaker":"b","kilde":"BARNETRYGD","utbetalt":2000,"landstilknytning":"NORGE"}],"hjelpestønadsperioder":[{"fom":"2022-01","tom":"2022-04","omsorgstype":"HJELPESTØNAD_FORHØYET_SATS_4","omsorgsmottaker":"b","kilde":"INFOTRYGD"}]}],"rådata":[{"a":"b"}],"innlesingId":"ecbfa0ee-da70-4abd-a8f3-b84319b36bf1","correlationId":"3b16c8bf-4682-442d-975e-8be450cf3877"}
        """.trimIndent()

        val expected = PersongrunnlagMelding(
            omsorgsyter = "o",
            persongrunnlag = listOf(
                PersongrunnlagMelding.Persongrunnlag(
                    omsorgsyter = "o",
                    omsorgsperioder = listOf(
                        PersongrunnlagMelding.Omsorgsperiode(
                            fom = januar(2022),
                            tom = april(2022),
                            omsorgsmottaker = "b",
                            omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                            kilde = Kilde.BARNETRYGD,
                            utbetalt = 2000,
                            landstilknytning = Landstilknytning.NORGE,
                        ),
                    ),
                    hjelpestønadsperioder = listOf(
                        PersongrunnlagMelding.Hjelpestønadperiode(
                            fom = januar(2022),
                            tom = april(2022),
                            omsorgsmottaker = "b",
                            omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_4,
                            kilde = Kilde.INFOTRYGD,
                        ),
                    )
                )
            ),
            feilinfo = emptyList(),
            rådata = Rådata(
                data = listOf(
                    RådataFraKilde(data = mapOf("a" to "b")),
                )
            ),
            innlesingId = InnlesingId.fromString("ecbfa0ee-da70-4abd-a8f3-b84319b36bf1"),
            correlationId = CorrelationId.fromString("3b16c8bf-4682-442d-975e-8be450cf3877")
        )

        val deserialized = deserialize<PersongrunnlagMelding>(serialized)

        assertThat(deserialized).isEqualTo(expected)
    }

    @Test
    fun `ikke tillatt med overlappende perioder for samme omsorgsmottaker barnetrygd`() {
        val barn1 = "1"
        assertThrows<IllegalArgumentException> {
            PersongrunnlagMelding.Persongrunnlag(
                omsorgsyter = "o",
                omsorgsperioder = listOf(
                    PersongrunnlagMelding.Omsorgsperiode(
                        fom = januar(2022),
                        tom = april(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                        kilde = Kilde.BARNETRYGD,
                        utbetalt = 2000,
                        landstilknytning = Landstilknytning.NORGE,
                    ),
                    PersongrunnlagMelding.Omsorgsperiode(
                        fom = januar(2022),
                        tom = april(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                        kilde = Kilde.BARNETRYGD,
                        utbetalt = 2000,
                        landstilknytning = Landstilknytning.NORGE,
                    ),
                ),
                hjelpestønadsperioder = emptyList()
            )
        }
    }

    @Test
    fun `ikke tillatt med overlappende perioder for samme omsorgsmottaker hjelpestønad `() {
        val barn1 = "1"
        assertThrows<IllegalArgumentException> {
            PersongrunnlagMelding.Persongrunnlag(
                omsorgsyter = "o",
                omsorgsperioder = emptyList(),
                hjelpestønadsperioder = listOf(
                    PersongrunnlagMelding.Hjelpestønadperiode(
                        fom = januar(2022),
                        tom = november(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                        kilde = Kilde.INFOTRYGD,
                    ),
                    PersongrunnlagMelding.Hjelpestønadperiode(
                        fom = januar(2022),
                        tom = november(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                        kilde = Kilde.INFOTRYGD,
                    ),
                )
            )
        }
    }

    @Test
    fun `ulike omsorgsmottakere kan ha overlappende måneder barnetrgyd`() {
        val barn1 = "1"
        val barn2 = "2"
        assertDoesNotThrow {
            PersongrunnlagMelding.Persongrunnlag(
                omsorgsyter = "o",
                omsorgsperioder = listOf(
                    PersongrunnlagMelding.Omsorgsperiode(
                        fom = januar(2022),
                        tom = april(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                        kilde = Kilde.BARNETRYGD,
                        utbetalt = 2000,
                        landstilknytning = Landstilknytning.NORGE,
                    ),
                    PersongrunnlagMelding.Omsorgsperiode(
                        fom = januar(2022),
                        tom = april(2022),
                        omsorgsmottaker = barn2,
                        omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                        kilde = Kilde.BARNETRYGD,
                        utbetalt = 2000,
                        landstilknytning = Landstilknytning.NORGE,
                    ),
                ),
                hjelpestønadsperioder = emptyList()
            )
        }
    }

    @Test
    fun `ulike omsorgsmottakere kan ha overlappende måneder hjelpestønad`() {
        val barn1 = "1"
        val barn2 = "2"
        assertDoesNotThrow {
            PersongrunnlagMelding.Persongrunnlag(
                omsorgsyter = "o",
                omsorgsperioder = emptyList(),
                hjelpestønadsperioder = listOf(
                    PersongrunnlagMelding.Hjelpestønadperiode(
                        fom = januar(2022),
                        tom = november(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                        kilde = Kilde.INFOTRYGD,
                    ),
                    PersongrunnlagMelding.Hjelpestønadperiode(
                        fom = januar(2022),
                        tom = november(2022),
                        omsorgsmottaker = barn2,
                        omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                        kilde = Kilde.INFOTRYGD,
                    ),
                ),
            )
        }
    }

    @Test
    fun `slår sammen like perioder`() {
        val omsorgsyter = "0"
        val barn1 = "1"
        val barn2 = "2"
        val barnhs1 = "11"
        val barnhs2 = "22"

        val persongrunnlag = PersongrunnlagMelding.Persongrunnlag.of(
            omsorgsyter = omsorgsyter,
            omsorgsperioder = listOf(
                PersongrunnlagMelding.Omsorgsperiode(
                    fom = januar(2022),
                    tom = april(2022),
                    omsorgsmottaker = barn1,
                    omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                    kilde = Kilde.BARNETRYGD,
                    utbetalt = 2000,
                    landstilknytning = Landstilknytning.NORGE,
                ),
                PersongrunnlagMelding.Omsorgsperiode(
                    fom = mai(2022),
                    tom = juni(2022),
                    omsorgsmottaker = barn1,
                    omsorgstype = Omsorgstype.DELT_BARNETRYGD,
                    kilde = Kilde.BARNETRYGD,
                    utbetalt = 1000,
                    landstilknytning = Landstilknytning.NORGE,
                ),
                PersongrunnlagMelding.Omsorgsperiode(
                    fom = juli(2022),
                    tom = desember(2022),
                    omsorgsmottaker = barn1,
                    omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                    kilde = Kilde.BARNETRYGD,
                    utbetalt = 2000,
                    landstilknytning = Landstilknytning.NORGE,
                ),
                PersongrunnlagMelding.Omsorgsperiode(
                    fom = mai(2022),
                    tom = desember(2022),
                    omsorgsmottaker = barn2,
                    omsorgstype = Omsorgstype.DELT_BARNETRYGD,
                    kilde = Kilde.BARNETRYGD,
                    utbetalt = 1000,
                    landstilknytning = Landstilknytning.NORGE,
                ),
                PersongrunnlagMelding.Omsorgsperiode(
                    fom = januar(2022),
                    tom = april(2022),
                    omsorgsmottaker = barn2,
                    omsorgstype = Omsorgstype.DELT_BARNETRYGD,
                    kilde = Kilde.BARNETRYGD,
                    utbetalt = 1000,
                    landstilknytning = Landstilknytning.NORGE,
                ),
            ),
            hjelpestønadsperioder = listOf(
                PersongrunnlagMelding.Hjelpestønadperiode(
                    fom = januar(2022),
                    tom = februar(2022),
                    omsorgsmottaker = barnhs1,
                    omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                    kilde = Kilde.INFOTRYGD,
                ),
                PersongrunnlagMelding.Hjelpestønadperiode(
                    fom = desember(2022),
                    tom = desember(2022),
                    omsorgsmottaker = barnhs1,
                    omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_4,
                    kilde = Kilde.INFOTRYGD,
                ),
                PersongrunnlagMelding.Hjelpestønadperiode(
                    fom = januar(2022),
                    tom = februar(2022),
                    omsorgsmottaker = barnhs1,
                    omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                    kilde = Kilde.INFOTRYGD,
                ),
                PersongrunnlagMelding.Hjelpestønadperiode(
                    fom = mars(2022),
                    tom = november(2022),
                    omsorgsmottaker = barnhs1,
                    omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                    kilde = Kilde.INFOTRYGD,
                ),
                PersongrunnlagMelding.Hjelpestønadperiode(
                    fom = januar(2022),
                    tom = desember(2022),
                    omsorgsmottaker = barnhs2,
                    omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                    kilde = Kilde.INFOTRYGD,
                ),
            )
        )

        assertEquals(
            PersongrunnlagMelding.Persongrunnlag(
                omsorgsyter = omsorgsyter,
                omsorgsperioder = listOf(
                    PersongrunnlagMelding.Omsorgsperiode(
                        fom = januar(2022),
                        tom = april(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                        kilde = Kilde.BARNETRYGD,
                        utbetalt = 2000,
                        landstilknytning = Landstilknytning.NORGE,
                    ),
                    PersongrunnlagMelding.Omsorgsperiode(
                        fom = mai(2022),
                        tom = juni(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.DELT_BARNETRYGD,
                        kilde = Kilde.BARNETRYGD,
                        utbetalt = 1000,
                        landstilknytning = Landstilknytning.NORGE,
                    ),
                    PersongrunnlagMelding.Omsorgsperiode(
                        fom = juli(2022),
                        tom = desember(2022),
                        omsorgsmottaker = barn1,
                        omsorgstype = Omsorgstype.FULL_BARNETRYGD,
                        kilde = Kilde.BARNETRYGD,
                        utbetalt = 2000,
                        landstilknytning = Landstilknytning.NORGE,
                    ),
                    PersongrunnlagMelding.Omsorgsperiode(
                        fom = januar(2022),
                        tom = desember(2022),
                        omsorgsmottaker = barn2,
                        omsorgstype = Omsorgstype.DELT_BARNETRYGD,
                        kilde = Kilde.BARNETRYGD,
                        utbetalt = 1000,
                        landstilknytning = Landstilknytning.NORGE,
                    ),
                ),
                hjelpestønadsperioder = listOf(
                    PersongrunnlagMelding.Hjelpestønadperiode(
                        fom = januar(2022),
                        tom = november(2022),
                        omsorgsmottaker = barnhs1,
                        omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                        kilde = Kilde.INFOTRYGD,
                    ),
                    PersongrunnlagMelding.Hjelpestønadperiode(
                        fom = desember(2022),
                        tom = desember(2022),
                        omsorgsmottaker = barnhs1,
                        omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_4,
                        kilde = Kilde.INFOTRYGD,
                    ),
                    PersongrunnlagMelding.Hjelpestønadperiode(
                        fom = januar(2022),
                        tom = desember(2022),
                        omsorgsmottaker = barnhs2,
                        omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
                        kilde = Kilde.INFOTRYGD,
                    ),
                )
            ),
            persongrunnlag
        )
    }
}