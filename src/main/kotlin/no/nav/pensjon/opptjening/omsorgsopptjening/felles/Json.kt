package no.nav.pensjon.opptjening.omsorgsopptjening.felles

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

val mapper: ObjectMapper = jacksonObjectMapper()
    .registerModule(KotlinModule.Builder().build())
    .registerModule(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

fun Any.mapToJson(): String = mapper.writeValueAsString(this)

fun <T> String.mapToClass(valueType: Class<T>): T = mapper.readValue(this, valueType)

fun serialize(value: Any): String {
    return mapper.writeValueAsString(value)
}

inline fun <reified T> deserialize(value: String): T {
    return mapper.readValue(value)
}

inline fun <reified T> List<T>.serializeList(): String {
    val listType = mapper.typeFactory.constructCollectionLikeType(List::class.java, T::class.java)
    return mapper.writerFor(listType).writeValueAsString(this)
}

inline fun <reified T> String.deserializeList(): List<T> {
    val listType = mapper.typeFactory.constructCollectionLikeType(List::class.java, T::class.java)
    return mapper.readerFor(listType).readValue(this)
}
