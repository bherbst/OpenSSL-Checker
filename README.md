# OpenSSL Vulnerabilty Checker
[![Build Status](https://travis-ci.org/bherbst/OpenSSL-Checker.svg?branch=master)](https://travis-ci.org/bherbst/OpenSSL-Checker)

A Gradle plugin for checking whether an .apk or an .aar contains OpenSSL
versions with known vulnerabilities.

Google automatically scans the APKs you upload to the Play Store for versions
of OpenSSL that contain known vulnerabilities. If it detects a vulnerable OpenSSL version, your app
will be rejected. You can find more information on addressing these vulnerabilities in your
application [here](https://support.google.com/faqs/answer/6376725).

## Usage

In your project's root `build.gradle`:
```groovy
buildscript {
    repositories {
        // jCenter() or mavenCentral()
    }

    dependencies {
        classpath 'com.bryanherbst.openssl-checker:openssl-checker:0.1.1'
    }
}
```

In your `app/build.gradle`:
```groovy
apply plugin: 'android'
//...
apply plugin: 'com.bryanherbst.openssl-checker'
```

Then run `./gradlew check[variantName]OpenSSL`. For example, `./gradlew checkDebugOpenSsl`.
This task will fail if a vulnerable version is found.

*Note:* This plugin currently only works on Unix machines, as it runs a shell
command to analyze your build's output file. Contributions to get it working on
Windows are welcome!

## Sample output
```
Found OpenSSL version 1.0.0m in:
        - /Users/username/bad-library
:app:checkDebugOpenSsl FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:checkDebugOpenSSL'.
> OpenSSL 1.0.0m detected and contains known vulnerabilities
```

## Vulnerabilities detected

This plugin works by unzipping your apk/aar and checking for references to insecure
OpenSSL versions.

Currently only versions released after *1.0.2f* and *1.0.1r* are considered secure,
which matches what Google currently considers secure for Android applications.

You can achieve similar results by running `unzip -p your-app.apk | strings | grep "OpenSSL"`.
