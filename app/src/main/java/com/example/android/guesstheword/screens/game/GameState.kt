package com.example.android.guesstheword.screens.game

sealed class GameState {
    data class InProgress(val wordList: List<String>, val word: String, val score: Int) : GameState()
    data class Complete(val score: Int) : GameState()
}
