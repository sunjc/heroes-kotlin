package io.itrunner.heroes.extension

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class Messages(private val messageSource: MessageSource) {
    fun getMessage(code: String, args: Array<Any>? = null): String =
        messageSource.getMessage(code, args, LocaleContextHolder.getLocale())
}

