package tssvett.dev.translator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tssvett.dev.translator.validation.annotation.ValidLanguage;
import tssvett.dev.translator.validation.language.Language;

public class LanguageValidator implements ConstraintValidator<ValidLanguage, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Проверяем, является ли значение допустимым языком
        return value == null || Language.isValidLanguage(value);
    }
}
