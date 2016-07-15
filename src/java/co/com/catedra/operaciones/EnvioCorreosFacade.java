/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.catedra.operaciones;

import co.com.catedra.eo.Asignatura;
import co.com.catedra.eo.Curso;
import java.sql.Time;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author sebastianPc
 */
@Stateless
public class EnvioCorreosFacade {

    public void SEND(String para,String usuario,String contaseña,String rol) 
    {
        try
        {
            // Propiedades de la conexión
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "catedrasena679470@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("catedrasena679470@gmail.com"));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(para));
            message.setSubject("Bienvenido a catedra");
            message.setText(
                "Usted ha sido registrado en el aplicativo catedra con los siguientes datos "+"\n"
                        +"Su rol es: "+rol+"\n"
                        + "NOMBRE USUARIO: "+usuario+"\n"
                        + "CONTRASEÑA: "+contaseña);

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect("catedrasena679470@gmail.com", "catedra679470");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       
    }
    
    
    public void SENDNewClass(String para,String dia,Curso curso,Time hora,Asignatura asignatura ) 
    {
        try
        {
            // Propiedades de la conexión
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "catedrasena679470@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("catedrasena679470@gmail.com"));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(para));
            message.setSubject("Bienvenido a catedra");
            message.setText(
                "Se le imforma cordialmente que se le asigno una nueva clase "+"\n"
                        +"curso: "+curso.getNombre()+"\n"
                        + "Dia: "+dia+"\n"
                        + "hora: "+hora+"\n"
                        +"asignatura"+asignatura.getNomAsignatura());

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect("catedrasena679470@gmail.com", "catedra679470");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       
    }
}


