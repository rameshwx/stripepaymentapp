package com.rameshwx.stripepaymentapp

import android.app.Application
import com.rameshwx.stripepaymentapp.di.AppComponent
import com.rameshwx.stripepaymentapp.di.DaggerAppComponent

class StripePaymentApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}