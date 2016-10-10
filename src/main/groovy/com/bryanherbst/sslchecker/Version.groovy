package com.bryanherbst.sslchecker

class Version implements Comparable {

    def major;
    def minor;
    def revision;
    def patch;

    static Version fromString(String version) {
        def (major, minor, last) = version.split(/\./)
        def revision = last.find(/\d+/)
        def patch = last.find(/[a-z]+/)

        Version result = new Version()
        result.major = major as int
        result.minor = minor as int
        result.revision = revision as int
        result.patch = patch

        return result
    }

    @Override
    int compareTo(Object o) {
        if (major > o.major) {
            return 1
        } else if (major < o.major) {
            return -1
        }

        if (minor > o.minor) {
            return 1
        } else if (minor < o.minor) {
            return -1
        }

        if (revision > o.revision) {
            return 1
        } else if (revision < o.revision) {
            return -1
        }

        if (patch == o.patch) {
            return 0
        } else if (patch.length() > o.patch.length()) {
            return 1
        } else if (patch.length() < o.patch.length()) {
            return -1
        } else {
            for (def i = 0; i < patch.length(); i++) {
                if (patch[i] > o.patch[i]) {
                    return 1
                }
            }

            return -1
        }
    }

    @Override
    String toString() {
        return "${major}.${minor}.${revision}${patch}"
    }
}