package com.bryanherbst.sslchecker

class InsecureSslVersionException extends Exception {
    InsecureSslVersionException(String message) {
        super(message)
    }
}