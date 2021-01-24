package com.example.android.guesstheword

class ImmutableList<T>(private val inner:List<T>) : List<T> by inner

fun <T> List<T>.toImmutableList(): List<T> {
    if (this is ImmutableList<T>) {
        return this
    } else {
        return ImmutableList(this)
    }
}