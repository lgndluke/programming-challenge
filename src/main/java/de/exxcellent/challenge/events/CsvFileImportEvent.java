package de.exxcellent.challenge.events;

import de.exxcellent.challenge.framework.Event;

/**
 * An immutable {@link Event} implementation that signals a request to import a CSV file.
 *
 * @author Lukas Jeckle
 **/
public class CsvFileImportEvent extends Event
{

    private final String filePath;

    /**
     * @param filePath The (absolute/relative) file path of the CSV file to be imported.
     **/
    public CsvFileImportEvent(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * @return The events (absolute/relative) CSV file path that was provided at instantiation.
     **/
    public String getFilePath()
    {
        return filePath;
    }

}
