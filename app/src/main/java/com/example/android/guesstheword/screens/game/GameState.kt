package com.example.android.guesstheword.screens.game

data class GameState(val status: GameStatus, val wordList: List<String>, val word: String, val score: Int)

sealed class GameStatus {
    object New : GameStatus()
    object InProgress : GameStatus()
    object Complete : GameStatus()
}
