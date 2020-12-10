//package edu.nuce.cinema.di
//
//import dagger.hilt.DefineComponent
//import dagger.hilt.android.components.ApplicationComponent
//import edu.nuce.base.auth.OAuthCredentials
//
//@LoggedUserScope
//@DefineComponent(parent = ApplicationComponent::class)
//interface UserSessionComponent {
//
//    @DefineComponent.Builder
//    interface Builder {
//        fun setCredentials(oAuthCredentials: OAuthCredentials): Builder
//        fun build(): UserSessionComponent
//    }
//
//}