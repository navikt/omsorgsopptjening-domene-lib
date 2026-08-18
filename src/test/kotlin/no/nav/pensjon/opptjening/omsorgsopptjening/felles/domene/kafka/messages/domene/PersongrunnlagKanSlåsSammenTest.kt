package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.april
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.desember
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.januar
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.juli
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.mai
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Dedikert testsuite for [KanSlåsSammen]-implementasjonene på
 * [PersongrunnlagMelding.Omsorgsperiode] og [PersongrunnlagMelding.Hjelpestønadperiode].
 *
 * Verifiserer at *alle* feltene som inngår i sammenligningen faktisk blir tatt hensyn til,
 * ikke bare perioden (tidsrommet).
 */
class PersongrunnlagKanSlåsSammenTest {

    @Nested
    inner class OmsorgsperiodeKanSlåsSammen {

        private fun periode(
            fom: java.time.YearMonth = januar(2022),
            tom: java.time.YearMonth = april(2022),
            omsorgstype: Omsorgstype = Omsorgstype.FULL_BARNETRYGD,
            omsorgsmottaker: String = "barn",
            kilde: Kilde = Kilde.BARNETRYGD,
            utbetalt: Int = 2000,
            landstilknytning: Landstilknytning = Landstilknytning.NORGE,
            omsorgsyterHarSelvstendigRett: Boolean = false,
        ): PersongrunnlagMelding.Omsorgsperiode {
            return PersongrunnlagMelding.Omsorgsperiode(
                fom = fom,
                tom = tom,
                omsorgstype = omsorgstype,
                omsorgsmottaker = omsorgsmottaker,
                kilde = kilde,
                utbetalt = utbetalt,
                landstilknytning = landstilknytning,
                omsorgsyterHarSelvstendigRett = omsorgsyterHarSelvstendigRett,
            )
        }

        @Test
        fun `identiske perioder med tilstøtende tidsrom kan slås sammen`() {
            val a = periode(fom = januar(2022), tom = april(2022))
            val b = periode(fom = mai(2022), tom = juli(2022))

            assertThat(a.kanSlåsSammen(b)).isTrue()
        }

        @Test
        fun `ulik omsorgstype hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(omsorgstype = Omsorgstype.FULL_BARNETRYGD)
            val b = periode(fom = mai(2022), tom = juli(2022), omsorgstype = Omsorgstype.DELT_BARNETRYGD)

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `ulik omsorgsmottaker hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(omsorgsmottaker = "barn1")
            val b = periode(fom = mai(2022), tom = juli(2022), omsorgsmottaker = "barn2")

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `ulik kilde hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(kilde = Kilde.BARNETRYGD)
            val b = periode(fom = mai(2022), tom = juli(2022), kilde = Kilde.INFOTRYGD)

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `ulikt utbetalt beløp hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(utbetalt = 2000)
            val b = periode(fom = mai(2022), tom = juli(2022), utbetalt = 1000)

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `ulik landstilknytning hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(landstilknytning = Landstilknytning.NORGE)
            val b = periode(fom = mai(2022), tom = juli(2022), landstilknytning = Landstilknytning.EØS_NORGE_SEKUNDÆR)

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `ulik omsorgsyterHarSelvstendigRett hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(omsorgsyterHarSelvstendigRett = false)
            val b = periode(fom = mai(2022), tom = juli(2022), omsorgsyterHarSelvstendigRett = true)

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `overlappende perioder med alle andre felter like kan slås sammen`() {
            val a = periode(fom = januar(2022), tom = april(2022))
            val b = periode(fom = april(2022), tom = juli(2022))

            assertThat(a.kanSlåsSammen(b)).isTrue()
        }

        @Test
        fun `identiske perioder med tidsmessig gap kan ikke slås sammen`() {
            val a = periode(fom = januar(2022), tom = april(2022))
            val b = periode(fom = desember(2022), tom = desember(2022))

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `slåSammen kaster exception dersom periodene ikke kan slås sammen`() {
            val a = periode(omsorgsmottaker = "barn1")
            val b = periode(fom = mai(2022), tom = juli(2022), omsorgsmottaker = "barn2")

            assertThrows<IllegalArgumentException> { a.slåSammen(b) }
        }

        @Test
        fun `slåSammen beholder alle felter og utvider perioden til å dekke begge`() {
            val a = periode(fom = januar(2022), tom = april(2022))
            val b = periode(fom = mai(2022), tom = juli(2022))

            val resultat = a.slåSammen(b)

            assertThat(resultat.fom).isEqualTo(januar(2022))
            assertThat(resultat.tom).isEqualTo(juli(2022))
            assertThat(resultat.omsorgstype).isEqualTo(a.omsorgstype)
            assertThat(resultat.omsorgsmottaker).isEqualTo(a.omsorgsmottaker)
            assertThat(resultat.kilde).isEqualTo(a.kilde)
            assertThat(resultat.utbetalt).isEqualTo(a.utbetalt)
            assertThat(resultat.landstilknytning).isEqualTo(a.landstilknytning)
            assertThat(resultat.omsorgsyterHarSelvstendigRett).isEqualTo(a.omsorgsyterHarSelvstendigRett)
        }
    }

    @Nested
    inner class HjelpestønadperiodeKanSlåsSammen {

        private fun periode(
            fom: java.time.YearMonth = januar(2022),
            tom: java.time.YearMonth = april(2022),
            omsorgstype: Omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3,
            omsorgsmottaker: String = "barn",
            kilde: Kilde = Kilde.INFOTRYGD,
        ): PersongrunnlagMelding.Hjelpestønadperiode {
            return PersongrunnlagMelding.Hjelpestønadperiode(
                fom = fom,
                tom = tom,
                omsorgstype = omsorgstype,
                omsorgsmottaker = omsorgsmottaker,
                kilde = kilde,
            )
        }

        @Test
        fun `identiske perioder med tilstøtende tidsrom kan slås sammen`() {
            val a = periode(fom = januar(2022), tom = april(2022))
            val b = periode(fom = mai(2022), tom = juli(2022))

            assertThat(a.kanSlåsSammen(b)).isTrue()
        }

        @Test
        fun `ulik omsorgstype hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3)
            val b = periode(fom = mai(2022), tom = juli(2022), omsorgstype = Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_4)

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `ulik omsorgsmottaker hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(omsorgsmottaker = "barn1")
            val b = periode(fom = mai(2022), tom = juli(2022), omsorgsmottaker = "barn2")

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `ulik kilde hindrer sammenslåing selv om perioden er tilstøtende`() {
            val a = periode(kilde = Kilde.INFOTRYGD)
            val b = periode(fom = mai(2022), tom = juli(2022), kilde = Kilde.BARNETRYGD)

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `identiske perioder med tidsmessig gap kan ikke slås sammen`() {
            val a = periode(fom = januar(2022), tom = april(2022))
            val b = periode(fom = desember(2022), tom = desember(2022))

            assertThat(a.kanSlåsSammen(b)).isFalse()
        }

        @Test
        fun `slåSammen kaster exception dersom periodene ikke kan slås sammen`() {
            val a = periode(kilde = Kilde.INFOTRYGD)
            val b = periode(fom = mai(2022), tom = juli(2022), kilde = Kilde.BARNETRYGD)

            assertThrows<IllegalArgumentException> { a.slåSammen(b) }
        }

        @Test
        fun `slåSammen beholder alle felter og utvider perioden til å dekke begge`() {
            val a = periode(fom = januar(2022), tom = april(2022))
            val b = periode(fom = mai(2022), tom = juli(2022))

            val resultat = a.slåSammen(b)

            assertThat(resultat.fom).isEqualTo(januar(2022))
            assertThat(resultat.tom).isEqualTo(juli(2022))
            assertThat(resultat.omsorgstype).isEqualTo(a.omsorgstype)
            assertThat(resultat.omsorgsmottaker).isEqualTo(a.omsorgsmottaker)
            assertThat(resultat.kilde).isEqualTo(a.kilde)
        }
    }
}
