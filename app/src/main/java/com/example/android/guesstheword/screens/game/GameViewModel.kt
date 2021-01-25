package com.example.android.guesstheword.screens.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(onNewGame())
    val gameState: StateFlow<GameState> = _gameState

    fun handleGameAction(action: GameAction, state: GameState) {
        when (action) {
            is GameAction.NewGame -> _gameState.value = onNewGame()
            is GameAction.SkipWord -> _gameState.value = onSkipWord(state)
            is GameAction.CorrectWord -> _gameState.value = onCorrectWord(state)
            is GameAction.EndGame -> _gameState.value = onEndGame(state)
        }
    }

    private fun onNewGame(): GameState {
        val initialWordList = getNewWordList()
        val word = initialWordList[0]
        val remainingWordList = initialWordList.slice(1 until initialWordList.count())
        val score = 0
        return GameState.InProgress(remainingWordList, word, score)
    }

    private fun onSkipWord(state: GameState): GameState {
        when (state) {
            is GameState.InProgress -> {
                if (state.wordList.count() == 1) return GameState.Complete(state.score)
                val wordList = getUpdatedWordList(state)
                val word = getNextWord(state)
                val score = state.score - 1
                return GameState.InProgress(wordList, word, score)
            }
            is GameState.Complete -> {
                return state
            }
        }
    }

    private fun onCorrectWord(state: GameState): GameState {
        when (state) {
            is GameState.InProgress -> {
                if (state.wordList.count() == 1) return GameState.Complete(state.score)
                val wordList = getUpdatedWordList(state)
                val word = getNextWord(state)
                val score = state.score + 1
                return GameState.InProgress(wordList, word, score)
            }
            is GameState.Complete -> {
                return state
            }
        }
    }

    private fun onEndGame(state: GameState): GameState {
        return when (state) {
            is GameState.InProgress -> GameState.Complete(state.score)
            is GameState.Complete -> state
        }
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun getNewWordList(): MutableList<String> {
        val wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
        return wordList
    }

    private fun getNextWord(state: GameState.InProgress): String {
        return state.wordList[0]
    }

    private fun getUpdatedWordList(state: GameState.InProgress): List<String> {
        return state.wordList.slice(1 until state.wordList.count())
    }
}