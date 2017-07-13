# ASCII Table

This library provides an easy and fluent way to build ASCII tables. Looking at a very simple first example:

```java
JTable table = JTable.of()
                     .width(50)
                     .row()
                        .col().content("Col 1 - 1").done()
                        .col().content("Col 1 - 2").done()
                     .done()
                     .row()
                        .col().content("Col 2 - 1").done()
                        .col().content("Col 2 - 2").done()
                     .done();

table.render().forEach(System.out::println)
```
Results in

```
┌─────────────────────────┬─────────────────────────┐
│Col 1 - 1                │Col 1 - 2                │
├─────────────────────────┼─────────────────────────┤
│Col 2 - 1                │Col 2 - 2                │
└─────────────────────────┴─────────────────────────┘
```


## Related Projects

This library is built with a lot of borrowed ideas (and even some code) from other similar projects.

* https://github.com/iNamik/java_text_tables
* https://github.com/vdmeer/asciitable

## Motivation

The main motivation behind this library is to support several features that I was unable to find in
any other libraries I explored:

* Use ANSI codes
* Support variable column widths
* Automatic line wrapping

More on the above to come

## Usage

ASCII Table is rather simple by nature, it uses a fluent API to build the table. It does however require
the following:

* The table MUST have an explicit width
* The columns' width of each row must add up to the table width. They **are not required** and we'll
evenly distribute the available width among the columns


Object | Method | Description 
--- | --- | ---
**JTable** | `of()` | Bootstrap method to create a table.
... | `width(int width)` | Sets the width of the table to the specified number of **spaces**
... | `theme(JTheme theme)` | Speficies the theme to be used by this table. Defaults to single line border
... | `contentParser(ContentParser contentParser)` | Specified the content parser to be used. Defaults to simple text parser
... | `render()` | Generates a list of strings representing the rendered table. Each entry represents a line
... | `row()` | Creates a new row and returns its fluent builder
**JRow** | `padding(JPadding padding)` | Specified the padding to be used on all columns of this row. Defaults to the theme's configuration
... | `done()` | Returns the parent `JTable` fluent builder
... | `col()` | Creates a new column and returns its fluent builder
**JCol** | `content(Object content)` | Sets the content of the column
... | `width(int width)` | Explicitly sets the width of the column
... | `padding(JPadding padding)` | Specified the padding to be used by the column. Defaults to the row's configuration
... | `done()` | Returns the parent `JRow` fluent builder

## In Action


### Variable Length Columns

```java
JTable.of().width(23)
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
      .render().forEach(System.out::println);

Results in
┌───────────┬─────┬───────┐
│Simple 1   │A 1  │Bob 2  │
├──────┬────┴────┬┴───────┤
│Alt 1 │Alt 2    │Second  │
└──────┴─────────┴────────┘
```

### ANSI Colors

Ansi colors are provided by [JAnsi](https://github.com/fusesource/jansi).

Notice that a different `ContentParser` is used. This is required so column width calculations disregard magic
ASCII codes.

```java
JTable.of().width(20)
      .contentParser(new AnsiContentParser())
      .row()
        .col().content(ansi().fg(Ansi.Color.RED).a("Simple")
        .bgYellow().fgBlue().a(" content").reset()).done()
      .done()
      .row()
        .col().content('Alt content').done()
      .done()
      .render().forEach(System.out::println)
┌────────────────────┐
│[31mSimple[43;34m content[m      │
├────────────────────┤
│Alt content         │
└────────────────────┘
```

### Custom Theme

We provide 3 builtin themes, single line, double line and no line (pretty self explanatory). You may
however create your own!

```java
JTheme theme = new JTheme('#', '-', '}', '/', 'v', '\\', '>', '<', '\\', '^', '/');
JTable.of().width(20)
      .theme(theme)
      .row()
        .col().content('Simple 1').done()
        .col().content('Simple 2').done()
      .done()
      .row()
        .col().content('Alt 1').done()
        .col().content('Alt 2').done()
      .done()

Results in
/----------v----------\
}Simple 1  }Simple 2  }
>----------#----------<
}Alt 1     }Alt 2     }
\----------^----------/
```

### Custom Padding

We provide 2 builtin padding options, `NO_PADDING` (default) and `DEFAUL_PADDING` (one space on each side).
 As with everything else, you may create your own padding pattern!
 
 
```java

JPadding specialPadding = new JPadding(2, '&', 1, '%', 4, '+', 2, '"')
JPadding specialPadding2 = new JPadding(2, '>', 1, 'v', 4, '<', 2, '^')
JTable.of().width(30)
      .row()
        .col().padding(JPadding.DEFAULT_PADDING).content('Simple 1').done()
        .col().padding(specialPadding).content('Simple 2').done()
      .done()
      .row()
        .col().padding(specialPadding2).content('Alt 1').done()
        .col().content('Alt 2').done()
      .done()
      .render().forEach(System.out::println)

Results in

┌───────────────┬───────────────┐
│ Simple 1      │%%%%%%%%%%%%%%%│
│               │&&Simple 2+++++│
│               │"""""""""""""""│
│               │"""""""""""""""│
├───────────────┼───────────────┤
│vvvvvvvvvvvvvvv│Alt 2          │
│>>Alt 1<<<<<<<<│               │
│^^^^^^^^^^^^^^^│               │
│^^^^^^^^^^^^^^^│               │
└───────────────┴───────────────┘
```
