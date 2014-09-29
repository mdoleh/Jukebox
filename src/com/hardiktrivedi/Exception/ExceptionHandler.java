package com.hardiktrivedi.Exception;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import com.doleh.Jukebox.Static.Config;
import com.doleh.Jukebox.Static.Email;
import com.doleh.Jukebox.Static.MessageDialog;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final static String LINE_SEPARATOR = "\n";
    private Activity _mainActivity;

    public ExceptionHandler(Activity mainActivity)
    {
        _mainActivity = mainActivity;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Email.sendErrorReport(exception);
        Log.e(Config.APP_TITLE, exception.getMessage(), exception);

        MessageDialog.showErrorMessage(_mainActivity);
    }

    public static String generateReport(Throwable exception)
    {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        return errorReport.toString();
    }
}
