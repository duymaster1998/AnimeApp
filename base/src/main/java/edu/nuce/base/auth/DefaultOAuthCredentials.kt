package edu.nuce.base.auth

/**
 * Default [OAuthCredentials] implementation.
 */
data class DefaultOAuthCredentials(
    override val accessToken: String,
    override val refreshToken: String,
    override val expiresIn: Long? = null
) : OAuthCredentials