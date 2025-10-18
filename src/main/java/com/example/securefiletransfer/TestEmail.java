package com.example.securefiletransfer;

public class TestEmail {
    public static void main(String[] args) {
        // Replace with the email you want to send the OTP to
        String recipient = "simplparadox.spam@gmail.com"; 
        String subject = "Test OTP";
        String message = "Hello! This is a test OTP.";

        // Call your EmailSender class
        EmailSender.sendEmail(recipient, subject, message);
    }
}
