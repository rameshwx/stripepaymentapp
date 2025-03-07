<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['error' => 'Method not allowed']);
    exit;
}

// Get POST 
$data = json_decode(file_get_contents('php://input'), true);

// Validate request 
if (!isset($data['amount']) || !isset($data['orderNumber'])) {
    http_response_code(400);
    echo json_encode(['error' => 'Missing required parameters']);
    exit;
}

require_once('autoload.php');

// secret key
$stripeSecretKey = 'sk_test_51ImhUEGRdQfhxaSJxvKteyRPsuhnXJiXyShRYdZHFS6C0n8g0OJmnWtWweIvI7utOqS9o6Os16WE2hVy6Q4k0pnq00lXiv98uc';
\Stripe\Stripe::setApiKey($stripeSecretKey);

try {
    // Create payment intent
    $paymentIntent = \Stripe\PaymentIntent::create([
        'amount' => round($data['amount'] * 100), 
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