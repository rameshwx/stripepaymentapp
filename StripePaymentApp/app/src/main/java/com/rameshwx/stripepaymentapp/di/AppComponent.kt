package com.rameshwx.stripepaymentapp.di

import android.app.Application
import com.rameshwx.stripepaymentapp.controller.PaymentController
import com.rameshwx.stripepaymentapp.view.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun paymentController(): PaymentController

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}