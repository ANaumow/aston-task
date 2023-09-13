package ru.company.app.common.util;

import ru.company.app.common.util.exception.BusinessLogicException;

public abstract class BusinessAssert {

    public static void check(boolean expression, String code, Object... args) {
        if (!expression) {
            throw new BusinessLogicException(code, args);
        }
    }

}
