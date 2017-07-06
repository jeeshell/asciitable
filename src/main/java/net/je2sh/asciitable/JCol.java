package net.je2sh.asciitable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import net.je2sh.asciitable.style.JPadding;
import net.je2sh.asciitable.style.JTheme;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;



/**
 * Representation of a column. The contents will use {@link Object#toString()} to render them as a
 * String.
 *
 * @see JRow
 * @see JTable
 */
@Getter
public class JCol {

    /**
     * Parent row
     */
    private final JRow row;

    /**
     * Explicit width of this column. May be {@code null} in which case width will be automatically
     * assessed based on the number of columns and the width of the table.
     */
    private Integer width;

    /**
     * Content held by this column. Will be converted to a String through {@link Object#toString()}
     */
    private Object content;

    /**
     * Explicit padding of this column. If {@code null} it will default to {@link JRow#getPadding()}
     */
    private JPadding padding;

    public JCol(JRow row) {
        this.row = row;
    }

    public JCol content(Object content) {
        this.content = content;
        return this;
    }

    public JCol width(int width) {
        this.width = width;
        return this;
    }

    public JCol padding(JPadding padding) {
        this.padding = padding;
        return this;
    }

    public JPadding getPadding() {
        return Optional.ofNullable(padding).orElse(row.getPadding());
    }

    public JRow done() {
        return row;
    }

    /**
     * Same as {@link #render(JTheme, int, Integer)} where the height is unknown and therefore only
     * the lines required by this column will be generated.
     *
     * @param theme      Theme to be used on this column
     * @param widthToUse How wide should this column be
     * @return A list of lines representing
     * @see #render(JTheme, int, Integer)
     */
    public List<String> render(JTheme theme, int widthToUse) {
        return render(theme, widthToUse, null);
    }

    /**
     * Renders the column producing a list of Strings representing each line of the column.
     * <p>
     * The produced lines <strong>only contain the left border</strong>.
     * <p>
     * This method guarantees that if the content is wider than {@literal widthToUse} then it is
     * wrapped to respect the width. {@link JPadding#rightCharacter} is used to fill in the
     * gaps.
     *
     * @param theme       Theme to be used on this column
     * @param widthToUse  How wide should this column be
     * @param heightToUse How high should this column be
     * @return A list of lines representing
     * @see #wrapLines(String, int)
     */
    public List<String> render(JTheme theme, int widthToUse, Integer heightToUse) {
        List<String> colResult = new ArrayList<>();

        if (getPadding().getTop() > 0) {
            String topPadding = (theme.vertical != Character.MIN_VALUE ? theme.vertical : "") +
                                StringUtils.repeat(getPadding().getTopCharacter(), widthToUse);
            for (int i = 0; i < getPadding().getTop(); i++) {
                colResult.add(topPadding);
            }
        }

        for (String contentLine : wrapLines(content.toString(), widthToUse)) {
            colResult.add((theme.vertical != Character.MIN_VALUE ? theme.vertical : "") +
                          contentLine);
        }

        if (getPadding().getBottom() > 0 || (heightToUse != null && heightToUse > 0)) {
            int bottomHeight = Math.max(getPadding().getBottom(),
                                        Optional.ofNullable(heightToUse).orElse(0));
            String bottomPadding = (theme.vertical != Character.MIN_VALUE ? theme.vertical : "") +
                                   StringUtils.repeat(getPadding().getBottomCharacter(),
                                                      widthToUse);
            for (int i = 0; i < bottomHeight; i++) {
                colResult.add(bottomPadding);
            }
        }

        return colResult;
    }

    /**
     * Wraps the content in such a way that {@literal widthToUse} is respected. Wrapping is done
     * based on '{@literal \n}' therefore any explicit line breaks will also be respected.
     *
     * @param rawContent Unwrapped content (may contain explicit '{@literal \n}'s)
     * @param widthToUse Maximum width allowed
     * @return A list of Strings representing the wrapped content
     * @see WordUtils#wrap(String, int, String, boolean)
     */
    private List<String> wrapLines(String rawContent, int widthToUse) {
        List<String> result = new ArrayList<>();
        int rawContentLength = row.getTable().getContentParser().getLength(rawContent);
        for (String explicitLine : WordUtils.wrap(rawContent,
                                                  widthToUse - getPadding().getLeft() -
                                                  getPadding().getRight() +
                                                  (rawContent.length() - rawContentLength),
                                                  "\n", true)
                                            .split("\n")) {
            String contentStr =
                    StringUtils.repeat(getPadding().getLeftCharacter(), getPadding().getLeft()) +
                    explicitLine +
                    StringUtils.repeat(getPadding().getRightCharacter(), getPadding().getRight());
            int contentLength = row.getTable().getContentParser().getLength(contentStr);
            StringBuilder extraPadding = new StringBuilder();
            if (getPadding().getRight() > 0) {
                extraPadding.append(StringUtils.repeat(getPadding().getRightCharacter(),
                                                       widthToUse - contentLength));
            }
            else {
                extraPadding.append(StringUtils.repeat(' ',
                                                       widthToUse - contentLength));
            }
            result.add(contentStr + extraPadding.toString());
        }

        return result;
    }

}
