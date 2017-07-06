package net.je2sh.asciitable.style;

/**
 * Borrowed from
 * <a href="https://github.com/iNamik/java_text_tables/blob/dc624fdf7b03ca20aa070c5af5f5fb86fba1e0c5/src/main/java/com/inamik/text/tables/grid/Border.java#L185">https://github.com/iNamik/java_text_tables/blob/dc624fdf7b03ca20aa070c5af5f5fb86fba1e0c5/src/main/java/com/inamik/text/tables/grid/Border.java#L185</a>
 * <p>
 * iNamik Text Tables for Java
 * <p>
 * Copyright (C) 2016 David Farrell (DavidPFarrell@yahoo.com)
 * <p>
 * Licensed under The MIT License (MIT), see LICENSE.txt
 *
 * @see <a href="https://github.com/iNamik/java_text_tables">https://github.com/iNamik/java_text_tables</a>
 */
public class Chars {
    public final char intersect;
    public final char horizontal;
    public final char vertical;
    public final char topLeft;
    public final char topIntersect;
    public final char topRight;
    public final char leftIntersect;
    public final char rightIntersect;
    public final char bottomLeft;
    public final char bottomIntersect;
    public final char bottomRight;

    public Chars(char i, char h, char v, char tl, char ti, char tr, char li, char ri, char bl,
                 char bi, char br)
    {
        this.intersect = i;
        this.horizontal = h;
        this.vertical = v;
        this.topLeft = tl;
        this.topIntersect = ti;
        this.topRight = tr;
        this.leftIntersect = li;
        this.rightIntersect = ri;
        this.bottomLeft = bl;
        this.bottomIntersect = bi;
        this.bottomRight = br;
    }
}
