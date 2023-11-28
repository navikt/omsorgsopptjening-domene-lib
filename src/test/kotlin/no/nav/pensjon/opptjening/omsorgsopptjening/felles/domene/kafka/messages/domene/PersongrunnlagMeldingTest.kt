package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.april
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.desember
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.februar
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.januar
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.juli
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.juni
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.mai
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.mars
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.november
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class PersongrunnlagMeldingTest {

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