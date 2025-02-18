package com.esprit.controllers.User;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    public static void envoyerEmail(String to, String subject, String body) {
        // Configuration SMTP pour ESPrit
        String host = "smtp.gmail.com"; // Remplacez par le serveur SMTP de l'ESPrit
        String username = "ahmedbrahmi1984@gmail.com"; // Votre adresse e-mail ESPrit
        String password = "zttf ykuz vsoo gdlo"; // Votre mot de passe ESPrit

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587"); // Port SMTP (587 pour TLS)
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Activer TLS
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.debug", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email envoyé avec succès!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void envoyerEmail(String destinataire, String contenu) {
        envoyerEmail(destinataire, "Code de vérification", "Votre code de vérification est : " + contenu);
    }
}