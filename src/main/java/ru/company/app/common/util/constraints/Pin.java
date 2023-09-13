package ru.company.app.common.util.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@PositiveOrZero
@Max(9999)
@ReportAsSingleViolation
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
public @interface Pin {

    String message() default "{error.validation.incorrectPinFormat}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
