package com.techlab.api.exceptions;

import com.techlab.api.responses.data.ErrorData;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private HttpStatus status;
    private ErrorData<?> errorData;

    public ApiException(HttpStatus status, ErrorData<?> errorData) {
      super(errorData.toString());
      this.status = status;
      this.errorData = errorData;
    }

    public HttpStatus getStatus() {
      return this.status;
    }

    public ErrorData<?> getErrorData() {
      return this.errorData;
    }

    public String getMessage() {
        return this.errorData.toString();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setErrorData(ErrorData<?> errorData) {
        this.errorData = errorData;
    }

    @Override
    public String toString() {
        return this.errorData.toString();
    }
}
