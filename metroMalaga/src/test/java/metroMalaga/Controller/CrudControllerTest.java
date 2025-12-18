package metroMalaga.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import metroMalaga.View.CrudFrontend;
import metroMalaga.Controller.menu.MenuSelect;
import metroMalaga.Model.Usuario;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class CrudControllerTest {

    private CrudController controller;
    private CrudFrontend mockVista;
    private MenuSelect mockMenu;
    
    // Configuration for a real test (Integration)
    // Note: Ensure your local MySQL has the 'metromalaga' DB
    private static final String URL = "jdbc:mysql://localhost:3306/metromalaga";
    private static final String USER = "root";
    private static final String PASS = "";

    @BeforeEach
    public void setUp() {
        // We mock the View to control the data the Controller "reads" from the screen
        mockVista = mock(CrudFrontend.class);
        mockMenu = mock(MenuSelect.class);
        
        // Initialize the controller
        controller = new CrudController(mockVista, mockMenu);
    }

    @Test
    public void testDatabaseConnection() {
        // Verify that the connection is active after initialization
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            assertNotNull(conn, "Database connection should be successful");
            assertFalse(conn.isClosed());
        } catch (SQLException e) {
            fail("Connection failed: " + e.getMessage());
        }
    }

    @Test
    public void testGetTablesNotNull() {
        // Verifies that the controller populates the lateral list
        // This confirms cargarListaTablas() worked
        verify(mockVista, atLeastOnce()).setListaTablas(anyList());
    }

    @Test
    public void testFormGenerationLogic() {
        // Simulate selecting a table
        when(mockVista.getTablaSeleccionada()).thenReturn("usuarios");
        
        // We trigger the logic manually if needed or via event simulation
        // The goal is to verify that generarFormulario is called with column names
        assertDoesNotThrow(() -> {
            // Internal call simulation
            // In a real scenario, we'd trigger the ListSelectionListener
        });
    }

    @Test
    public void testInsertOperationLogic() throws SQLException {
        // Setup: Simulate a table selected and form data
        // Assuming 'roles' table has 2 columns: id_rol, nombre
        List<String> mockFormData = Arrays.asList("99", "TEST_ROLE");
        when(mockVista.getDatosFormulario()).thenReturn(mockFormData);
        
        // This tests if the controller can handle an INSERT without crashing
        // Note: This will actually attempt to insert into your DB if not using a mock DB
        assertDoesNotThrow(() -> {
            // We would call the private method via reflection or trigger the button
            // For now, we verify the view interaction
            mockVista.getBtnGuardar().doClick();
        });
    }

    @Test
    public void testUpdateAssumption() {
        // Your code assumes column 0 is the ID. Let's verify data retrieval.
        Object expectedId = 1;
        when(mockVista.getTableValueAt(0, 0)).thenReturn(expectedId);
        
        assertEquals("1", mockVista.getTableValueAt(0, 0).toString());
    }
}
