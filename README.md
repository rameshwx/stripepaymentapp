# Android Stripe Payment Integration

![Stripe](https://img.shields.io/badge/Stripe-Integration-6772E5)
![Android](https://img.shields.io/badge/Platform-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-orange)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue)

A modern Android application demonstrating secure payment processing with Stripe, built with Kotlin and Jetpack Compose.

## Features

- 💳 Secure payment processing with Stripe
- 🔒 Server-side payment intent creation
- 📱 Modern UI with Jetpack Compose
- 🧩 MVC architecture with Dagger DI
- ⚡ Responsive payment flow

## Tech Stack

- **Frontend**: Android (Kotlin) with Jetpack Compose
- **Architecture**: MVC (Model-View-Controller)
- **Networking**: Google Volley
- **Dependency Injection**: Dagger
- **Payment Processing**: Stripe Android SDK
- **Backend**: PHP endpoint for creating payment intents

## Setup Instructions

### Prerequisites

- Android Studio
- PHP hosting environment with Stripe PHP SDK installed
- Stripe account (for test and live API keys)

### Android Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/stripe-payment-app.git
   cd stripe-payment-app
   ```

2. **Open in Android Studio**

3. **Configure Stripe publishable key**
   Update `Constants.kt` with your Stripe publishable key:
   ```kotlin
   const val STRIPE_PUBLISHABLE_KEY = "pk_test_your_key_here"
   ```

4. **Update API endpoint**
   In `StripeApiService.kt`, set the URL to your PHP backend:
   ```kotlin
   private val PAYMENT_API_URL = "https://yourserver.com/stripe/create-payment-intent.php"
   ```

5. **Build and run the application**

### PHP Backend Setup

1. **Install Stripe PHP SDK via cPanel**
   ```bash
   cd public_html/stripe
   curl -sS https://getcomposer.org/installer | php
   php composer.phar require stripe/stripe-php
   ```

2. **Create payment intent endpoint**
   Create `create-payment-intent.php` with this content:
   ```php
   <?php
   // Set headers for JSON response
   header('Content-Type: application/json');
   header('Access-Control-Allow-Origin: *');
   header('Access-Control-Allow-Methods: POST');
   header('Access-Control-Allow-Headers: Content-Type');

   // Check if it's a POST request
   if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
       http_response_code(405);
       echo json_encode(['error' => 'Method not allowed']);
       exit;
   }

   // Get POST data
   $data = json_decode(file_get_contents('php://input'), true);

   // Validate request data
   if (!isset($data['amount']) || !isset($data['orderNumber'])) {
       http_response_code(400);
       echo json_encode(['error' => 'Missing required parameters']);
       exit;
   }

   // Required Stripe PHP library
   require_once('vendor/autoload.php');

   // Set your secret key
   $stripeSecretKey = 'sk_test_your_key_here';
   \Stripe\Stripe::setApiKey($stripeSecretKey);

   try {
       // Create payment intent
       $paymentIntent = \Stripe\PaymentIntent::create([
           'amount' => round($data['amount'] * 100), // Convert to cents
           'currency' => 'usd',
           'description' => 'Order #' . $data['orderNumber'],
           'automatic_payment_methods' => [
               'enabled' => true,
           ],
       ]);

       // Return client secret
       echo json_encode([
           'clientSecret' => $paymentIntent->client_secret,
           'paymentIntentId' => $paymentIntent->id
       ]);
   } catch (\Exception $e) {
       http_response_code(500);
       echo json_encode(['error' => $e->getMessage()]);
   }
   ?>
   ```

## Project Structure

```
com.rameshwx.stripepaymentapp/
  ├── di/                    # Dependency injection
  │   ├── AppModule.kt
  │   └── AppComponent.kt
  ├── model/                 # Data models
  │   ├── Order.kt
  │   └── PaymentResponse.kt
  ├── controller/            # Business logic
  │   └── PaymentController.kt
  ├── view/                  # UI components
  │   ├── MainActivity.kt
  │   ├── PaymentScreen.kt
  │   └── PaymentResultScreen.kt
  ├── network/               # Volley networking
  │   └── StripeApiService.kt
  ├── util/                  # Utilities
  │   └── Constants.kt
  └── StripePaymentApp.kt    # Application class
```

## Architecture Design

The application follows the MVC architectural pattern:

### Model
Data classes representing payment entities:
```kotlin
data class Order(val orderNumber: String, val amount: Double)
data class PaymentResponse(
    val success: Boolean,
    val message: String,
    val paymentIntentId: String? = null,
    val clientSecret: String? = null
)
```

### View
Jetpack Compose screens for UI rendering:
```kotlin
@Composable
fun PaymentScreen(
    paymentController: PaymentController,
    onPaymentComplete: (Boolean, String) -> Unit
) {
    // UI implementation
}
```

### Controller
Business logic for processing payments:
```kotlin
class PaymentController @Inject constructor(
    private val stripeApiService: StripeApiService
) {
    fun processPayment(
        order: Order,
        onSuccess: (PaymentResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        // Payment processing logic
    }
}
```

## Payment Flow

1. User enters order number and amount
2. App validates input
3. Request sent to PHP backend
4. Backend creates Stripe payment intent
5. Client secret returned to app
6. App presents Stripe payment sheet
7. User completes payment
8. Result screen displays success/failure

## Test Cards

For testing payments in the app, use Stripe's test cards:

| Card Number | Scenario |
|-------------|----------|
| 4242 4242 4242 4242 | Successful payment |
| 4000 0025 0000 3155 | Requires authentication |
| 4000 0000 0000 9995 | Payment declined |

## Security Considerations

- Client authentication should be implemented in production
- HTTPS is required for production deployments
- API rate limiting should be implemented on the backend
- Server should validate payment amounts and order data

## Screenshots

*Coming soon*

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- [Stripe API Documentation](https://stripe.com/docs/api)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)

---

**Note:** Replace placeholder API keys with your actual Stripe test API keys. Never include production API keys in source code.
```
