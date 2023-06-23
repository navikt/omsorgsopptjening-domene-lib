package no.nav.pensjon.opptjening.omsorgsopptjening.felles

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Month
import java.time.YearMonth
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PeriodeTest {
    @Test
    fun `overlappende måneder`(){
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
            ), it.overlappendeMåneder(2023))
        }

        Periode(
            fom = YearMonth.of(2023, Month.JANUARY),
            tom = YearMonth.of(2024, Month.DECEMBER)
        ).let {
            assertEquals(
                Periode(
                fom = YearMonth.of(2023, Month.JANUARY),
                tom = YearMonth.of(2023, Month.DECEMBER)
            ), it.overlappendeMåneder(2023))
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
    fun `min and max`(){
        Periode(2023).let {
            assertEquals(YearMonth.of(2023, Month.JANUARY), it.min())
            assertEquals(YearMonth.of(2023, Month.DECEMBER), it.max())
        }
    }
}