package app.vrabia.vrcommon.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VrabiaException extends RuntimeException {
    private final ErrorCodes errorCode;
    private final List<String> errorArgs;

    public VrabiaException() {
        super(ErrorCodes.INTERNAL_SERVER_ERROR.getDefaultErrorMessage());
        this.errorCode = ErrorCodes.INTERNAL_SERVER_ERROR;
        this.errorArgs = new ArrayList<>();
    }

    public VrabiaException(ErrorCodes errorCode) {
        super(Optional.ofNullable(errorCode).map(ErrorCodes::getDefaultErrorMessage).orElseGet(ErrorCodes.INTERNAL_SERVER_ERROR::getDefaultErrorMessage));
        this.errorCode = errorCode;
        this.errorArgs = new ArrayList<>();
    }

    public VrabiaException(ErrorCodes errorCode, List<String> errorArgs) {
        super(Optional.ofNullable(errorCode).map(ErrorCodes::getDefaultErrorMessage).orElseGet(ErrorCodes.INTERNAL_SERVER_ERROR::getDefaultErrorMessage));
        this.errorCode = errorCode;
        this.errorArgs = errorArgs;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    public List<String> getErrorArgs() {
        return errorArgs;
    }
}
