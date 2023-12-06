/*
 * Copyright (c) mithilesh89ece@gmail.com. All Rights Reserved.
 * ============================================================
 */
package nl.abn.assignment.exception;

public class InvalidJwtToken extends RuntimeException {

    public InvalidJwtToken(String message) {
        super(message);
    }

    public InvalidJwtToken(String message, Throwable cause) {
        super(message, cause);
    }
}