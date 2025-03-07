package com.rameshwx.stripepaymentapp.di

import android.app.Application
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.rameshwx.stripepaymentapp.network.StripeApiService
import com.stripe.android.PaymentConfiguration
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideRequestQueue(context: Context): RequestQueue = Volley.newRequestQueue(context)

    @Provides
    @Singleton
    fun provideStripeApiService(requestQueue: RequestQueue): StripeApiService =
        StripeApiService(requestQueue)

    @Provides
    @Singleton
    fun provideStripeConfiguration(context: Context): PaymentConfiguration {
        // Initialize Stripe publishable key
        PaymentConfiguration.init(context, "pk_test_51ImhUEGRdQfhxaSJOCyltUeJL5VtoBPxc8ysIqJdxx6tCsIBXXIH2y42BtVMLIW0iYrVIufFY3XL6ZmPAXzbqHHx00NXlqbFjZ")
        return PaymentConfiguration.getInstance(context)
    }
}