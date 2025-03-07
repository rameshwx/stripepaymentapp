package com.rameshwx.stripepaymentapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rameshwx.stripepaymentapp.controller.PaymentController
import com.rameshwx.stripepaymentapp.model.Order
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@Composable
fun PaymentScreen(
    paymentController: PaymentController,
    onPaymentComplete: (Boolean, String) -> Unit
) {
    var orderNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Get the payment sheet object
    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> {
                onPaymentComplete(true, "Payment successful!")
            }
            is PaymentSheetResult.Canceled -> {
                onPaymentComplete(false, "Payment canceled")
            }
            is PaymentSheetResult.Failed -> {
                onPaymentComplete(false, "Payment failed: ${result.error.localizedMessage}")
            }
        }
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Stripe Payment Integration",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = orderNumber,
            onValueChange = { orderNumber = it },
            label = { Text("Order Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                errorMessage = ""
                isLoading = true

                try {
                    val amountValue = amount.toDoubleOrNull()

                    if (orderNumber.isBlank()) {
                        errorMessage = "Please enter an order number"
                        isLoading = false
                        return@Button
                    }

                    if (amountValue == null || amountValue <= 0) {
                        errorMessage = "Please enter a valid amount"
                        isLoading = false
                        return@Button
                    }

                    val order = Order(orderNumber, amountValue)

                    paymentController.processPayment(
                        order = order,
                        onSuccess = { response ->
                            response.clientSecret?.let { clientSecret ->
                                val configuration = PaymentSheet.Configuration(
                                    merchantDisplayName = "Rameshwx Payment App"
                                )

                                paymentSheet.presentWithPaymentIntent(
                                    clientSecret,
                                    configuration
                                )
                            } ?: run {
                                errorMessage = "Unable to get client secret"
                                isLoading = false
                            }
                        },
                        onError = { error ->
                            errorMessage = error
                            isLoading = false
                        }
                    )
                } catch (e: Exception) {
                    errorMessage = "Error: ${e.message}"
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Proceed to Payment")
            }
        }
    }
}