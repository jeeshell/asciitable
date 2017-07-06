package net.je2sh.asciitable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import net.je2sh.asciitable.style.JPadding;
import net.je2sh.asciitable.style.JTheme;



/**
 * Representation of a table row. May contain multiple columns
 *
 * @see JCol
 * @see JTable
 */
@Getter
@Setter
public class JRow {

    /**
     * Parent table
     */
    private final JTable table;

    /**
     * List of columns belonging to this row
     */
    private List<JCol> cols = new ArrayList<>();

    /**
     * Explicit padding for this row. If {@code null} it will default to {@link JTheme#getPadding()}
     */
    private JPadding padding;

    public JRow(JTable table) {
        this.table = table;
    }

    public JCol col() {
        JCol newcell = new JCol(this);
        cols.add(newcell);
        return newcell;
    }

    public JRow padding(JPadding padding) {
        this.padding = padding;
        return this;
    }

    public JPadding getPadding() {
        return Optional.ofNullable(padding).orElse(table.getTheme().getPadding());
    }

    public JTable done() {
        return table;
    }
}
