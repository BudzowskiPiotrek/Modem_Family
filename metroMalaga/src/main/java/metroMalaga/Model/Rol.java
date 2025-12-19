package metroMalaga.Model;

public class Rol {
	private int id;
	private String permiso;
	private String nombre;
	private boolean canDownload;
	private boolean canModify;
	private boolean canDelete;

	public Rol(int id, String permiso, String nombre) {
		super();
		this.id = id;
		this.permiso = permiso;
		this.nombre = nombre;
		// Default permissions (will be overridden by DB values)
		this.canDownload = true;
		this.canModify = false;
		this.canDelete = false;
	}

	public Rol(int id, String permiso, String nombre, boolean canDownload, boolean canModify, boolean canDelete) {
		super();
		this.id = id;
		this.permiso = permiso;
		this.nombre = nombre;
		this.canDownload = canDownload;
		this.canModify = canModify;
		this.canDelete = canDelete;
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

	public boolean isCanDownload() {
		return canDownload;
	}

	public void setCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}

	public boolean isCanModify() {
		return canModify;
	}

	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

}
