package com.example.melichallenge.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed class WelcomeUIEvent {
    class OnSearchCorrect(val query: String): WelcomeUIEvent()
    object OnSearchFailure: WelcomeUIEvent()
}

abstract class WelcomeViewModel: ViewModel() {
    abstract val eventsFlow: Flow<WelcomeUIEvent>
    abstract var query: String
    abstract fun onSearchButtonClicked()
}

internal class WelcomeViewModelImpl: WelcomeViewModel() {

    override var query: String = ""

    private val eventChannel = Channel<WelcomeUIEvent>(Channel.BUFFERED)
    override val eventsFlow = eventChannel.receiveAsFlow()

    override fun onSearchButtonClicked() {
        viewModelScope.launch {
            if(query.isNotEmpty() && query.isNotBlank()) {
                eventChannel.send(WelcomeUIEvent.OnSearchCorrect(query))
            } else {
                eventChannel.send(WelcomeUIEvent.OnSearchFailure)
            }
        }
    }
}