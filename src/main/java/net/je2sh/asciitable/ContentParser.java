package net.je2sh.asciitable;

/**
 * Interface that allows implementations to properly determine the length of the content.
 * <p>
 * Different implementations may consider parts of the content to be invisible and therefore should
 * not be accounted for.
 * This is particularly relevant when determining if a column needs to be wrapped.
 *
 * @see JCol#wrapLines(String, int)
 */
public interface ContentParser {

    int getLength(Object content);

}
