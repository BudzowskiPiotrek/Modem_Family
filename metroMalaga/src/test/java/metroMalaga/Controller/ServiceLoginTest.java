package metroMalaga.Controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import metroMalaga.Model.Usuario;

public class ServiceLoginTest {

	private ServiceLogin service;
	private Common cn;

	@BeforeEach
	public void setUp() {
		service = new ServiceLogin();
		cn = new Common();
	}

	@Test
	public void testLoginCorrecto() {
		boolean resultado = service.authenticateUser("admin", "admin123");
		assertTrue(resultado);
	}

	@Test
	public void testLoginIncorrecto() {
		boolean resultado = service.authenticateUser("noexiste", "malpassword");
		assertFalse(resultado);
	}

	@Test
	public void testUsuarioVacio() {
		boolean resultado = service.authenticateUser("", "password");
		assertFalse(resultado);
	}

	@Test
	public void testPasswordVacio() {
		boolean resultado = service.authenticateUser("admin", "");
		assertFalse(resultado);
	}

	@Test
	public void testAmbosVacios() {
		boolean resultado = service.authenticateUser("", "");
		assertFalse(resultado);
	}

	@Test
	public void testObtenerDatosUsuario() {
		Usuario usuario = service.getUserData("admin","");

		assertNotNull(usuario);
		assertEquals("admin", usuario.getUsernameApp());
		assertNotNull(usuario.getEmailReal());
		assertNotNull(usuario.getRol());
	}

	@Test
	public void testObtenerUsuarioNoExiste() {
		Usuario usuario = service.getUserData("usuarioInexistente999","");
		assertNull(usuario);
	}

	@Test
	public void testRegistrarLog() {
		assertDoesNotThrow(() -> {
			cn.registerLog("admin", "Test de login");
		});
	}

	@Test
	public void testRegistrarLogConValoresVacios() {
		assertDoesNotThrow(() -> {
			cn.registerLog("", "");
		});
	}

	@Test
	public void testUsuarioTieneRolValido() {
		Usuario usuario = service.getUserData("admin","");

		assertNotNull(usuario);
		assertNotNull(usuario.getRol());
		assertTrue(usuario.getRol().getId() > 0);
		assertFalse(usuario.getRol().getNombre().isEmpty());
	}
}
