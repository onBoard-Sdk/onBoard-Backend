package com.onboard.server.domain

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/say")
@Controller
@RestController
class HelloController {
    companion object {
        const val ABCDE = "abcde"
    }

    constructor()

    @GetMapping("/hello")
    fun hello() = "hello"

    @GetMapping("/move")
    fun move() = "move"




}

