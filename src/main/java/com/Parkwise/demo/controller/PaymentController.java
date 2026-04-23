package com.parkwise.demo.controller;

import com.parkwise.demo.service.EmailService;
import com.parkwise.demo.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;
    private final EmailService emailService;

    public PaymentController(PaymentService paymentService, EmailService emailService) {
        this.paymentService = paymentService;
        this.emailService = emailService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> data) {
        try {
            int amount = Integer.parseInt(data.get("amount").toString());
            String orderJson = paymentService.createRazorpayOrder(amount);
            return ResponseEntity.ok(orderJson);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\":\"Failed to create order\"}");
        }
    }

    @PostMapping("/verify-and-book")
    public ResponseEntity<String> verifyPaymentAndBook(@RequestBody Map<String, Object> data) {
        String razorpayOrderId = data.get("razorpayOrderId").toString();
        String razorpayPaymentId = data.get("razorpayPaymentId").toString();
        String razorpaySignature = data.get("razorpaySignature").toString();

        boolean isValid = paymentService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

        if (isValid) {
            try {
                String email = data.get("email").toString();
                String location = data.get("location").toString();
                String slot = data.get("slot").toString();
                String from = data.get("from").toString();
                String to = data.get("to").toString();
                String carNumber = data.get("carNumber").toString();
                String amount = data.get("amount").toString();

                emailService.sendBookingInvoice(email, location, slot, from, to, carNumber, amount, razorpayPaymentId);
                
                return ResponseEntity.ok("{\"status\":\"SUCCESS\"}");
            } catch (Exception e) {
                return ResponseEntity.ok("{\"status\":\"SUCCESS_EMAIL_FAILED\"}");
            }
        } else {
            return ResponseEntity.badRequest().body("{\"status\":\"FAILED\"}");
        }
    }
}