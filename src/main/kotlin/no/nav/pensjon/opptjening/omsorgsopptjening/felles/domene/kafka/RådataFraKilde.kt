package no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class RådataFraKilde(
    private val value: String
) {
    @JsonValue
    override fun toString(): String {
        return value
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun fromString(value: String): RådataFraKilde {
            return RådataFraKilde(value)
        }
    }
}