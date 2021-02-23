package mock.com.craftedbeer.data.api;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;

public class ErrorUtils {
    public static String parseThrowable(Throwable throwable) {
        String message = null;

        if (throwable instanceof IOException) {
            message = ErrorConstants.NETWORK_ERROR;

        } else if (throwable instanceof HttpException) {
            message = ErrorConstants.STATUS_UNAUTHORIZED;
        }
        return message;

    }
}
