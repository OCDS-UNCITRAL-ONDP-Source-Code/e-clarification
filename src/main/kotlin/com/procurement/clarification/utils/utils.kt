package com.procurement.clarification.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.procurement.clarification.domain.fail.Fail
import com.procurement.clarification.lib.functional.Result
import com.procurement.clarification.model.dto.databinding.IntDeserializer
import com.procurement.clarification.model.dto.databinding.JsonDateDeserializer
import com.procurement.clarification.model.dto.databinding.JsonDateSerializer
import com.procurement.clarification.model.dto.databinding.StringsDeserializer
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*


private object JsonMapper {

    val mapper: ObjectMapper = ObjectMapper()
    var dateTimeFormatter: DateTimeFormatter

    init {
        val module = SimpleModule()
        module.addSerializer(LocalDateTime::class.java, JsonDateSerializer())
        module.addDeserializer(LocalDateTime::class.java, JsonDateDeserializer())
        module.addDeserializer(String::class.java, StringsDeserializer())
        module.addDeserializer(Int::class.java, IntDeserializer())

        mapper.registerModule(module)
        mapper.registerKotlinModule()
        mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
        mapper.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)

        dateTimeFormatter = DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(ChronoField.HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                .appendLiteral('Z')
                .toFormatter()
    }

}

/*Date utils*/
fun String.toLocal(): LocalDateTime {
    return LocalDateTime.parse(this, JsonMapper.dateTimeFormatter)
}

/*Date utils*/
fun LocalDateTime.toDate(): Date {
    return Date.from(this.toInstant(ZoneOffset.UTC))
}

fun Date.toLocal(): LocalDateTime {
    return LocalDateTime.ofInstant(this.toInstant(), ZoneOffset.UTC)
}

fun localNowUTC(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
}

fun milliNowUTC(): Long {
    return localNowUTC().toInstant(ZoneOffset.UTC).toEpochMilli()
}

fun LocalDateTime.toMilliseconds(): Long = this.toInstant(ZoneOffset.UTC).toEpochMilli()

/*Json utils*/
fun <Any> toJson(obj: Any): String {
    try {
        return JsonMapper.mapper.writeValueAsString(obj)
    } catch (e: JsonProcessingException) {
        throw RuntimeException(e)
    }
}

fun <T> toObject(clazz: Class<T>, json: String): T {
    try {
        return JsonMapper.mapper.readValue(json, clazz)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }
}

fun <T> toObject(clazz: Class<T>, json: JsonNode): T {
    try {
        return JsonMapper.mapper.treeToValue(json, clazz)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }
}

fun String.toNode(): JsonNode = try {
    JsonMapper.mapper.readTree(this)
} catch (exception: JsonProcessingException) {
    throw IllegalArgumentException("Error parsing JSON to JsonNode.", exception)
}

/*Collection*/
fun <T> Collection<T>.containsAny(dest: Collection<T>): Boolean {
    for (value in dest) {
        if (this.contains(value)) {
            return true
        }
    }
    return false
}

fun <T : Any> JsonNode.tryToObject(target: Class<T>): Result<T, Fail.Incident.Parsing> = try {
    Result.success(JsonMapper.mapper.treeToValue(this, target))
} catch (expected: Exception) {
    Result.failure(Fail.Incident.Parsing(className = target.canonicalName, exception = expected))
}

fun <T : Any> String.tryToObject(target: Class<T>): Result<T, Fail.Incident.Parsing> = try {
    Result.success(JsonMapper.mapper.readValue(this, target))
} catch (expected: Exception) {
    Result.failure(Fail.Incident.Parsing(className = target.canonicalName, exception = expected))
}

fun String.tryToNode(): Result<JsonNode, Fail.Incident.Transforming> = try {
    Result.success(JsonMapper.mapper.readTree(this))
} catch (exception: JsonProcessingException) {
    Result.failure(Fail.Incident.Transforming(exception = exception))
}

