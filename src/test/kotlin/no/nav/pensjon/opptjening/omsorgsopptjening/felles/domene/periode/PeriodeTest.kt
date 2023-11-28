package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene.KanSlåsSammen.Companion.slåSammenLike
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.april
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.desember
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.februar
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.januar
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.mai
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.mars
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.periode
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode.Companion.september
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Month
import java.time.YearMonth
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PeriodeTest {
    @Test
    fun `overlappende måneder`() {
        Periode(
            fom = YearMonth.of(2023, Month.JANUARY),
            tom = YearMonth.of(2023, Month.DECEMBER)
        ).let {
            assertEquals(Periode(), it.overlappendeMåneder(2022))
        }

        Periode(
            fom = YearMonth.of(2023, Month.JANUARY),
            tom = YearMonth.of(2023, Month.DECEMBER)
        ).let {
            assertEquals(
                Periode(
                    fom = YearMonth.of(2023, Month.JANUARY),
                    tom = YearMonth.of(2023, Month.DECEMBER)
                ), it.overlappendeMåneder(2023)
            )
        }

        Periode(
            fom = YearMonth.of(2023, Month.JANUARY),
            tom = YearMonth.of(2024, Month.DECEMBER)
        ).let {
            assertEquals(
                Periode(
                    fom = YearMonth.of(2023, Month.JANUARY),
                    tom = YearMonth.of(2023, Month.DECEMBER)
                ), it.overlappendeMåneder(2023)
            )
        }
    }

    @Test
    fun `inneholder`() {
        Periode(2023).let {
            assertTrue {
                it.inneholderMåned(YearMonth.of(2023, Month.JANUARY))
            }
            assertTrue {
                it.inneholderMåned(YearMonth.of(2023, Month.MAY))
            }
            assertFalse {
                it.inneholderMåned(YearMonth.of(2024, Month.JANUARY))
            }
        }
    }

    @Test
    fun `min and max`() {
        Periode(2023).let {
            assertEquals(YearMonth.of(2023, Month.JANUARY), it.min())
            assertEquals(YearMonth.of(2023, Month.DECEMBER), it.max())
        }
    }

    @Test
    fun `fullstendig overlapp`() {
        val a = Periode(2023)
        val b = Periode(YearMonth.of(2023, Month.MAY), YearMonth.of(2023, Month.JULY))
        assertTrue(a.overlapper(b))
        assertTrue(b.overlapper(a))
    }

    @Test
    fun `delvis overlapp start`() {
        val a = Periode(2023)
        val b = Periode(YearMonth.of(2022, Month.MAY), YearMonth.of(2023, Month.JANUARY))
        assertTrue(a.overlapper(b))
        assertTrue(b.overlapper(a))
    }

    @Test
    fun `delvis overlapp slutt`() {
        val a = Periode(2023)
        val b = Periode(YearMonth.of(2023, Month.DECEMBER), YearMonth.of(2024, Month.JANUARY))
        assertTrue(a.overlapper(b))
        assertTrue(b.overlapper(a))
    }

    @Test
    fun `ingen overlapp`() {
        val a = Periode(2023)
        val b = Periode(2024)
        assertFalse(a.overlapper(b))
        assertFalse(b.overlapper(a))
    }

    @Test
    fun `tilstøter`() {
        val a = Periode(2023)
        val b = Periode(2024)
        assertTrue(a.tilstøter(b))
        assertTrue(b.tilstøter(a))
    }

    @Test
    fun `ikke tilstøtende`() {
        val a = Periode(2023)
        val b = Periode(2025)
        assertFalse(a.tilstøter(b))
        assertFalse(b.tilstøter(a))
    }

    @Test
    fun `slå sammen tilstøtende`() {
        val a = Periode(2023)
        val b = Periode(2024)
        assertEquals(a.slåSammen(b), Periode(YearMonth.of(2023, Month.JANUARY), YearMonth.of(2024, Month.DECEMBER)))
    }

    @Test
    fun `slå sammen ikke tilstøtende`() {
        val a = Periode(2023)
        val b = Periode(2025)
        assertThrows<IllegalArgumentException> { a.slåSammen(b) }
    }

    @Test
    fun `slå sammen når alle perioder tilstøter`() {
        val like = listOf(
            januar(2022).periode(),
            februar(2022).periode(),
            mars(2022).periode(),
            april(2022).periode(),
        ).slåSammenLike().single()

        assertEquals(
            Periode(januar(2022), april(2022)),
            like
        )
    }

    @Test
    fun `slår sammen det som kan slås sammen`() {
        val like = listOf(
            januar(2022).periode(),
            februar(2022).periode(),
            april(2022).periode(),
            mai(2022).periode(),
        ).slåSammenLike()

        assertEquals(
            listOf(
                Periode(januar(2022), februar(2022)),
                Periode(april(2022), mai(2022)),
            ),
            like
        )
    }

    @Test
    fun `slår sammen overlapp`() {
        val like = listOf(
            januar(2022).periode(),
            januar(2022).periode(),
            januar(2022).periode(),
        ).slåSammenLike().single()

        assertEquals(
            januar(2022).periode(),
            like
        )
    }
}