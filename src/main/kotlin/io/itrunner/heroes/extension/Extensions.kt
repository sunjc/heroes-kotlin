package io.itrunner.heroes.extension

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun asJson(value: Any): String = jacksonObjectMapper().writeValueAsString(value)