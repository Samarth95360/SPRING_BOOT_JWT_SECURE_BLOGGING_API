package com.example.Blogging.CustomValidator.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator passwordValidator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30), // Minimum 8, Maximum 30 characters
                new CharacterRule(EnglishCharacterData.UpperCase, 1), // At least 1 uppercase character
                new CharacterRule(EnglishCharacterData.LowerCase, 1), // At least 1 lowercase character
                new CharacterRule(EnglishCharacterData.Digit, 1), // At least 1 digit
                new CharacterRule(EnglishCharacterData.Special, 1), // At least 1 special character
                new WhitespaceRule() // No whitespace allowed
        ));

        RuleResult result = passwordValidator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        // Collect custom error messages for all failed rules
        List<String> messages = result.getDetails().stream()
                .map(detail -> {
                    String errorCode = detail.getErrorCode();
                    Object values = detail.getValues(); // Get the values object
                    switch (errorCode) {
                        case "TOO_SHORT":
                            return "Password must be at least " + extractValue(values, "minimumLength") + " characters long.";
                        case "TOO_LONG":
                            return "Password must not exceed " + extractValue(values, "maximumLength") + " characters.";
                        case "INSUFFICIENT_UPPERCASE":
                            return "Password must contain at least one uppercase letter.";
                        case "INSUFFICIENT_LOWERCASE":
                            return "Password must contain at least one lowercase letter.";
                        case "INSUFFICIENT_DIGIT":
                            return "Password must contain at least one numeric digit.";
                        case "INSUFFICIENT_SPECIAL":
                            return "Password must contain at least one special character.";
                        case "ILLEGAL_WHITESPACE":
                            return "Password must not contain whitespace.";
                        default:
                            return "Invalid password.";
                    }
                })
                .collect(Collectors.toList());

        // Add custom error messages to the context
        context.disableDefaultConstraintViolation();
        messages.forEach(message ->
                context.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation()
        );

        return false;
    }

    private String extractValue(Object values, String key) {
        // If values is a Map
        if (values instanceof Map) {
            return ((Map<String, String>) values).get(key);
        }

        // If values is an Object array
        if (values instanceof Object[]) {
            Object[] valueArray = (Object[]) values;
            return Arrays.toString(valueArray); // Convert to String for clarity
        }

        // Default fallback
        return "unknown";
    }
}
