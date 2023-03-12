package app.vrabia.vrcommon.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class ApiError {
    @Schema(name = "code", description = "The unique code of the error ", example = "ERR1001", required = true)
    private String code;
    @Schema(name = "message", description = "The error message", example = "At least one of the provided category ids do not exist in the database", required = true)
    private String message;
    @Schema(name = "details", required = true)
    private List<String> details;

    public ApiError(ErrorCodes errorCode) {
        this.details = new ArrayList<>();
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getDefaultErrorMessage();
    }

    public ApiError(String message, ErrorCodes errorCode) {
        this.code = errorCode.getErrorCode();
        this.message = message;
        this.details = new ArrayList<>();
    }

    public ApiError(String message, List<String> details, ErrorCodes errorCode) {
        this.code = errorCode.getErrorCode();
        this.message = message;
        this.details = details;
    }
}
