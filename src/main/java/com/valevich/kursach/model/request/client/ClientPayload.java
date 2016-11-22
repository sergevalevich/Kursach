package com.valevich.kursach.model.request.client;

import com.valevich.kursach.model.request.Validatable;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.valevich.kursach.util.ConstantsManager.EMAIL_PATTERN;

@Data
public class ClientPayload implements Validatable {

    private String email;
    private String password;

    @Override
    public boolean isValid() {
        return email != null && !email.isEmpty() && isEmailValid() && password != null && !password.isEmpty();
    }

    private boolean isEmailValid() {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
