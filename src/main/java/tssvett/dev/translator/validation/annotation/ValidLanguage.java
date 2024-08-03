package tssvett.dev.translator.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import tssvett.dev.translator.validation.LanguageValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LanguageValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLanguage {
    String message() default "Invalid language code. Please, check documentation for valid language codes.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
