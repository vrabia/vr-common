package app.vrabia.vrcommon.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class VrabiaExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(value = {VrabiaException.class})
    protected ResponseEntity<Object> handleVrabiaException(VrabiaException ex, WebRequest request) {
        log.info("Handling VrabiaException: {}", ex.getErrorCode());
        ex.printStackTrace();
        HttpStatus httpErrorCode;
        switch (ex.getErrorCode()) {
            case BAD_REQUEST:
            case INVALID_PARAMETERS:
            case MISSING_QUERY_PARAM:
            case REQUEST_METHOD_NOT_SUPPORTED:
            case UNIQUE_EMAIL:
                httpErrorCode = HttpStatus.BAD_REQUEST;
                break;
            case UNAUTHORIZED:
            case MISSING_AUTHORIZATION_HEADER:
            case TOKEN_EXPIRED:
            case TOKEN_INVALID:
            case INVALID_CREDENTIALS:
                httpErrorCode = HttpStatus.UNAUTHORIZED;
                break;
            default:
                httpErrorCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return handleException(ex, httpErrorCode, request);
    }

    @ExceptionHandler(value = {Throwable.class})
    protected ResponseEntity<Object> handleThrowable(Exception ex, WebRequest request) {
        log.info("Handling Throwable: {}", ex.getMessage());
        ex.printStackTrace();
        return handleExceptionInternal(ex, ErrorCodes.INTERNAL_SERVER_ERROR, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles a {@link VrabiaException}.
     *
     * @param ex         - the raised exception.
     * @param httpStatus - the http status of the response.
     * @param request    - the original request.
     * @return a wrapper over an {@link ErrorCodes} object containing the error response.
     */
    private ResponseEntity<Object> handleException(VrabiaException ex, HttpStatus httpStatus, WebRequest request) {
        ApiError apiError = new ApiError(extractErrorMessage(ex.getErrorCode(), ex.getErrorArgs()), ex.getErrorCode());
        log.error("Handling ApiError: {}, {}", apiError.getCode(), apiError.getMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), httpStatus, request);
    }

    private String extractErrorMessage(ErrorCodes errorCode, List<String> args) {
        try {
            return messageSource.getMessage(errorCode.getErrorCode(), args.toArray(), LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException ex) {
            return new MessageFormat(errorCode.getDefaultErrorMessage()).format(args.toArray());
        }
    }
}
