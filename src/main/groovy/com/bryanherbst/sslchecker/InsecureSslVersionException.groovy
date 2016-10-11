package com.bryanherbst.sslchecker

/**
 * Thrown when an insecure OpenSSL version is found
 */
class InsecureSslVersionException extends Exception {
    InsecureSslVersionException(String message) {
        super(message)
    }
}