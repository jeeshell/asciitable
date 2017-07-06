package net.je2sh.asciitable

import static org.fusesource.jansi.Ansi.ansi

import net.je2sh.asciitable.style.JPadding
import net.je2sh.asciitable.style.JTheme
import org.fusesource.jansi.Ansi
import spock.lang.Specification

class TableTests extends Specification {

    def 'Simple table should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col().content('Simple content').done()
                .done()

        def result = [
                '┌────────────────────┐',
                '│Simple content      │',
                '└────────────────────┘'
        ]

        TestUtils.printTable(table.render(), result)

        expect:
        table.render() == result
    }

    def 'Large content should wrap'() {
        given:
        def table = JTable.of().width(10)
                .row()
                .col().content('Simple content').done()
                .done()

        def result = [
                '┌──────────┐',
                '│Simple    │',
                '│content   │',
                '└──────────┘'
        ]

        TestUtils.printTable(table.render(), result)

        expect:
        table.render() == result
    }

    def 'Multi column auto width table should work'() {
        given:
        def table = JTable.of().width(30)
                .row()
                .col().content('Simple content').done()
                .col().content('Alternative').done()
                .done()

        def result = [
                '┌───────────────┬───────────────┐',
                '│Simple content │Alternative    │',
                '└───────────────┴───────────────┘'
        ]
        TestUtils.printTable(table.render(), result)
        expect:
        table.render() == result
    }

    def 'Multi column auto odd width table should work'() {
        given:
        def table = JTable.of().width(31)
                .row()
                .col().content('Simple content').done()
                .col().content('Alternative').done()
                .done()

        def result = [
                '┌───────────────┬────────────────┐',
                '│Simple content │Alternative     │',
                '└───────────────┴────────────────┘'
        ]
        TestUtils.printTable(table.render(), result)
        expect:
        table.render() == result
    }

    def 'Multi column fixed width table should work'() {
        given:
        def table = JTable.of().width(28)
                .row()
                .col().width(17).content('Simple content').done()
                .col().width(11).content('Alternative').done()
                .done()

        def result = [
                '┌─────────────────┬───────────┐',
                '│Simple content   │Alternative│',
                '└─────────────────┴───────────┘'
        ]
        expect:
        table.render() == result
    }

    def 'Multi row table should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col().content('Simple content').done()
                .done()
                .row()
                .col().content('Alt content').done()
                .done()

        def result = [
                '┌────────────────────┐',
                '│Simple content      │',
                '├────────────────────┤',
                '│Alt content         │',
                '└────────────────────┘'
        ]
        expect:
        table.render() == result
    }

    def 'Multi row ANSI table should work'() {
        given:
        def table = JTable.of().width(20)
                .contentParser(new AnsiContentParser())
                .row()
                .col().content(ansi().fg(Ansi.Color.RED).a("Simple")
                .bgYellow().fgBlue().a(" content").reset()).done()
                .done()
                .row()
                .col().content('Alt content').done()
                .done()

        def result = [
                '┌────────────────────┐',
                '│\u001B[31mSimple\u001B[43;34m content\u001B[m      │',
                '├────────────────────┤',
                '│Alt content         │',
                '└────────────────────┘'
        ]

        TestUtils.printTable(table.render(), result)

        expect:
        table.render() == result
    }

    def 'Complex table auto width should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col().content('Simple 1').done()
                .col().content('Simple 2').done()
                .done()
                .row()
                .col().content('Alt 1').done()
                .col().content('Alt 2').done()
                .done()

        def result = [
                '┌──────────┬──────────┐',
                '│Simple 1  │Simple 2  │',
                '├──────────┼──────────┤',
                '│Alt 1     │Alt 2     │',
                '└──────────┴──────────┘'
        ]
        expect:
        table.render() == result
    }

    def 'Complex table constant width should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col().width(11).content('Simple 1').done()
                .col().width(9).content('Simple 2').done()
                .done()
                .row()
                .col().width(11).content('Alt 1').done()
                .col().width(9).content('Alt 2').done()
                .done()

        def result = [
                '┌───────────┬─────────┐',
                '│Simple 1   │Simple 2 │',
                '├───────────┼─────────┤',
                '│Alt 1      │Alt 2    │',
                '└───────────┴─────────┘'
        ]
        expect:
        table.render() == result
    }

    def 'Complex table variable width should work'() {
        given:
        def table = JTable.of().width(23)
                .row()
                .col().width(11).content('Simple 1').done()
                .col().width(5).content('A 1').done()
                .col().width(7).content('Bob 2').done()
                .done()
                .row()
                .col().width(6).content('Alt 1').done()
                .col().width(9).content('Alt 2').done()
                .col().width(8).content('Second').done()
                .done()

        def result = [
                '┌───────────┬─────┬───────┐',
                '│Simple 1   │A 1  │Bob 2  │',
                '├──────┬────┴────┬┴───────┤',
                '│Alt 1 │Alt 2    │Second  │',
                '└──────┴─────────┴────────┘'
        ]

        expect:
        table.render() == result
    }

    def 'Row with invalid width should fail'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col().width(colWidth).content('Simple content').done()
                .done()

        when:
        table.render()

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains('Row width does not match')

        where:
        colWidth << [10, 19, 21, 30]
    }

    def 'Cell with padding should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col()
                .padding(JPadding.of(1, 0))
                .content('Simple content').done()
                .done()

        def result = [
                '┌────────────────────┐',
                '│ Simple content     │',
                '└────────────────────┘'
        ]

        expect:
        table.render() == result
    }

    def 'Row with side padding should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .padding(JPadding.of(1, 0))
                .col()
                .content('Simple content').done()
                .done()

        def result = [
                '┌────────────────────┐',
                '│ Simple content     │',
                '└────────────────────┘'
        ]

        expect:
        table.render() == result
    }

    def 'Table with padding should work'() {
        given:
        def table = JTable.of().width(20)
                .theme(JTheme.SINGLE_PADDED_LINE)
                .row()
                .col()
                .content('Simple content').done()
                .done()

        def result = [
                '┌────────────────────┐',
                '│ Simple content     │',
                '└────────────────────┘'
        ]

        expect:
        table.render() == result
    }

    def 'Cell with different character padding should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col()
                .padding(JPadding.of(2, 0, '^' as char))
                .content('Simple content').done()
                .done()

        def result = [
                '┌────────────────────┐',
                '│^^Simple content^^^^│',
                '└────────────────────┘'
        ]

        expect:
        table.render() == result
    }

    def 'Row with top and bottom padding should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .padding(JPadding.of(1, 1))
                .col()
                .content('Simple content').done()
                .done()

        def result = [
                '┌────────────────────┐',
                '│                    │',
                '│ Simple content     │',
                '│                    │',
                '└────────────────────┘'
        ]

        TestUtils.printTable(table.render(), result)
        expect:
        table.render() == result
    }

    def 'Cells with mixed padding should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col()
                .padding(JPadding.of(1, 1))
                .content('Main 1').done()
                .col()
                .padding(JPadding.of(2, 0))
                .content('Alt 1').done()
                .done()

        def result = [
                '┌──────────┬──────────┐',
                '│          │  Alt 1   │',
                '│ Main 1   │          │',
                '│          │          │',
                '└──────────┴──────────┘'
        ]

        TestUtils.printTable(table.render(), result)
        expect:
        table.render() == result
    }

    def 'Cells with complex padding should work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col()
                .padding(JPadding.of(1, 1))
                .content('Main 1').done()
                .col()
                .padding(JPadding.of(2, 0))
                .content('Alt 1').done()
                .done()
                .row()
                .col()
                .padding(JPadding.of(1, 4, 1, 2))
                .width(7)
                .content('Bob X').done()
                .col()
                .width(13)
                .padding(JPadding.of(2, 2, 1, 0))
                .content('Charl Y').done()
                .done()

        def result = [
                '┌──────────┬──────────┐',
                '│          │  Alt 1   │',
                '│ Main 1   │          │',
                '│          │          │',
                '├───────┬──┴──────────┤',
                '│       │             │',
                '│       │             │',
                '│       │  Charl Y    │',
                '│       │             │',
                '│ Bob X │             │',
                '│       │             │',
                '│       │             │',
                '└───────┴─────────────┘'
        ]

        TestUtils.printTable(table.render(), result)
        expect:
        table.render() == result
    }

    def 'Cells with complex padding and long text should wrap and work'() {
        given:
        def table = JTable.of().width(20)
                .row()
                .col()
                .padding(JPadding.of(1, 1))
                .content('Main 1 but larger').done()
                .col()
                .padding(JPadding.of(2, 0))
                .content('Alt 1').done()
                .done()
                .row()
                .col()
                .padding(JPadding.of(1, 4, 1, 2))
                .width(7)
                .content('verylargesingleword').done()
                .col()
                .width(13)
                .padding(JPadding.of(2, 2, 1, 0))
                .content('Charl Y').done()
                .done()

        def result = [
                '┌──────────┬──────────┐',
                '│          │  Alt 1   │',
                '│ Main 1   │          │',
                '│ but      │          │',
                '│ larger   │          │',
                '│          │          │',
                '├───────┬──┴──────────┤',
                '│       │             │',
                '│       │             │',
                '│       │  Charl Y    │',
                '│       │             │',
                '│ veryl │             │',
                '│ arges │             │',
                '│ ingle │             │',
                '│ word  │             │',
                '│       │             │',
                '│       │             │',
                '└───────┴─────────────┘'
        ]

        TestUtils.printTable(table.render(), result)
        expect:
        table.render() == result
    }

    def 'Cells with complex padding and long text and no border should wrap and work'() {
        given:
        def table = JTable.of().width(20)
                .theme(JTheme.NO_LINE)
                .row()
                .col()
                .padding(JPadding.of(1, 1))
                .content('Main 1 but larger').done()
                .col()
                .padding(JPadding.of(2, 0))
                .content('Alt 1').done()
                .done()
                .row()
                .col()
                .padding(JPadding.of(1, 4, 1, 2))
                .width(7)
                .content('verylargesingleword').done()
                .col()
                .width(13)
                .padding(JPadding.of(2, 2, 1, 0))
                .content('Charl Y').done()
                .done()

        def result = [
                '            Alt 1   ',
                ' Main 1             ',
                ' but                ',
                ' larger             ',
                '                    ',
                '                    ',
                '                    ',
                '         Charl Y    ',
                '                    ',
                ' veryl              ',
                ' arges              ',
                ' ingle              ',
                ' word               ',
                '                    ',
                '                    '
        ]

        TestUtils.printTable(table.render(), result)
        expect:
        table.render() == result
    }


}
