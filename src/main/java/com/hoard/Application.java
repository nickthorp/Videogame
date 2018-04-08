package com.hoard;

import com.hoard.error.ErrorCodes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaAuditing
public class Application {

    public static Map<String, ErrorCodes> errors = new HashMap<String, ErrorCodes>();

    static private void buildErrors(){
        errors.put("NO_CHANGE", new ErrorCodes("NO_CHANGE", "No changes."));
        errors.put("ITEM_NOT_FOUND", new ErrorCodes("ITEM_NOT_FOUND", "Item not found."));
        errors.put("ITEM_IN_USE", new ErrorCodes("ITEM_IN_USE", "ID or Email already in use."));
        errors.put("ITEM_DELETE_ERROR", new ErrorCodes("ITEM_DELETE_ERROR", "Error deleting item."));
        errors.put("INVALID_ID", new ErrorCodes("INVALID_ID", "Invalid ID. Please check the URI and JSON request."));
        errors.put("INVALID_EMAIL", new ErrorCodes("INVALID_EMAIL", "Please provide a valid email address."));
        errors.put("INVALID_USERNAME", new ErrorCodes("INVALID_USERNAME", "Please provide a valid user name."));
        errors.put("MALFORMED_JSON", new ErrorCodes("MALFORMED_JSON", "JSON formatting error."));
    }

    public static void main(String[] args) {
        buildErrors();
        SpringApplication.run(Application.class, args);
    }
}