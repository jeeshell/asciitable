package net.je2sh.asciitable

class TestUtils {

    static def printTable(List<String> result, List<String> expected = null) {
        result.each {println it}
        if (expected) {
            println 'Expected'
            expected.each {println it}
        }
    }

}
