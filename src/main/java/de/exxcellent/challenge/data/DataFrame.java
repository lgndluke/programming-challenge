package de.exxcellent.challenge.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The DataFrame class represents a simple key-value based data table.
 * It stores each column as {@code List<String>} values. <br>
 * <br>
 * <b>Important:</b>
 * <br>
 *   The order of the columns is determined by the underlying {@link ConcurrentHashMap}.
 *   Therefore, it is not guaranteed to match the insertion order!
 *
 * @author Lukas Jeckle
 **/
public class DataFrame
{

    /**
     * String returned by the {@link DataFrame#toString()} method when the {@link DataFrame} does not contain any columns.
     **/
    private static final String emptyDataFrameString = "DataFrame: []";

    /**
     * The column storage - thread-safe by design ({@link ConcurrentHashMap}).
     **/
    private final Map<String, List<String>> columns = new ConcurrentHashMap<>();

    /**
     * Method to insert or replace a column in the {@link DataFrame}.
     *
     * @param columnName The column name for which to insert or replace the provided values.
     * @param values     The list of string values to be put under the provided column name.
     **/
    public void putColumnValues(String columnName, List<String> values)
    {
        columns.put(columnName, values);
    }

    /**
     * Overridden {@link DataFrame#toString()} method.
     * @return The {@link DataFrame} represented as formatted string value.
     **/
    @Override
    public String toString()
    {
        if (columns.isEmpty())
            return emptyDataFrameString;


        StringBuilder builder = new StringBuilder();

        builder.append("\n").append("DataFrame:").append("\n");

        Map<String, Integer> columnWidths = new HashMap<>();
        for (String header : columns.keySet())
        {
            int maxHeaderWidth = header.length();
            int maxValueWidth  = columns.get(header).stream()
                    .map(Object::toString)
                    .map(String::length)
                    .max(Integer::compareTo)
                    .orElse(0);
            columnWidths.put(header, Math.max(maxHeaderWidth + 3, maxValueWidth + 3));
        }

        for (String header : columns.keySet())
        {
            builder.append(String.format("%-" + columnWidths.get(header) + "s", header));
        }
        builder.append("\n");

        int rows = columns.values().iterator().next().size();

        for (int i = 0; i < rows; i++)
        {
            for (Map.Entry<String, List<String>> entry : columns.entrySet())
            {
                List<?> values = entry.getValue();
                String value = (values.size() > i && values.get(i) != null) ? values.get(i).toString() : "N/A";
                builder.append(String.format("%-" + columnWidths.get(entry.getKey()) + "s", value));
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * @return The column names as {@code List<String>}. <br>
     *         <b>The import order is not preserved!</b>
     **/
    public List<String> getColumnNames()
    {
        return new ArrayList<>(columns.keySet());
    }

    /**
     * @param columnName The column name for which to retrieve the column values.
     * @return The column values corresponding to the provided param {@code columnName} as {@code List<String>}.
     **/
    public List<String> getColumnValues(String columnName)
    {
        return columns.get(columnName);
    }

}
