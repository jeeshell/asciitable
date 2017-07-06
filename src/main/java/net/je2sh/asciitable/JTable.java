package net.je2sh.asciitable;

import static net.je2sh.asciitable.style.JTheme.SINGLE_LINE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.Getter;
import net.je2sh.asciitable.style.Chars;
import net.je2sh.asciitable.style.JTheme;
import org.apache.commons.lang3.StringUtils;



/**
 * Representation of a table
 *
 * @see JTheme
 * @see ContentParser
 */
@Getter
public class JTable {

    /**
     * Rows belonging to the table
     */
    private List<JRow> rows = new ArrayList<>();

    /**
     * Width of the table. Currently this is required but configurable.
     */
    private int width = 100;

    /**
     * Theme to be applied to the table
     */
    private JTheme theme = SINGLE_LINE;

    /**
     * Content parser which understands certain magic strings that do not contribute to the overall
     * content length
     */
    private ContentParser contentParser = new PlainContextParser();

    public static JTable of() {
        return new JTable();
    }

    public JRow row() {
        JRow newRow = new JRow(this);
        rows.add(newRow);
        return newRow;
    }

    public JTable width(int width) {
        this.width = width;
        return this;
    }

    public JTable theme(JTheme theme) {
        this.theme = theme;
        return this;
    }

    public JTable contentParser(ContentParser contentParser) {
        this.contentParser = contentParser;
        return this;
    }

    public List<String> render() {
        List<String> result = new ArrayList<>();

        // Used to determine the bottom border intercept connectors
        int[] colSizes = null;

        for (JRow row : rows) {
            int[] previousColSizes = colSizes;

            // If the table width is not evenly distributable between the columns, the leftover
            // will be applied to the first column
            colSizes = colSizes(width, row.getCols());

            String topBorder = renderTopBorder(colSizes, previousColSizes, width, theme);
            if (!topBorder.trim().isEmpty()) {
                result.add(topBorder);
            }

            // Each column rendered without borders
            List<RenderedCol> colResult = new ArrayList<>();
            int maxHeight = 0;

            Iterator<JCol> colIter = row.getCols().iterator();

            int colIdx = 0;
            while (colIter.hasNext()) {
                JCol col = colIter.next();
                int widthToUse = colSizes[colIdx];
                RenderedCol renderedCol = new RenderedCol(col, widthToUse,
                                                          col.render(theme, widthToUse));
                maxHeight = Math.max(maxHeight, renderedCol.getHeight());
                colResult.add(renderedCol);
                colIdx++;
            }

            final int finalMaxHeight = maxHeight;
            List<List<String>> colLines =
                    colResult.stream()
                             .map(cr -> cr.getCol().render(theme, cr.widthToUse, finalMaxHeight))
                             .collect(Collectors.toList());

            for (int rowNum = 0; rowNum < finalMaxHeight; rowNum++) {
                StringBuilder rowBuilder = new StringBuilder();
                for (List<String> colLine : colLines) {
                    rowBuilder.append(colLine.get(rowNum));
                }
                safeAppend(rowBuilder, theme.vertical);
                result.add(rowBuilder.toString());
            }

        }

        String bottomBorder = renderBottomBorder(colSizes, theme);
        if (!bottomBorder.trim().isEmpty()) {
            result.add(bottomBorder);
        }
        return result;
    }

    private int[] colSizes(int tableWidth, List<JCol> cols) {
        int[] result = new int[cols.size()];
        int remainingWidth = tableWidth;
        List<Integer> dynamicCols = new ArrayList<>();

        for (int i = 0; i < cols.size(); i++) {
            JCol col = cols.get(i);
            if (col.getWidth() == null) {
                dynamicCols.add(i);
            }
            else {
                result[i] = col.getWidth();
                remainingWidth -= col.getWidth();
            }
        }

        if (!dynamicCols.isEmpty()) {
            int leftoverWidth = remainingWidth % dynamicCols.size();
            int colWidth = remainingWidth / dynamicCols.size();

            for (Integer dynamicIndex : dynamicCols) {
                result[dynamicIndex] = colWidth;
            }
            if (leftoverWidth > 0) {
                result[result.length - 1] = result[result.length - 1] + leftoverWidth;
            }
        }
        int rowWidth = Arrays.stream(result).sum();
        if (rowWidth != tableWidth) {
            throw new IllegalArgumentException("Row width does not match table width. " +
                                               "Expected " + tableWidth + " but got " + rowWidth);
        }

        return result;
    }

    public static String renderBottomBorder(int[] colsWidth, Chars theme) {
        StringBuilder builder = new StringBuilder();
        for (int aColsWidth : colsWidth) {
            if (builder.length() == 0) {
                safeAppend(builder, theme.bottomLeft);
            }
            else {
                safeAppend(builder, theme.bottomIntersect);
            }
            safeAppend(builder, theme.horizontal, aColsWidth);
        }
        safeAppend(builder, theme.bottomRight);

        return builder.toString();
    }

    public static String renderTopBorder(int[] colsWidth, int[] previousColsWidth, int tableWidth,
                                         Chars theme)
    {
        StringBuilder builder = new StringBuilder();
        for (int aColsWidth : colsWidth) {
            if (builder.length() == 0) {
                safeAppend(builder, theme.topLeft);
            }
            else {
                safeAppend(builder, theme.topIntersect);
            }
            safeAppend(builder, theme.horizontal, aColsWidth);
        }
        safeAppend(builder, theme.topRight);

        if (previousColsWidth != null) {
            safeInsert(builder, 0, theme.leftIntersect);
            int prevIdx = 1;

            for (int prevColWidth : previousColsWidth) {
                prevIdx += prevColWidth;
                if (prevIdx > builder.length()) {
                    // noop
                }
                else if (builder.charAt(prevIdx) == theme.topIntersect) {
                    safeInsert(builder, prevIdx, theme.intersect);
                }
                else if (builder.charAt(prevIdx) == theme.horizontal) {
                    safeInsert(builder, prevIdx, theme.bottomIntersect);
                }
                else if (builder.charAt(prevIdx) == theme.topRight) {
                    safeInsert(builder, prevIdx, theme.rightIntersect);
                }
                else {
                    throw new IllegalArgumentException("Do not know how to replace '" +
                                                       builder.charAt(prevIdx) + "'");
                }
                prevIdx++;
            }

        }

        return builder.toString();
    }

    private static void safeInsert(StringBuilder builder, int idx, char character) {
        if (character != Character.MIN_VALUE) {
            builder.setCharAt(idx, character);
        }
    }

    private static void safeAppend(StringBuilder builder, char character, int repeat) {
        if (character != Character.MIN_VALUE) {
            builder.append(StringUtils.repeat(character, repeat));
        }
    }

    private static void safeAppend(StringBuilder builder, char character) {
        safeAppend(builder, character, 1);
    }

    @Data
    private static class RenderedCol {
        private final JCol col;
        private final int widthToUse;
        private final List<String> renderedCol;

        public int getHeight() {
            return renderedCol.size();
        }
    }

}
