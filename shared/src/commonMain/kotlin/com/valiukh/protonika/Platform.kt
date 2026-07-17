package com.valiukh.protonika

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform