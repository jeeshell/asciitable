package net.je2sh.asciitable

import static JTable.renderTopBorder
import static net.je2sh.asciitable.style.JTheme.SINGLE_LINE

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class BorderTests extends Specification {

    def 'Top border should work'() {
        expect:
        renderTopBorder(colsWidth, null, 10, SINGLE_LINE) == expected

        where:
        colsWidth       | expected
        [4, 6] as int[] | '┌────┬──────┐'
        [5, 5] as int[] | '┌─────┬─────┐'
        [1, 9] as int[] | '┌─┬─────────┐'
    }

    def 'Middle border same width should work'() {
        expect:
        renderTopBorder(colsWidth, colsWidth, 10, SINGLE_LINE) == expected

        where:
        colsWidth       | expected
        [4, 6] as int[] | '├────┼──────┤'
        [5, 5] as int[] | '├─────┼─────┤'
        [1, 9] as int[] | '├─┼─────────┤'
    }

    def 'Middle border different width should work'() {
        expect:
        renderTopBorder(colsWidth, previousColsWidth, 10, SINGLE_LINE) == expected

        where:
        colsWidth          | previousColsWidth  | expected
        [6, 4] as int[]    | [4, 6] as int[]    | '├────┴─┬────┤'
        [4, 6] as int[]    | [6, 4] as int[]    | '├────┬─┴────┤'
        [3, 4, 3] as int[] | [4, 3, 3] as int[] | '├───┬┴───┼───┤'
        [4, 3, 3] as int[] | [3, 4, 3] as int[] | '├───┴┬───┼───┤'
    }

    def 'Bottom border should work'() {
        expect:
        JTable.renderBottomBorder(colsWidth, SINGLE_LINE) == expected

        where:
        colsWidth          | expected
        [6] as int[]       | '└──────┘'
        [4, 6] as int[]    | '└────┴──────┘'
        [4, 3, 2] as int[] | '└────┴───┴──┘'
    }
}
