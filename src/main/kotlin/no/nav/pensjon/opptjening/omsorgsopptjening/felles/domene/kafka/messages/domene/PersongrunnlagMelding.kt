package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.CorrelationId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.InnlesingId
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.UgyldigPersongrunnlag
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.Rådata
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene.KanSlåsSammen.Companion.slåSammenLike
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode
import java.time.YearMonth

data class PersongrunnlagMelding(
    val omsorgsyter: String,
    val persongrunnlag: List<Persongrunnlag> = emptyList(),
    val feilinfo: List<Feilinformasjon> = emptyList(),
    val rådata: Rådata,
    val innlesingId: InnlesingId,
    val correlationId: CorrelationId,
) {
    init {
        require((persongrunnlag.isEmpty().xor(feilinfo.isEmpty()))) {
            "Meldingen må inneholde enten persongrunnlag eller feilinfo"
        }
    }

    fun hentPersoner(): Set<String> {
        return hentOmsorgsytere() + hentOmsorgsmottakere()
    }

    fun hentOmsorgsytere(): Set<String> {
        return persongrunnlag.map { it.omsorgsyter }.toSet()
    }

    fun hentOmsorgsmottakere(): Set<String> {
        return persongrunnlag.flatMap { it.hentOmsorgsmottakere() }.toSet()
    }

    fun harFeilet(): Boolean {
        return feilinfo.isNotEmpty()
    }

    data class Persongrunnlag(
        val omsorgsyter: String,
        val omsorgsperioder: List<Omsorgsperiode>,
        val hjelpestønadsperioder: List<Hjelpestønadperiode>,
    ) {
        init {
            ikkeTillatOverlappendeOmsorgsperioderForSammeOmsorgsmottaker()
            ikkeTillatOverlappendeOmsorgsperioderForSammeHjelpestønadsmottaker()
        }

        private fun ikkeTillatOverlappendeOmsorgsperioderForSammeOmsorgsmottaker() {
            omsorgsperioder
                .groupBy { it.omsorgsmottaker }
                .mapValues { it.value.map { it.periode() } }
                .forEach { (omsorgsmottaker, perioderForOmsorgsmottaker) ->
                    val overlappendePerioder = perioderForOmsorgsmottaker.filter {
                        it.overlappendeMåneder(
                            (perioderForOmsorgsmottaker - it)
                                .flatMap { it.alleMåneder() }
                                .toSet()
                        ).isNotEmpty()
                    }
                    if (overlappendePerioder.isNotEmpty()) {
                        throw UgyldigPersongrunnlag.OverlappendeOmsorgsperiode(
                            msg = "Overlappende perioder for samme omsorgsmottaker",
                            perioder = overlappendePerioder,
                            omsorgsmottaker = omsorgsmottaker,
                        )
                    }
                }
        }

        private fun ikkeTillatOverlappendeOmsorgsperioderForSammeHjelpestønadsmottaker() {
            hjelpestønadsperioder
                .groupBy { it.omsorgsmottaker }
                .mapValues { it.value.map { it.periode() } }
                .forEach { (omsorgsmottaker, perioderForOmsorgsmottaker) ->
                    val overlappendePerioder = perioderForOmsorgsmottaker.filter {
                        it.overlappendeMåneder(
                            (perioderForOmsorgsmottaker - it)
                                .flatMap { it.alleMåneder() }
                                .toSet()
                        ).isNotEmpty()
                    }
                    if (overlappendePerioder.isNotEmpty()) {
                        throw UgyldigPersongrunnlag.OverlappendeOmsorgsperiode(
                            msg = "Overlappende perioder for samme omsorgsmottaker",
                            perioder = overlappendePerioder,
                            omsorgsmottaker = omsorgsmottaker
                        )
                    }
                }
        }

        companion object {
            fun of(
                omsorgsyter: String,
                omsorgsperioder: List<Omsorgsperiode>,
                hjelpestønadsperioder: List<Hjelpestønadperiode>,
            ): Persongrunnlag {
                return Persongrunnlag(
                    omsorgsyter = omsorgsyter,
                    omsorgsperioder = omsorgsperioder.slåSammenLike(),
                    hjelpestønadsperioder = hjelpestønadsperioder.slåSammenLike()
                )
            }
        }

        fun medHjelpestønadPerioder(hjelpestønadsperioder: List<Hjelpestønadperiode>): Persongrunnlag {
            return of(
                omsorgsyter = omsorgsyter,
                omsorgsperioder = omsorgsperioder,
                hjelpestønadsperioder = hjelpestønadsperioder
            )
        }

        fun hentOmsorgsmottakere(): Set<String> {
            return omsorgsperioder.map { it.omsorgsmottaker }.toSet()
        }
    }

    data class Omsorgsperiode(
        val fom: YearMonth,
        val tom: YearMonth,
        val omsorgstype: Omsorgstype,
        val omsorgsmottaker: String,
        val kilde: Kilde,
        val utbetalt: Int,
        val landstilknytning: Landstilknytning
    ) : KanSlåsSammen<Omsorgsperiode> {
        init {
            if (!gyldigOmsorgsType(omsorgstype)) {
                throw UgyldigPersongrunnlag.UgyldigOmsorgstype(omsorgstype)
            }
        }

        private fun gyldigOmsorgsType(omsorgstype: Omsorgstype): Boolean {
            return omsorgstype == Omsorgstype.FULL_BARNETRYGD
                    || omsorgstype == Omsorgstype.DELT_BARNETRYGD
                    || omsorgstype == Omsorgstype.USIKKER_BARNETRYGD
        }

        fun periode(): Periode {
            return Periode(fom, tom)
        }

        override fun sorteringVedSammenslåing(): Comparator<Omsorgsperiode> {
            return compareBy<Omsorgsperiode> { it.omsorgsmottaker }.thenBy { it.periode().min() }
        }

        override fun kanSlåsSammen(other: Omsorgsperiode): Boolean {
            return periode().kanSlåsSammen(other.periode())
                    && omsorgstype == other.omsorgstype
                    && omsorgsmottaker == other.omsorgsmottaker
                    && kilde == other.kilde
                    && utbetalt == other.utbetalt
                    && landstilknytning == other.landstilknytning
        }

        override fun slåSammen(other: Omsorgsperiode): Omsorgsperiode {
            require(kanSlåsSammen(other)) { "Kan ikke slå sammen perioder som ikke er like" }
            return copy(
                fom = minOf(fom, other.fom),
                tom = maxOf(tom, other.tom)
            )
        }
    }

    data class Hjelpestønadperiode(
        val fom: YearMonth,
        val tom: YearMonth,
        val omsorgstype: Omsorgstype,
        val omsorgsmottaker: String,
        val kilde: Kilde,
    ) : KanSlåsSammen<Hjelpestønadperiode> {
        init {
            require(omsorgstype == Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_3 || omsorgstype == Omsorgstype.HJELPESTØNAD_FORHØYET_SATS_4) { "Ugyldig omsorgstype" }
        }

        fun periode(): Periode {
            return Periode(fom, tom)
        }

        override fun sorteringVedSammenslåing(): Comparator<Hjelpestønadperiode> {
            return compareBy<Hjelpestønadperiode> { it.omsorgsmottaker }.thenBy { it.periode().min() }
        }

        override fun kanSlåsSammen(other: Hjelpestønadperiode): Boolean {
            return periode().kanSlåsSammen(other.periode())
                    && omsorgstype == other.omsorgstype
                    && omsorgsmottaker == other.omsorgsmottaker
                    && kilde == other.kilde
        }

        override fun slåSammen(other: Hjelpestønadperiode): Hjelpestønadperiode {
            require(kanSlåsSammen(other)) { "Kan ikke slå sammen perioder som ikke er like" }
            return copy(
                fom = minOf(fom, other.fom),
                tom = maxOf(tom, other.tom)
            )
        }
    }
}