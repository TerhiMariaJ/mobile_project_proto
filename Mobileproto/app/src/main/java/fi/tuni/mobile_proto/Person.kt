package fi.tuni.mobile_proto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Person(var firstName: String? = null, var lastName: String? = null){
    override fun toString(): String {
        return "$firstName $lastName"
    }
}