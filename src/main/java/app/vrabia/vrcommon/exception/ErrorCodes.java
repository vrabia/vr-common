package app.vrabia.vrcommon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodes {
    // Request validation errors
    BAD_REQUEST("ERR_41", "Bad request"),
    INVALID_PARAMETERS("ER_42", "Invalid parameters"),
    MISSING_QUERY_PARAM("ERR_43", "Missing query param: {0}"),
    REQUEST_METHOD_NOT_SUPPORTED("ERR_44", "Request method {0} not supported"),

    // Authorization errors
    UNAUTHORIZED("ERR_45", "Unauthorized"),
    MISSING_AUTHORIZATION_HEADER("ERR_46", "Missing authorization header"),
    TOKEN_EXPIRED("ERR_47", "Token has expired"),
    TOKEN_INVALID("ERR_48", "Token is invalid"),
    INVALID_CREDENTIALS("ERR_49", "Invalid credentials"),
    UNIQUE_EMAIL("ERR_410", "Email already exists"),

    // Internal error
    INTERNAL_SERVER_ERROR("ERR_51", "Something unexpected happened");

    private final String errorCode;
    private final String defaultErrorMessage;
}
