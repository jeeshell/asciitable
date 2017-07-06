package net.je2sh.asciitable.style;

import lombok.Getter;



@Getter
public class JPadding extends JSurroundingStyle {

    public static final Character DEFAULT_PADDING_CHARACTER = ' ';
    public static final JPadding DEFAULT_PADDING = JPadding.of(1, 0);
    public static final JPadding NO_PADDING = JPadding.of(0, 0);

    public static JPadding of(int leftAndRight, int topAndBottom) {
        return new JPadding(leftAndRight, topAndBottom, leftAndRight, topAndBottom,
                            DEFAULT_PADDING_CHARACTER);
    }

    public static JPadding of(int leftAndRight, int topAndBottom, Character character) {
        return new JPadding(leftAndRight, topAndBottom, leftAndRight, topAndBottom, character);
    }

    public static JPadding of(int left, int top, int right, int bottom) {
        return new JPadding(left, top, right, bottom, DEFAULT_PADDING_CHARACTER);
    }

    public JPadding(int left, int top, int right, int bottom, Character character) {
        super(left, top, right, bottom, character, character, character, character);
    }
}
