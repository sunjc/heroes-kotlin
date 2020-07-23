package io.itrunner.heroes.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

class ErrorMessage(val error: String, val message: String?) {
    val timestamp: Date = Date()
}

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class HeroNotFoundException(message: String) : Exception(message)