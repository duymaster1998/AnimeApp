package edu.nuce.base.delegates

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

private class DialogFragmentViewBindingProperty<F: DialogFragment, T: ViewBinding>(
    viewBinder: (F) -> T
): ViewBindingProperty<F, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        return if (thisRef.view == null) thisRef.viewLifecycleOwner else thisRef
    }

}

/**
 * Create new [ViewBinding] associated with the [DialogFragment]
 */
@JvmName("viewBindingDialogFragment")
public fun <F : DialogFragment, T : ViewBinding> DialogFragment.dialogViewBinding(
    viewBinder: (F) -> T
): ViewBindingProperty<F, T> {
    return DialogFragmentViewBindingProperty(viewBinder)
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal class DialogFragmentViewBinder<T : ViewBinding>(
    private val viewBindingClass: Class<T>,
    @IdRes private val viewBindingRootId: Int = 0
) {

    /**
     * Cache static method `ViewBinding.bind(View)`
     */
    private val bindViewMethod by lazy(LazyThreadSafetyMode.NONE) {
        viewBindingClass.getMethod("bind", View::class.java)
    }

    /**
     * Create new [ViewBinding] instance
     */
    @Suppress("UNCHECKED_CAST")
    fun bind(fragment: DialogFragment): T {
        return bindViewMethod(null, getRootView(fragment)) as T
    }

    private fun getRootView(fragment: DialogFragment): View {
        val dialog = checkNotNull(fragment.dialog) { "Dialog hasn't been created yet" }
        val window = checkNotNull(dialog.window) { "Dialog has no window" }
        if (viewBindingRootId != 0) {
            return window.decorView.findViewById(viewBindingRootId)
        } else {
            return window.decorView
        }
    }
}

/**
 * Create new [ViewBinding] associated with the [DialogFragment]'s view
 *
 * @param viewBindingRootId Id of the root view from your custom view
 */
@JvmName("viewBindingDialogFragment")
public inline fun <reified T : ViewBinding> DialogFragment.dialogViewBinding(
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<DialogFragment, T> {
    return dialogViewBinding(DialogFragmentViewBinder(T::class.java, viewBindingRootId)::bind)
}