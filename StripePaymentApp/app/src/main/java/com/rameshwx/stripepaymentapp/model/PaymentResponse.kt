package com.rameshwx.stripepaymentapp.model

data class PaymentResponse(
    val success: Boolean,
    val message: String,
    val paymentIntentId: String? = null,
    val clientSecret: String? = null,
    val error: String? = null
)