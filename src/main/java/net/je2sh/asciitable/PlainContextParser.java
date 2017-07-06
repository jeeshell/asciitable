package net.je2sh.asciitable;

/**
 * Content parser that interprets Strings as-is
 */
public class PlainContextParser implements ContentParser {
    @Override
    public int getLength(Object content) {
        return content.toString().length();
    }
}
