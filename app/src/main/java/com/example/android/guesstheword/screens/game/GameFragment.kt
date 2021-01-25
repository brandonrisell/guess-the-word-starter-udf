package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import com.example.android.guesstheword.screens.title.TitleFragmentDirections
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Fragment where the game is played
 */
@ExperimentalCoroutinesApi
class GameFragment : Fragment(R.layout.game_fragment) {
    private var gameStateUpdates: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewBinding = GameFragmentBinding.bind(view)
        val viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        gameStateUpdates = lifecycleScope.launchWhenStarted {
            viewModel.gameState.collect { state ->
                when (state) {
                    is GameState.Complete -> {
                        findNavController().navigate(GameFragmentDirections.actionGameToScore(state.score))
                    }
                    is GameState.InProgress -> {
                        viewBinding.wordText.text = state.word
                        viewBinding.scoreText.text = state.score.toString()
                        viewBinding.correctButton.setOnClickListener { viewModel.handleGameAction(GameAction.CorrectWord, state) }
                        viewBinding.skipButton.setOnClickListener { viewModel.handleGameAction(GameAction.SkipWord, state) }
                        viewBinding.endGameButton.setOnClickListener { viewModel.handleGameAction(GameAction.EndGame, state) }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        gameStateUpdates?.cancel()
        gameStateUpdates = null
        super.onDestroyView()
    }
}
