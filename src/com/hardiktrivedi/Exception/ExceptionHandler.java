package com.hardiktrivedi.Exception;

import android.os.Build;
import com.doleh.Jukebox.Email;
import com.doleh.Jukebox.MainActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final static String LINE_SEPARATOR = "\n";
    private MainActivity _mainActivity;

    public ExceptionHandler(MainActivity mainActivity)
    {
        _mainActivity = mainActivity;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Email.sendErrorReport(exception);

        _mainActivity.showErrorMessage();
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
