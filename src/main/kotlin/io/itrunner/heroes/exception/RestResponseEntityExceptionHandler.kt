package io.itrunner.heroes.exception

import org.springframework.core.NestedExceptionUtils.getMostSpecificCause
import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@ControllerAdvice(basePackages = ["io.itrunner.heroes.controller"])
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        EntityNotFoundException::class,
        DuplicateKeyException::class,
        DataAccessException::class,
        Exception::class
    )
    fun handleAllException(e: Exception): ResponseEntity<Any> {
        logger.error(e.message, e)

        return when (e) {
            is EntityNotFoundException -> notFound(e.simpleName(), e.message)
            is DuplicateKeyException -> badRequest(e.simpleName(), e.message)
            is DataAccessException -> badRequest(e.mostSpecificSimpleName(), e.mostSpecificMessage())
            else -> badRequest(e.mostSpecificSimpleName(), e.mostSpecificMessage())
        }
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val messages = StringBuilder()

        ex.bindingResult.globalErrors.forEach {
            messages.append(it.defaultMessage).append(";")
        }

        ex.bindingResult.fieldErrors.forEach {
            messages.append(it.field).append(" ").append(it.defaultMessage).append(";")
        }

        return badRequest(ex.simpleName(), messages.toString())
    }

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> = ResponseEntity(ErrorMessage(ex.simpleName(), ex.message), headers, status)

    private fun badRequest(error: String, message: String?): ResponseEntity<Any> =
        ResponseEntity(ErrorMessage(error, message), HttpStatus.BAD_REQUEST)

    private fun notFound(error: String, message: String?): ResponseEntity<Any> =
        ResponseEntity(ErrorMessage(error, message), HttpStatus.NOT_FOUND)

    private fun Exception.simpleName(): String = javaClass.simpleName

    private fun Exception.mostSpecificMessage(): String? = getMostSpecificCause(this).message

    private fun Exception.mostSpecificSimpleName(): String = getMostSpecificCause(this).javaClass.simpleName
}