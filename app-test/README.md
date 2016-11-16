# Integration Test Application
This application serves primarily as an integration test for OpenSSL-Checker

`gradlew checkJniLibDebugOpenSsl` will check this application with a vulnerable Open SSL version
included as a JNI library (should fail).

`gradlew checkNoSslDebugOpenSsl` will check this application with no Open SSL library (should succeed).

Be sure to update build.gradle to reflect the OpenSSL-Checker version you actually want to test.