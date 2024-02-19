import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LoadHandlerTest {

    @Mock
    private Logger mockLogger;
    private LoadHandler loadHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        loadHandler = new LoadHandler(mockLogger);
    }

    @Test
    void testLoadValidCSVFile() {
        // Assuming method to set file path for testing exists
        loadHandler.setFilePath("path/to/RI_Town_Income.csv");
        loadHandler.loadCSV("RI_Town_Income.csv", "true");

        assertTrue(loadHandler.getIsLoaded());
        assertNotNull(loadHandler.getDataPacket());
        // Additional checks on the content of dataPacket can be added here
    }

    @Test
    void testLoadNonexistentCSVFile() {
        loadHandler.setFilePath("nonexistent.csv");
        assertThrows(FileNotFoundException.class, () -> loadHandler.loadCSV("nonexistent.csv", "true"));
    }

    @Test
    void testInvalidHasHeaderString() {
        loadHandler.setFilePath("path/to/RI_Town_Income.csv");
        assertThrows(IllegalArgumentException.class, () -> loadHandler.loadCSV("RI_Town_Income.csv", "invalid"));
    }

    @Test
    void testPathTraversalAttackPrevention() {
        loadHandler.setFilePath("../../etc/passwd");
        assertThrows(SecurityException.class, () -> loadHandler.loadCSV("../../etc/passwd", "true"));
    }
}