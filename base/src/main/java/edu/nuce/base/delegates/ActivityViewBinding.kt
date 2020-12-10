package edu.nuce.base.delegates

import android.app.Activity
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

private class ActivityViewBindingProperty<A : ComponentActivity, T : ViewBinding>(
    viewBinder: (A) -> T
) : ViewBindingProperty<A, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: A): LifecycleOwner = thisRef

}

/**
 * Create new [ViewBinding] associated with the [Activity][ComponentActivity] and allow customize how
 * a [View] will be bounded to the view binding.
 */
@JvmName("viewBindingActivity")
public fun <A : ComponentActivity, T : ViewBinding> ComponentActivity.viewBinding(
    viewBinder: (A) -> T
): ViewBindingProperty<A, T> {
    return ActivityViewBindingProperty(viewBinder)
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal class ActivityViewBinder<T : ViewBinding>(
    private val viewBindingClass: Class<T>,
    private val viewProvider: (Activity) -> View
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
    fun bind(activity: Activity): T {
        val view = viewProvider(activity)
        return bindViewMethod(null, view) as T
    }
}

@JvmName("viewBindingActivity")
public inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<ComponentActivity, T> {
    val activityViewBinder =
        ActivityViewBinder(T::class.java) { ActivityCompat.requireViewById(this, viewBindingRootId) }
    return viewBinding(activityViewBinder::bind)
}