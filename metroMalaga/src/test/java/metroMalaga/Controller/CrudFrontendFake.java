package metroMalaga.Controller;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class CrudFrontendFake {

    private CrudController controller;
    private CrudFrontendFake vistaFake;

    @BeforeEach
    public void setUp() {
        // Usamos nuestra clase "mentirosa" en lugar de la real
        vistaFake = new CrudFrontendFake();
        // El controlador usará el fake pensando que es la vista real
        controller = new CrudController(vistaFake, null);
    }

    @Test
    public void testSeleccionDeTabla() {
        // Simulamos que el usuario elige "usuarios"
        String seleccionada = vistaFake.getTablaSeleccionada();
        assertEquals("usuarios", seleccionada, "La tabla seleccionada debería ser 'usuarios'");
    }

    @Test
    public void testRecuperarDatosFormulario() {
        // Preparamos datos en el fake
        vistaFake.setDatosSimulados(Arrays.asList("1", "Admin", "Pass123"));
        
        var datos = vistaFake.getDatosFormulario();
        
        assertEquals(3, datos.size());
        assertEquals("Admin", datos.get(1));
    }

    @Test
    public void testControladorInicializado() {
        assertNotNull(controller, "El controlador debería haberse creado correctamente");
    }
}