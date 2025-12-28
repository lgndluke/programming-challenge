package de.exxcellent.challenge.event_handlers;

import de.exxcellent.challenge.data.DataFrame;
import de.exxcellent.challenge.events.CsvFileImportEvent;
import de.exxcellent.challenge.framework.EventHandler;
import de.exxcellent.challenge.framework.EventDispatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * EventHandler for {@link CsvFileImportEvent}. <br>
 * <br>
 * Handles {@link CsvFileImportEvent}s by asynchronously reading their CSV file and converting the read file contents
 * into a {@link DataFrame}.
 *
 * @author Lukas Jeckle
 **/
public class CsvFileImportEventHandler implements EventHandler<CsvFileImportEvent>
{

    private static final String COMMA_DELIMITER = ",";
    private static final String FILE_EXTENSION  = ".csv";

    /**
     * Invoked by the {@link EventDispatcher} whenever a {@link CsvFileImportEvent} is fired. <br>
     * Prerequisites: A {@link CsvFileImportEventHandler} has been registered with the {@link EventDispatcher} <br>
     *
     * @param event The {@link CsvFileImportEvent} to be handled.
     * @return      A {@link CompletableFuture} completing with a {@link DataFrame} object which either contains the CSV
     *              file contents, or is empty if an error had occurred.
     **/
    @Override
    public CompletableFuture<DataFrame> onEvent(CsvFileImportEvent event)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            String filePath = event.getFilePath();

            try
            {
                validateFilePath(filePath);
                return readCsvFileToDataFrame(filePath);
            }
            catch (IllegalArgumentException | IOException exception)
            {
                System.out.println(exception.getMessage());
                return new DataFrame();
            }
        });
    }

    /**
     * Private method to validate the {@link CsvFileImportEvent}s file path. <br>
     *
     * Validation checks performed:
     * <ul>
     *     <li>Provided file path is not null and not empty.</li>
     *     <li>Provided file path ends with {@value #FILE_EXTENSION}.</li>
     *     <li>Provided file path references an existing file and is a regular file.</li>
     * </ul>
     *
     * @param filePath The {@link CsvFileImportEvent}s file path.
     * @throws IllegalArgumentException if any validation rule is violated.
     **/
    private void validateFilePath(String filePath) throws IllegalArgumentException
    {
        if (filePath == null || filePath.isEmpty())
        {
            throw new IllegalArgumentException(
                    "CsvFileImportEventHandler::validateFilePath: The provided file path is null or empty!"
            );
        }

        if (!filePath.endsWith(FILE_EXTENSION))
        {
            throw new IllegalArgumentException(
                    String.format(
                            "CsvFileImportEventHandler::validateFilePath: Path '%s' does not end with '%s'!",
                            filePath,
                            FILE_EXTENSION
                    )
            );
        }

        File fileToCheck = new File(filePath);
        if (!fileToCheck.exists() || !fileToCheck.isFile())
        {
            throw new IllegalArgumentException(
                    String.format(
                            "CsvFileImportEventHandler::validateFilePath: File '%s' does not exist or is not a file!",
                            filePath
                    )
            );
        }
    }

    /**
     * Private method to read the {@link CsvFileImportEvent}s CSV file and convert its contents into a {@link DataFrame}.
     *
     * @param filePath The {@link CsvFileImportEvent}s file path.
     * @return A {@link DataFrame} containing the CSV file contents.
     * @throws IOException if an I/O error occurs while reading the file.
     **/
    private DataFrame readCsvFileToDataFrame(String filePath) throws IOException
    {
        DataFrame resultsFrame = new DataFrame();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            List<String>   headers      = Arrays.asList(reader.readLine().split(COMMA_DELIMITER));
            List<String>[] columnValues = new ArrayList[headers.size()];

            for (String header : headers)
            {
                columnValues[headers.indexOf(header)] = new ArrayList<>();
            }

            String readLine;

            while ((readLine = reader.readLine()) != null)
            {
                List<String> currentLineValues = Arrays.asList(readLine.split(COMMA_DELIMITER));

                for (String header : headers)
                {
                    columnValues[headers.indexOf(header)].add(currentLineValues.get(headers.indexOf(header)));
                }

            }

            for (String header : headers)
            {
                resultsFrame.putColumnValues(header, columnValues[headers.indexOf(header)]);
            }

        }
        catch (IOException io)
        {
            throw new IOException(
                    String.format(
                            "CsvFileImportEventHandler::readCsvFileToDataFrame: An error occurred whilst reading file '%s'",
                            filePath
                    ),
                    io
            );
        }

        return resultsFrame;
    }

}
