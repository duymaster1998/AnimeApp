package edu.nuce.base.extensions

import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Invoke the listener when afterTextChanged() method is called.
 *
 * @return TextWatcher remember to remove when no longer use it anymore.
 */
fun EditText.textChanged(listener: (String) -> Unit): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener.invoke(s?.toString() ?: "")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
    this.addTextChangedListener(textWatcher)
    return textWatcher
}

fun EditText.textChanged(): Observable<out CharSequence> {
    val source: PublishSubject<String> = PublishSubject.create()
    val textWatcher = TextChangeWatcher(source)
    addTextChangedListener(textWatcher)
    return source.doOnDispose {
        textWatcher.unsubscribe()
        removeTextChangedListener(textWatcher)
    }
}

private class TextChangeWatcher(private var publisher: PublishSubject<String>? = null) :
    TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        publisher?.onNext(s?.toString() ?: "")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    fun unsubscribe() {
        publisher = null
    }
}

/**
 * Validate the input text and display error when it's invalid.
 * @param [errorMessage] error message to display
 * @param [validator] the predicate to validate input.
 *  @return TextWatcher remember to remove when no longer use it anymore.
 */
fun EditText.validate(errorMessage: String, validator: (String) -> Boolean): TextWatcher {
    fun showErrorIfInvalid(s: String) {
        this.error = if (validator(s)) null else errorMessage
    }
    // validate original text
    showErrorIfInvalid(this.text.toString())
    // validate changed text
    return this.textChanged { showErrorIfInvalid(it) }
}

fun EditText.showPassword() {
    transformationMethod = HideReturnsTransformationMethod.getInstance()
    selectAll()
}

fun EditText.hidePassword() {
    transformationMethod = PasswordTransformationMethod.getInstance()
    selectAll()
}