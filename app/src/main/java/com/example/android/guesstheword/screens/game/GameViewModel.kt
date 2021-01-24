package com.example.android.guesstheword.screens.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow<GameState>(onNewGame())
    val gameState: StateFlow<GameState> = _gameState

    fun handleGameAction(state: GameState, action: GameAction) {
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
        return GameState(GameStatus.InProgress, remainingWordList, word, score)
    }

    private fun onSkipWord(state: GameState): GameState {
        val newWordState = getNextWord(state)
        return GameState(newWordState.status, newWordState.wordList, newWordState.word, newWordState.score - 1)
    }

    private fun onCorrectWord(state: GameState): GameState {
        val newWordState = getNextWord(state)
        return GameState(newWordState.status, newWordState.wordList, newWordState.word, newWordState.score + 1)
    }

    private fun onEndGame(state: GameState): GameState {
        return GameState(GameStatus.Complete, emptyList(), "", state.score)
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

    private fun getNextWord(state: GameState): GameState {
        if (state.wordList.count() == 1) {
            return GameState(GameStatus.Complete, emptyList(), "", state.score)
        }
        val word = state.wordList[0]
        val remainingWordList = state.wordList.slice(1 until state.wordList.count())
        return GameState(GameStatus.InProgress, remainingWordList, word, state.score)
    }
}