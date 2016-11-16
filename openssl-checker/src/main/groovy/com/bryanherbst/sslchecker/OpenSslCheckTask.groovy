package com.bryanherbst.sslchecker

import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

/**
 * The task that checks a file for vulnerable OpenSSL versions
 */
class OpenSslCheckTask extends DefaultTask {

    def BaseVariantOutput androidOutput

    def openSslVersions = [:]

    /**
     * Check this project for vulnerable OpenSSL versions
     */
    @TaskAction
    void checkSsl() {
        def filename = androidOutput.outputFile.name
        if (filename.endsWith("apk") || filename.endsWith("aar")) {
            findOpenSslVersionsInFile(androidOutput.outputFile)
            reportOpenSslVersions(openSslVersions)
            failOnSslVulnerabilityFound(openSslVersions)
        } else {
            throw new TaskExecutionException(this, new IllegalArgumentException("Output file ${filename} is not an APK or AAR."))
        }
    }

    /**
     * Attempt to find the OpenSSL versions contained in an APK or AAR
     *
     * @param file An APK or AAR file
     * @return
     */
    def findOpenSslVersionsInFile(File file) {
        def proc = ["sh", "-c", "unzip -p ${file.absolutePath} | strings | grep -i \"OpenSSL\""]
        def results = proc.execute().text

        results.eachLine { line ->
            def (version, source) = getOpenSslVersionAndSource(line);

            if (version != null) {
                registerVersion(version, source)
            }
        }
    }

    /**
     * Attempt to parse an OpenSSL version from a line of text
     * @param line A string, preferably from the output of unzip -p | strings | grep -i
     * @return [version, source] Strings containing the version found and the source of that version
     *      if it could be determined. If the source could not be determined, returns [version, "unknown"].
     *      If OpenSSL was not found in the line, returns [null, null]
     */
    def getOpenSslVersionAndSource(String line) {
        def folderFinder = (line =~ /\/openssl-(\d+\.\d+\.\d+[a-z]+)\//)
        if (folderFinder.find()) {
            def version = folderFinder[0][1]
            def source = line.substring(0, folderFinder.start())
            return [version, source]
        }

        def unknownSourceFinder = (line =~ /OpenSSL (\d+\.\d+\.\d+[a-z]+)/)
        if (unknownSourceFinder.find()) {
            def version = unknownSourceFinder[0][1]
            def source = "unknown"
            return [version, source]
        }

        return [null, null]
    }

    /**
     * Register a version to the list of found OpenSSL versions
     * @param version The version that was found
     * @param source The source of the found version
     */
    def registerVersion(String version, String source) {
        Set<String> sources = openSslVersions.containsKey(version) ? openSslVersions.get(version) : []

        if (source == "unknown") {
            if (sources.empty) {
                sources.add(source)
            }
        } else if (sources.contains("unknown")) {
            sources.add(source)

            // We have found a likely source of this version, so it is no longer unknown.
            // There might be some usages that are still unknown, but those will be revealed
            // after fixing the known sources
            sources.remove("unknown")
        } else {
            sources.add(source)
        }

        openSslVersions.put(version, sources)
    }

    /**
     * Output the versions of SSL found
     *
     * @param versionMap a map of found OpenSSL versions to their sources
     */
    static def reportOpenSslVersions(Map<String, Set<String>> versionMap) {
        versionMap.each { version, sources ->
            println "Found OpenSSL version ${version} in:"

            sources.each { source ->
                println "\t- ${source}"
            }
        }
    }

    /**
     * Fail the task if any found OpenSSL versions contain vulnerabilities
     *
     * @param versionMap a map of found OpenSSL versions to their sources
     */
    def failOnSslVulnerabilityFound(Map<String, Set<String>> versionMap) {
        versionMap.each { version, sources ->
            if (isVersionVulnerable(version)) {
                def message = "OpenSSL ${version} detected and contains known vulnerabilities";
                throw new TaskExecutionException(this, new InsecureSslVersionException(message))
            }
        }
    }

    /**
     * Check if a specific OpenSSL version contains known vulnerabilities
     * @param version The version to check
     * @return True if the version contains known vulnerabilities
     */
    static def isVersionVulnerable(String version) {
        def foundVersion = Version.fromString(version)

        def fixVersion
        if (version.startsWith("1.0.1")) {
            fixVersion = Version.fromString("1.0.1r");
        } else {
            fixVersion = Version.fromString("1.0.2f");
        }

        return foundVersion.compareTo(fixVersion) < 0;
    }
}