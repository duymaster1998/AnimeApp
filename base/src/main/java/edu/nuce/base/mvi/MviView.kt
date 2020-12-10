package edu.nuce.base.mvi

import io.reactivex.rxjava3.core.Observable

interface MviView<I: MviIntent, in S: MviViewState> {

    fun intents(): Observable<I>

    fun render(state: S)

}