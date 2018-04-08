package com.hoard.error;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.views.View;


public class ErrorCodes {
    //@JsonView(View.Summary.class)
    //private int code;
    @JsonView(View.Summary.class)
    private String error;
    @JsonView(View.Summary.class)
    private String detail;

    public ErrorCodes(){}

    public ErrorCodes(String error, String detail) {
        this.error = error;
        this.detail = detail;
    }
    /*
        public ErrorCodes(int code, String name, String detail) {
            this.code = code;
            this.name = name;
            this.detail = detail;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    */
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
