package com.example.android.guesstheword

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@PublishedApi
internal class SharedFlowLifecycleObserver<T> (
        lifecycleOwner: LifecycleOwner,
        private val flow: Flow<T>,
        private val collector: suspend (T) -> Unit
) : DefaultLifecycleObserver {

    private var job: Job? = null

    @InternalCoroutinesApi
    override fun onStart(owner: LifecycleOwner) {
        job = owner.lifecycleScope.launch {
            flow.collect(object : FlowCollector<T> {
                override suspend fun emit(value: T) {
                    collector(value)
                }
            })
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        job?.cancel()
        job = null
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }
}

inline fun <reified T> Flow<T>.observe(
        lifecycleOwner: LifecycleOwner,
        noinline collector: suspend (T) -> Unit
) {
    SharedFlowLifecycleObserver(lifecycleOwner, this, collector)
}

inline fun <reified T> Flow<T>.observeIn(
        lifecycleOwner: LifecycleOwner
) {
    SharedFlowLifecycleObserver(lifecycleOwner, this, {})
}
