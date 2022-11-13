package com.tutorial.newbostonbank.controller

import com.tutorial.newbostonbank.exception.UnprocessableEntityException
import com.tutorial.newbostonbank.exception.ElementNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController

@RestController
class BaseController {

    @ExceptionHandler(ElementNotFoundException::class)
    fun handleNotFound(e: ElementNotFoundException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(UnprocessableEntityException::class)
    fun handleUnprocessableEntity(e: UnprocessableEntityException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.UNPROCESSABLE_ENTITY)
}