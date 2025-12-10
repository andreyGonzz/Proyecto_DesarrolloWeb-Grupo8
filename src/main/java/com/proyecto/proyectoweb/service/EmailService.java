package com.proyecto.proyectoweb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String defaultFromEmail;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendContactEmail(String to, String name, String email, 
                                String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            
            mailMessage.setFrom(defaultFromEmail);
            mailMessage.setTo(to);
            mailMessage.setSubject("📧 Nuevo mensaje de contacto: " + subject);
            mailMessage.setText(
                "📬 **NUEVO MENSAJE DE CONTACTO**\n\n" +
                "👤 **Nombre:** " + name + "\n" +
                "📧 **Email:** " + email + "\n" +
                "📝 **Asunto:** " + subject + "\n\n" +
                "💬 **Mensaje:**\n" + message + "\n\n" +
                "---\n" +
                "🕐 Fecha: " + java.time.LocalDateTime.now() + "\n" +
                "🌐 Enviado desde el formulario de contacto"
            );
            
            mailSender.send(mailMessage);
            log.info("📤 Email enviado a administrador: {}", to);
            
        } catch (Exception e) {
            log.error("❌ Error al enviar email a administrador: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo enviar el email: " + e.getMessage(), e);
        }
    }
    
    public void sendConfirmationToUser(String userEmail, String name) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            
            mailMessage.setFrom(defaultFromEmail);
            mailMessage.setTo(userEmail);
            mailMessage.setSubject("✅ Confirmación de recepción de mensaje");
            mailMessage.setText(
                "Hola " + name + ",\n\n" +
                "✅ **Hemos recibido tu mensaje correctamente.**\n\n" +
                "Nuestro equipo revisará tu consulta y te contactará pronto.\n\n" +
                "⌛ **Tiempo de respuesta estimado:** 24-48 horas laborales\n\n" +
                "Gracias por contactarnos.\n\n" +
                "Saludos cordiales,\n" +
                "📧 **El equipo de soporte**\n" +
                "---\n" +
                "Este es un mensaje automático, por favor no respondas a este email."
            );
            
            mailSender.send(mailMessage);
            log.info("📤 Email de confirmación enviado a usuario: {}", userEmail);
            
        } catch (Exception e) {
            log.error("❌ Error al enviar email de confirmación: {}", e.getMessage(), e);
        }
    }
}