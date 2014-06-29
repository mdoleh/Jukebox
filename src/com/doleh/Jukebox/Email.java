package com.doleh.Jukebox;

import com.hardiktrivedi.Exception.ExceptionHandler;
import com.jon.Mail.Mail;

public class Email
{
    private final static String APPLICATION_PREFIX = "Jukebox - ";

    public static void sendErrorReport(Throwable ex)
    {
        startEmailThread("Error Report - Exception", ExceptionHandler.generateReport(ex));
    }

    private static void startEmailThread(String errorMessage, String stackTrace)
    {
        try
        {
            Thread email = new Thread(new EmailThread(errorMessage, stackTrace));
            email.start();
            email.join();
        }
        catch (InterruptedException e)
        {
            // ignore exceptions
        }
    }

    private static void sendEmail(String subject, String body)
    {
        Mail m = new Mail("dammahom59@gmail.com", "probablydarkmonkeyglobe");

        String[] toArr = {"dammahom59@gmail.com"};
        m.setTo(toArr);
        m.setFrom("noreply@jukebox.com");
        m.setSubject(APPLICATION_PREFIX + subject);
        m.setBody(subject + "\n\n" + body);

        try {
            m.send();
        } catch(Exception e) {
            // email failed to send
        }
    }

    private static class EmailThread implements Runnable
    {
        private String _errorMessage;
        private String _stackTrace;

        public EmailThread(String errorMessage, String stackTrace)
        {
            _errorMessage = errorMessage;
            _stackTrace = stackTrace;
        }

        @Override
        public void run()
        {
            sendEmail(_errorMessage, _stackTrace);
        }
    }
}
