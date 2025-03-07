package com.rameshwx.stripepaymentapp.controller

import com.rameshwx.stripepaymentapp.model.Order
import com.rameshwx.stripepaymentapp.model.PaymentResponse
import com.rameshwx.stripepaymentapp.network.StripeApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentController @Inject constructor(
    private val stripeApiService: StripeApiService
) {
    fun processPayment(
        order: Order,
        onSuccess: (PaymentResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        // Validations
        if (order.orderNumber.isBlank()) {
            onError("Order number cannot be empty")
            return
        }

        if (order.amount <= 0) {
            onError("Amount must be greater than zero")
            return
        }

        //payment intent
        stripeApiService.createPaymentIntent(
            order = order,
            onResponse = { response ->
                onSuccess(response)
            },
            onError = { errorMessage ->
                onError(errorMessage)
            }
        )
    }
}