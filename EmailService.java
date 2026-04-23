package com.parkwise.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendBookingInvoice(String toEmail, String location, String slot, String from, String to, String car, String amount, String txnId) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(senderEmail);
        helper.setTo(toEmail);
        helper.setSubject("Park Wise - Booking Confirmation & Invoice");

        String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; padding: 20px; border-radius: 10px;'>" +
                "<h2 style='color: #38bdf8; text-align: center;'>Park Wise Confirmation</h2>" +
                "<p>Your parking slot has been successfully reserved. Here is your invoice:</p>" +
                "<table style='width: 100%; border-collapse: collapse;'>" +
                "<tr style='background: #f8fafc;'><td style='padding: 10px; border: 1px solid #ddd;'><b>Transaction ID</b></td><td style='padding: 10px; border: 1px solid #ddd;'>" + txnId + "</td></tr>" +
                "<tr><td style='padding: 10px; border: 1px solid #ddd;'><b>Location</b></td><td style='padding: 10px; border: 1px solid #ddd;'>" + location + "</td></tr>" +
                "<tr style='background: #f8fafc;'><td style='padding: 10px; border: 1px solid #ddd;'><b>Slot</b></td><td style='padding: 10px; border: 1px solid #ddd;'>" + slot + "</td></tr>" +
                "<tr><td style='padding: 10px; border: 1px solid #ddd;'><b>Car Number</b></td><td style='padding: 10px; border: 1px solid #ddd;'>" + car + "</td></tr>" +
                "<tr style='background: #f8fafc;'><td style='padding: 10px; border: 1px solid #ddd;'><b>Start Time</b></td><td style='padding: 10px; border: 1px solid #ddd;'>" + from + "</td></tr>" +
                "<tr><td style='padding: 10px; border: 1px solid #ddd;'><b>End Time</b></td><td style='padding: 10px; border: 1px solid #ddd;'>" + to + "</td></tr>" +
                "<tr style='background: #38bdf8; color: white;'><td style='padding: 10px; border: 1px solid #ddd;'><b>Total Paid</b></td><td style='padding: 10px; border: 1px solid #ddd;'><b>₹" + amount + "</b></td></tr>" +
                "</table>" +
                "<p style='text-align: center; color: #64748b; font-size: 12px; margin-top: 20px;'>Show this when u reach the location to person there</p>"+
                "<p style='text-align: center; color: #64748b; font-size: 12px; margin-top: 20px;'>Thank you for choosing Park Wise.</p>" +
                "</div>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}