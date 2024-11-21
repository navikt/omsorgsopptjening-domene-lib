package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.domene.Omsorgstype
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.periode.Periode

sealed class UgyldigPersongrunnlag(msg: String, cause: Throwable?) : RuntimeException(msg, cause) {
    class OverlappendeOmsorgsperiode(
        msg: String,
        val perioder: List<Periode>
    ) : UgyldigPersongrunnlag(msg, null)

    class UgyldigOmsorgstype(
        val omsorgstype: Omsorgstype,
    ) : UgyldigPersongrunnlag("ugyldig omsorgstype: ${omsorgstype.name}", null)

}