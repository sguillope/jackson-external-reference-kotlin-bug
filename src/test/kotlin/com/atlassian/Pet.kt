package com.atlassian

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase

data class Pet(
    val type: String,

    @JsonTypeInfo(
        use = Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
    )
    @JsonTypeIdResolver(AnimalTypeIdResolver::class)
    val animal: Animal
)

interface Animal
class Cat : Animal
class Dog : Animal

private class AnimalTypeIdResolver : TypeIdResolverBase() {
    override fun idFromValue(value: Any): String {
        return idFromValueAndType(value, value.javaClass)
    }

    override fun idFromValueAndType(value: Any, suggestedType: Class<*>): String {
        if (suggestedType.isAssignableFrom(Cat::class.java)) {
            return "cat"
        } else if (suggestedType.isAssignableFrom(Dog::class.java)) {
            return "dog"
        }
        throw RuntimeException("Unknown type")
    }

    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        if ("cat" == id) {
            return context.constructType(Cat::class.java)
        } else if ("dog" == id) {
            return context.constructType(Dog::class.java)
        }
        throw RuntimeException("Unknown id")
    }

    override fun getMechanism(): Id {
        return Id.NAME
    }
}
