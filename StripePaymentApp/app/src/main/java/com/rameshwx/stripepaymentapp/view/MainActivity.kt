package com.rameshwx.stripepaymentapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rameshwx.stripepaymentapp.StripePaymentApp
import com.rameshwx.stripepaymentapp.controller.PaymentController
import com.stripe.android.PaymentConfiguration
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var paymentController: PaymentController

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as StripePaymentApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        PaymentConfiguration.init(
            applicationContext,
            com.rameshwx.stripepaymentapp.util.Constants.STRIPE_PUBLISHABLE_KEY
        )

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StripePaymentApp(paymentController)
                }
            }
        }
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Composable
fun StripePaymentApp(paymentController: PaymentController) {
    val showPaymentResult = remember { mutableStateOf(false) }
    val paymentSuccess = remember { mutableStateOf(false) }
    val resultMessage = remember { mutableStateOf("") }

    if (showPaymentResult.value) {
        PaymentResultScreen(
            success = paymentSuccess.value,
            message = resultMessage.value,
            onDone = {
                showPaymentResult.value = false
            }
        )
    } else {
        PaymentScreen(
            paymentController = paymentController,
            onPaymentComplete = { success, message ->
                paymentSuccess.value = success
                resultMessage.value = message
                showPaymentResult.value = true
            }
        )
    }
}