package com.techlab.api.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

public class StandardResponse implements ResponseInterface {
    protected boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Object data;

    public StandardResponse(HttpStatus status) {
        this.success = status.series() == HttpStatus.Series.SUCCESSFUL;

        if (!this.success) {
            this.message = status.getReasonPhrase();
        }
    }

    public StandardResponse(HttpStatus status, Object data) {
        this(status);
        this.data = data;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public Object getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
