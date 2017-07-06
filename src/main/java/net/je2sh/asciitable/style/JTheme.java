package net.je2sh.asciitable.style;

import lombok.Getter;



/**
 * Base theme of a table. This class borrows heavily from
 * <a href="https://github.com/iNamik/java_text_tables">https://github.com/iNamik/java_text_tables</a>.
 * <p>
 * On top of the original implementation it also adds the notion of padding.
 *
 * @see Chars
 */
@Getter
public class JTheme extends Chars {
    public static final JTheme DOUBLE_LINE =
            new JTheme('╬', '═', '║', '╔', '╦', '╗', '╠', '╣', '╚', '╩', '╝');
    public static final JTheme SINGLE_LINE =
            new JTheme('┼', '─', '│', '┌', '┬', '┐', '├', '┤', '└', '┴', '┘');
    public static final JTheme SINGLE_PADDED_LINE =
            new JTheme('┼', '─', '│', '┌', '┬', '┐', '├', '┤', '└', '┴', '┘',
                       JPadding.DEFAULT_PADDING);
    public static final JTheme NO_LINE = JTheme.of(Character.MIN_VALUE);

    private JPadding padding;

    public static JTheme of(char intersect) {
        return new JTheme(intersect, intersect, intersect, intersect, intersect, intersect,
                          intersect, intersect, intersect, intersect, intersect);
    }

    public JTheme(char i, char h, char v, char tl, char ti, char tr, char li, char ri, char bl,
                  char bi, char br)
    {
        this(i, h, v, tl, ti, tr, li, ri, bl, bi, br, JPadding.NO_PADDING);
    }

    public JTheme(char i, char h, char v, char tl, char ti, char tr, char li, char ri, char bl,
                  char bi, char br, JPadding padding)
    {
        super(i, h, v, tl, ti, tr, li, ri, bl, bi, br);
        this.padding = padding;
    }
}
