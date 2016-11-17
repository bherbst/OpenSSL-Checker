# OpenSSL Vulnerabilty Checker
[![Build Status](https://travis-ci.org/bherbst/OpenSSL-Checker.svg?branch=master)](https://travis-ci.org/bherbst/OpenSSL-Checker)
[![Coverage Status](https://coveralls.io/repos/github/bherbst/OpenSSL-Checker/badge.svg?branch=master)](https://coveralls.io/github/bherbst/OpenSSL-Checker?branch=master)

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
        classpath 'com.bryanherbst.openssl-checker:openssl-checker:1.0.0'
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
        - /Users/username/bad-library/openssl-1.0.0m
:app:checkDebugOpenSsl FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:checkDebugOpenSSL'.
> OpenSSL 1.0.0m detected and contains known vulnerabilities
```

## Source attribution
When possible, this plugin will attempt to tell you where a vulnerable Open SSL version came from.
This relies on the fact that when someone builds Open SSL, the path at which they built it is often
left in the built .so files.

For example, if you see this:
```
Found OpenSSL version 1.0.0m in:
        - /Users/username/bad-library/openssl-1.0.0m
```

You can assume pretty reasonably that "bad-library" is to blame for the bad version of Open SSL.

If the source is "unknown," we couldn't find a file path that looked like an Open SSL file path, so
we couldn't make any recommendations as to who might be at fault.

## Vulnerabilities detected

This plugin works by unzipping your apk/aar and checking for references to insecure
OpenSSL versions.

Currently only versions released after *1.0.2f* and *1.0.1r* are considered secure,
which matches what Google currently considers secure for Android applications.

You can achieve similar results by running `unzip -p your-app.apk | strings | grep "OpenSSL"`.
