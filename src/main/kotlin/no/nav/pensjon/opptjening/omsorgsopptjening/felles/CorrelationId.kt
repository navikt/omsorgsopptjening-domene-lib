package no.nav.pensjon.opptjening.omsorgsopptjening.felles

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.UUID

data class CorrelationId(
    private val value: UUID
) {
    fun toUUID(): UUID {
        return value
    }

    @JsonValue
    override fun toString(): String {
        return value.toString()
    }

    companion object {
        const val identifier: String = "x-correlation-id"

        fun generate(): CorrelationId {
            return CorrelationId(UUID.randomUUID())
        }

        @JvmStatic
        @JsonCreator
        fun fromString(value: String): CorrelationId {
            return CorrelationId(UUID.fromString(value))
        }
    }
}