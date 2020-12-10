package edu.nuce.base.auth

/**
 * Interface for checking if provided error indicates invalid tokens.
 */
interface ErrorChecker {

    fun invalidAccessToken(t: Throwable): Boolean

    fun invalidRefreshToken(t: Throwable): Boolean

}