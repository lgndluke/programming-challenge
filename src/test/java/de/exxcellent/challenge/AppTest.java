package de.exxcellent.challenge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link App} test class.
 *
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>, Lukas Jeckle
 */
class AppTest
{

    @Test
    void runApp()
    {
        assertDoesNotThrow(() -> App.main());
    }

}