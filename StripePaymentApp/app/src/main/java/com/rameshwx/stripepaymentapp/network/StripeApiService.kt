package com.rameshwx.stripepaymentapp.network

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.rameshwx.stripepaymentapp.model.Order
import com.rameshwx.stripepaymentapp.model.PaymentResponse
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StripeApiService @Inject constructor(
    private val requestQueue: RequestQueue
) {
    private val PAYMENT_API_URL = "https://testsite.com/stripe/create-payment-intent.php"

    fun createPaymentIntent(
        order: Order,
        onResponse: (PaymentResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        // Create JSON payload
        val requestData = JSONObject().apply {
            put("amount", order.amount)
            put("orderNumber", order.orderNumber)
        }

        // Make Volley request to your PHP endpoint
        val request = JsonObjectRequest(
            Request.Method.POST,
            PAYMENT_API_URL,
            requestData,
            { response ->
                try {
                    val clientSecret = response.getString("clientSecret")
                    val paymentIntentId = response.getString("paymentIntentId")

                    onResponse(
                        PaymentResponse(
                            success = true,
                            message = "Payment intent created successfully",
                            paymentIntentId = paymentIntentId,
                            clientSecret = clientSecret
                        )
                    )
                } catch (e: Exception) {
                    onError("Error parsing response: ${e.message}")
                }
            },
            { error ->
                onError("Network error: ${error.message ?: "Unknown error"}")
                print(error.message)
            }
        )

        requestQueue.add(request)
    }
}