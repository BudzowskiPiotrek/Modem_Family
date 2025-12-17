package metroMalaga.Model;

public class Rol {
	private int id;
	private String permiso;
	private String nombre;

	public Rol(int id, String permiso, String nombre) {
		super();
		this.id = id;
		this.permiso = permiso;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPermiso() {
		return permiso;
	}

	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
