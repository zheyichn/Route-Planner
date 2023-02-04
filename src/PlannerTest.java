import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlannerTest {

    Planner p;
    OutputStream os;
    InputStream systemIn = System.in;
    PrintStream systemOut = System.out;

    @BeforeEach
    void SetUp() {
        p = new Planner();
        os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
    }

    @Test
    void TestOneWayTrip() {
        // provide input data
        String data = "19:55:00\nChestnut St & 23rd St\nChestnut St & 11th St\n1\nN\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        p.start();
        String outputString = os.toString();
        assertEquals("Reading stops information...\n" + "Finished reading stops information...\n"
                + "\n" + "Reading routes information...\n"
                + "Finished reading routes information...\n" + "\n"
                + "Reading trip information...\n" + "Finished reading trip information...\n" + "\n"
                + "Reading stop time information...\n"
                + "Finished reading stop time information...\n" + "\n"
                + "Please specify your target departure time in the format of HH:MM:SS\n"
                + "Where are you departing from?\n" + "Where is your destination?\n"
                + "Max number of transfers? Type 99 if it doesn't matter.\n"
                + "--- Start of itinerary ---\n"
                + "Trip number 0: Take Bus Route 9 from Chestnut St & 23rd St to Chestnut St & 11th St\n"
                + "    Expected departure: 20:00, Expected arrival: 20:04\n"
                + "--- End of itinerary ---\n"
                + "Do you want to find the best route for another itinerary? \"Y\" or \"N\"\n",
                outputString);
    }

    @Test
    void TestMultiTrip() {
        String data = "14:00:00\nChestnut St & 23rd St\n5th St & Market St\n1\nN\n";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        p.start();
        String outputString = os.toString();
        assertEquals("Reading stops information...\n" + "Finished reading stops information...\n"
                + "\n" + "Reading routes information...\n"
                + "Finished reading routes information...\n" + "\n"
                + "Reading trip information...\n" + "Finished reading trip information...\n" + "\n"
                + "Reading stop time information...\n"
                + "Finished reading stop time information...\n" + "\n"
                + "Please specify your target departure time in the format of HH:MM:SS\n"
                + "Where are you departing from?\n" + "Where is your destination?\n"
                + "Max number of transfers? Type 99 if it doesn't matter.\n"
                + "--- Start of itinerary ---\n"
                + "Trip number 0: Take Bus Route 9 from Chestnut St & 23rd St to Chestnut St & 9th St\n"
                + "    Expected departure: 14:00, Expected arrival: 14:06\n"
                + "Trip number 1: Take Bus Route 38 from Chestnut St & 9th St to 5th St & Market St\n"
                + "    Expected departure: 14:09, Expected arrival: 14:13\n"
                + "--- End of itinerary ---\n"
                + "Do you want to find the best route for another itinerary? \"Y\" or \"N\"\n",
                outputString);
    }

    @AfterEach
    void TearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
}
