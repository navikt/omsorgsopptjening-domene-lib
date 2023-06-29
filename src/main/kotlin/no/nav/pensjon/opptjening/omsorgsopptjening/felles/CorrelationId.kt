package no.nav.pensjon.opptjening.omsorgsopptjening.felles

import java.util.UUID

object CorrelationId {
    const val name = "x-correlation-id"
    fun generate(): String = UUID.randomUUID().toString()
}