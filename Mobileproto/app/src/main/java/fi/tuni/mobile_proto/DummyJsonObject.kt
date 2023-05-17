package fi.tuni.mobile_proto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class DummyJsonObject(var results: MutableList<Users>? = null)