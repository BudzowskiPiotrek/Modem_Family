package metroMalaga.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CrudControllerTest {
	private CrudController controller;
	private CrudFrontendFake vistaFake;

	@BeforeEach
	public void setUp() {
		vistaFake = new CrudFrontendFake();
		controller = new CrudController(vistaFake, null);
	}

	@Test
	public void testSeleccionDeTabla() {
		String seleccionada = vistaFake.getTablaSeleccionada();
		assertEquals("usuarios", seleccionada, "La tabla seleccionada debería ser 'usuarios'");
	}

	@Test
	public void testRecuperarDatosFormulario() {
	    vistaFake.setDatosSimulados(Arrays.asList("1", "Admin", "Pass123"));

	    java.util.List<String> datos = vistaFake.getDatosFormulario();

	    assertEquals(3, datos.size());
	    assertEquals("Admin", datos.get(1));
	}

	@Test
	public void testControladorInicializado() {
		assertNotNull(controller, "El controlador debería haberse creado correctamente");
	}
}
