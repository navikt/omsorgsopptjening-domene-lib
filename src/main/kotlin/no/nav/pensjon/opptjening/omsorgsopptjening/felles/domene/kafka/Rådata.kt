package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka

import com.fasterxml.jackson.annotation.JsonValue

data class RådataFraKilde(
    @JsonValue
    private val data: Map<String, String> = emptyMap()
) : Map<String, String> by data

data class Rådata(
    @JsonValue
    private val data: List<RådataFraKilde> = emptyList()
) : List<RådataFraKilde> by data