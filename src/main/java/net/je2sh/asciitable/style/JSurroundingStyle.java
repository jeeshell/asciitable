package net.je2sh.asciitable.style;

import lombok.Data;



@Data
public abstract class JSurroundingStyle {

    private final int left;
    private final int top;
    private final int right;
    private final int bottom;
    private final Character leftCharacter;
    private final Character topCharacter;
    private final Character rightCharacter;
    private final Character bottomCharacter;

}
