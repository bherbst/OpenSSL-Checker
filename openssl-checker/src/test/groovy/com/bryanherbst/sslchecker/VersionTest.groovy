package com.bryanherbst.sslchecker

class VersionTest extends GroovyTestCase {

    void testParseSimpleVersionString() {
        def version = Version.fromString("1.2.3m")

        assertEquals 1, version.major
        assertEquals 2, version.minor
        assertEquals 3, version.revision
        assertEquals "m", version.patch
    }

    void testSimpleVersionEqual() {
        def version1 = Version.fromString("1.2.3a")
        def version2 = Version.fromString("1.2.3a")

        assertTrue version1 == version2
    }

    void testSimpleVersionPatchGreater() {
        def version1 = Version.fromString("1.2.3b")
        def version2 = Version.fromString("1.2.3a")

        assertTrue version1 > version2
    }

    void testSimpleVersionPatchLesser() {
        def version1 = Version.fromString("1.2.3a")
        def version2 = Version.fromString("1.2.3b")

        assertTrue version1 < version2
    }

    void testTwoDigitPatchGreater() {
        def version1 = Version.fromString("1.2.3zb")
        def version2 = Version.fromString("1.2.3za")

        assertTrue version1 > version2
    }

    void testTwoDigitPatchLesser() {
        def version1 = Version.fromString("1.2.3za")
        def version2 = Version.fromString("1.2.3zb")

        assertTrue version1 < version2
    }

    void testMixLengthPatchGreater() {
        def version1 = Version.fromString("1.2.3za")
        def version2 = Version.fromString("1.2.3b")

        assertTrue version1 > version2
    }

    void testMixLengthPatchLesser() {
        def version1 = Version.fromString("1.2.3b")
        def version2 = Version.fromString("1.2.3za")

        assertTrue version1 < version2
    }

    void testRevisionGreater() {
        def version1 = Version.fromString("1.2.4a")
        def version2 = Version.fromString("1.2.3a")

        assertTrue version1 > version2
    }

    void testRevisionLesser() {
        def version1 = Version.fromString("1.2.3a")
        def version2 = Version.fromString("1.2.4a")

        assertTrue version1 < version2
    }

    void testMinorGreater() {
        def version1 = Version.fromString("1.3.3a")
        def version2 = Version.fromString("1.2.3a")

        assertTrue version1 > version2
    }

    void testMinorLesser() {
        def version1 = Version.fromString("1.2.3a")
        def version2 = Version.fromString("1.3.3a")

        assertTrue version1 < version2
    }

    void testMajorGreater() {
        def version1 = Version.fromString("2.2.3a")
        def version2 = Version.fromString("1.2.3a")

        assertTrue version1 > version2
    }

    void testMajorLesser() {
        def version1 = Version.fromString("1.2.3a")
        def version2 = Version.fromString("2.3.3a")

        assertTrue version1 < version2
    }

    void testToString() {
        def version = Version.fromString("1.2.3a")

        assertEquals "1.2.3a", version.toString()
    }

}