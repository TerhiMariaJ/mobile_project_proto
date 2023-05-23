package fi.tuni.mobile_proto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Users(var firstName: String? = null, var lastName: String? = null)