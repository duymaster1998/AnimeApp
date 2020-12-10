package edu.nuce.base.view.viewmodels

data class ApiResponse<T> constructor(
    private val isLoading: Boolean = false,
    private val error: String? = null,
    private val data: T? = null
) {
    companion object {
        fun <T> loading(data: T?) = ApiResponse(isLoading = true, data = data, error = "")
        fun <T> success(data: T) = ApiResponse(isLoading = false, data = data, error = "")
        fun <T> error(error: Throwable, data: T?) = ApiResponse(isLoading = false, data = data, error = error.message)
        fun <T> uninitialized() = ApiResponse<T>()
    }

    fun isLoading() = isLoading
    fun isError() = error != null && error.isNotBlank()
    fun isSuccess() = data != null && !isError()

    fun getError(): String? {
        if (error != null) {
            return error
        }
        return null
    }

    fun getData(): T? {
        if (data != null) {
            return data
        }
        return null
    }
}