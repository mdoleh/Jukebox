package com.doleh.Jukebox;

import com.hardiktrivedi.Exception.ExceptionHandler;
import com.jon.Mail.Mail;
import com.snippets.Utils.AES;

public class Email
{
    private final static String APPLICATION_PREFIX = "Jukebox - ";
    private final static String SENDER_EMAIL = "jC+/N9ViMuHV/hhr5M2qJenGv6IX3u9Var3ANU8INyg=\n";
    private final static String SENDER_PASS = "UXTxMqg1u08trcX7GOo1jmzdwtqUld51BcxSdKDaPpY=\n";

    public static void sendErrorReport(Throwable ex)
    {
        startEmailThread("Error Report - Exception", ExceptionHandler.generateReport(ex));
    }

    public static void startEmailThread(String subject, String body)
    {
        try
        {
            Thread email = new Thread(new EmailThread(subject, body));
            email.start();
            email.join();
        }
        catch (InterruptedException e)
        {
            // ignore exceptions
        }
    }

    private static void sendEmail(String subject, String body) throws Exception
    {
        String email = AES.decrypt(SENDER_EMAIL);
        Mail m = new Mail(email, AES.decrypt(SENDER_PASS));

        String[] toArr = {email};
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
            try
            {
                sendEmail(_errorMessage, _stackTrace);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
