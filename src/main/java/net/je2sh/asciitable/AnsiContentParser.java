package net.je2sh.asciitable;

import org.fusesource.jansi.AnsiString;



/**
 * Content parser that ignores ANSI sequences for the sake of content length calculations.
 *
 * @see AnsiString
 */
public class AnsiContentParser implements ContentParser {
    @Override
    public int getLength(Object content) {
        return new AnsiString(content.toString()).length();
    }
}
