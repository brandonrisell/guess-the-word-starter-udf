package com.example.android.guesstheword.screens.game

sealed class GameAction {
    object NewGame : GameAction()
    object SkipWord : GameAction()
    object CorrectWord : GameAction()
    object EndGame : GameAction()
}