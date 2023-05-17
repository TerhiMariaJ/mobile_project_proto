package fi.tuni.mobile_proto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Users(var name: String? = null, var mass: Int = 0, var height: Int = 0)