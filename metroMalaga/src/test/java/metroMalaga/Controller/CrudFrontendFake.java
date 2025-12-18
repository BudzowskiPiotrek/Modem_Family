package metroMalaga.Controller;

import java.util.ArrayList;
import java.util.List;

import metroMalaga.View.CrudFrontend;

public class CrudFrontendFake extends CrudFrontend {
	private String tablaSimulada = "usuarios";
	private List<String> datosSimulados = new ArrayList<>();

	public CrudFrontendFake() {
		super(null);
	}

	@Override
	public String getTablaSeleccionada() {
		return tablaSimulada;
	}

	@Override
	public List<String> getDatosFormulario() {
		return datosSimulados;
	}

	public void setDatosSimulados(List<String> datos) {
		this.datosSimulados = datos;
	}

	@Override
	public void limpiarCamposFormulario() {
	}

	@Override
	public void actualizarTablaDatos(String[] c, Object[][] d) {
	}

	@Override
	public void generarFormulario(String[] n) {
	}
}
