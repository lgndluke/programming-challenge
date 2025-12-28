package de.exxcellent.challenge.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link DataFrame} test class.
 *
 * @author Lukas Jeckle
 **/
public class DataFrameTest
{

    private static DataFrame dataFrame;
    private static final List<String> columnNames = new ArrayList<>(Arrays.asList("Column 1", "Column 2", "Column 3"));
    private static final List<String> column1Vals = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));
    private static final List<String> column2Vals = new ArrayList<>(Arrays.asList("value4", "value5", "value6"));
    private static final List<String> column3Vals = new ArrayList<>(Arrays.asList("value7", "value8", "value9"));
    private static final String emptyDataFrameToStringValue = "DataFrame: []";

    @BeforeEach
    void setUp()
    {
        dataFrame = new DataFrame();
    }

    @Test
    void instantiated()
    {
        assertNotNull(dataFrame);
    }

    @Test
    void putColumnValuesInDataFrame()
    {
        dataFrame.putColumnValues(columnNames.get(0), column1Vals);
        dataFrame.putColumnValues(columnNames.get(1), column2Vals);
        dataFrame.putColumnValues(columnNames.get(2), column3Vals);

        /// Null Checks
        List<String> dataFrameColumnNames   = dataFrame.getColumnNames();
        List<String> dataFrameColumn1Values = dataFrame.getColumnValues(columnNames.get(0));
        List<String> dataFrameColumn2Values = dataFrame.getColumnValues(columnNames.get(1));
        List<String> dataFrameColumn3Values = dataFrame.getColumnValues(columnNames.get(2));

        assertNotNull(dataFrameColumnNames);
        assertNotNull(dataFrameColumn1Values);
        assertNotNull(dataFrameColumn2Values);
        assertNotNull(dataFrameColumn3Values);

        /// Content checks
        assert dataFrameColumnNames.containsAll(columnNames);
        assert dataFrameColumn1Values.containsAll(column1Vals);
        assert dataFrameColumn2Values.containsAll(column2Vals);
        assert dataFrameColumn3Values.containsAll(column3Vals);
    }

    @Test
    void toStringEmptyDataFrame()
    {
        DataFrame emptyDataFrame = new DataFrame();
        assertEquals(emptyDataFrameToStringValue, emptyDataFrame.toString());
    }

    @Test
    void toStringFilledDataFrame()
    {
        dataFrame.putColumnValues(columnNames.get(0), column1Vals);
        dataFrame.putColumnValues(columnNames.get(1), column2Vals);
        dataFrame.putColumnValues(columnNames.get(2), column3Vals);

        assertNotEquals(emptyDataFrameToStringValue, dataFrame.toString());
    }

    @Test
    void getColumnNames()
    {
        dataFrame.putColumnValues(columnNames.get(0), column1Vals);
        dataFrame.putColumnValues(columnNames.get(1), column2Vals);
        dataFrame.putColumnValues(columnNames.get(2), column3Vals);

        /// Null Check
        List<String> dataFrameColumnNames = dataFrame.getColumnNames();

        assertNotNull(dataFrameColumnNames);

        /// Value Check
        assert dataFrameColumnNames.containsAll(columnNames);
    }

    @Test
    void getColumnValues()
    {
        dataFrame.putColumnValues(columnNames.get(0), column1Vals);
        dataFrame.putColumnValues(columnNames.get(1), column2Vals);
        dataFrame.putColumnValues(columnNames.get(2), column3Vals);

        /// Null Checks
        List<String> dataFrameColumn1Values = dataFrame.getColumnValues(columnNames.get(0));
        List<String> dataFrameColumn2Values = dataFrame.getColumnValues(columnNames.get(1));
        List<String> dataFrameColumn3Values = dataFrame.getColumnValues(columnNames.get(2));

        assertNotNull(dataFrameColumn1Values);
        assertNotNull(dataFrameColumn2Values);
        assertNotNull(dataFrameColumn3Values);

        /// Content checks
        assert dataFrameColumn1Values.containsAll(column1Vals);
        assert dataFrameColumn2Values.containsAll(column2Vals);
        assert dataFrameColumn3Values.containsAll(column3Vals);
    }

}
