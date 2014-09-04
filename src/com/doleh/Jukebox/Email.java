package com.doleh.Jukebox;

import android.util.Base64;
import com.hardiktrivedi.Exception.ExceptionHandler;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class Email
{
    public static String APP_TITLE;
    private static String APPLICATION_PREFIX = APP_TITLE + " - ";

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
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost request = new HttpPost("https://api.mailgun.net/v2/sandbox222da1e2f56641cc8a3c988ebc09fe3f.mailgun.org/messages");
        String auth =new String(Base64.encode(( "api" + ":" + "key-d0d5950ae6a02b975efaf6a4f99093d4").getBytes(),Base64.URL_SAFE| Base64.NO_WRAP));
        request.addHeader("Authorization", "Basic " + auth);
        ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("from", APP_TITLE + " <postmaster@sandbox222da1e2f56641cc8a3c988ebc09fe3f.mailgun.org>"));
        data.add(new BasicNameValuePair("to", "doleh.jukebox@gmail.com"));
        data.add(new BasicNameValuePair("subject", APPLICATION_PREFIX + subject));
        data.add(new BasicNameValuePair("text", body));
        request.setEntity(new UrlEncodedFormEntity(data));
        HttpResponse response = httpclient.execute(request);
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() != HttpStatus.SC_OK){
            response.getEntity().getContent().close();
        }
    }

    private static class EmailThread implements Runnable
    {
        private String _subject;
        private String _body;

        public EmailThread(String subject, String body)
        {
            _subject = subject;
            _body = body;
        }

        @Override
        public void run()
        {
            try
            {
                sendEmail(_subject, _body);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
