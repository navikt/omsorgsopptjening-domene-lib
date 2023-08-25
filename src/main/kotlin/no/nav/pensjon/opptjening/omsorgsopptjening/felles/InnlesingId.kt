package no.nav.pensjon.opptjening.omsorgsopptjening.felles

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.UUID

data class InnlesingId(
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
        const val identifier: String = "x-innlesing-id"

        fun generate(): InnlesingId {
            return InnlesingId(UUID.randomUUID())
        }

        @JvmStatic
        @JsonCreator
        fun fromString(value: String): InnlesingId {
            return InnlesingId(UUID.fromString(value))
        }
    }
}