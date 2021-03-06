package com.hoard.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.views.View;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Errors {
    NO_CHANGE(0, "No changes."),
    ITEM_NOT_FOUND(1, "Item not found."),
    ITEM_IN_USE(2, "ID or Email already in use."),
    ITEM_DELETE_ERROR(3, "Error deleting item."),
    INVALID_ID(4, "Invalid ID. Please check the URI and JSON request."),
    INVALID_EMAIL(5, "Please provide a valid email address."),
    INVALID_USERNAME(6, "Please provide a valid user name."),
    JSON_MALFORMED(7, "JSON formatting error."),
    JSON_EXTRA_FIELD(8, "An erroneous JSON field was included."),
    ITEM_CREATE_ERROR(9, "Error creating item.");

    @JsonView(View.Summary.class)
    private final int code;
    @JsonView(View.Summary.class)
    private final String detail;

    Errors(int code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + detail;
    }
}
