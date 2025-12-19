package metroMalaga.Model;

/**
 * Model class representing a user role and its permissions.
 */
public class Rol {
	private int id;
	private String permiso;
	private String nombre;
	private boolean canDownload;
	private boolean canModify;
	private boolean canDelete;

	/**
	 * Constructor for Rol with default permissions.
	 * 
	 * @param id      The role ID.
	 * @param permiso The permission level.
	 * @param nombre  The role name.
	 */
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

	/**
	 * Constructor for Rol with specific permissions.
	 * 
	 * @param id          The role ID.
	 * @param permiso     The permission level.
	 * @param nombre      The role name.
	 * @param canDownload Permission to download files.
	 * @param canModify   Permission to modify files.
	 * @param canDelete   Permission to delete files.
	 */
	public Rol(int id, String permiso, String nombre, boolean canDownload, boolean canModify, boolean canDelete) {
		super();
		this.id = id;
		this.permiso = permiso;
		this.nombre = nombre;
		this.canDownload = canDownload;
		this.canModify = canModify;
		this.canDelete = canDelete;
	}

	/**
	 * Gets the role ID.
	 * 
	 * @return The ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the role ID.
	 * 
	 * @param id The ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the permission string.
	 * 
	 * @return The permission string.
	 */
	public String getPermiso() {
		return permiso;
	}

	/**
	 * Sets the permission string.
	 * 
	 * @param permiso The permission string.
	 */
	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}

	/**
	 * Gets the role name.
	 * 
	 * @return The name.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the role name.
	 * 
	 * @param nombre The name.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Checks if downloading is allowed.
	 * 
	 * @return true if allowed, false otherwise.
	 */
	public boolean isCanDownload() {
		return canDownload;
	}

	/**
	 * Sets the download permission.
	 * 
	 * @param canDownload true to allow, false otherwise.
	 */
	public void setCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}

	/**
	 * Checks if modification is allowed.
	 * 
	 * @return true if allowed, false otherwise.
	 */
	public boolean isCanModify() {
		return canModify;
	}

	/**
	 * Sets the modification permission.
	 * 
	 * @param canModify true to allow, false otherwise.
	 */
	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	/**
	 * Checks if deletion is allowed.
	 * 
	 * @return true if allowed, false otherwise.
	 */
	public boolean isCanDelete() {
		return canDelete;
	}

	/**
	 * Sets the deletion permission.
	 * 
	 * @param canDelete true to allow, false otherwise.
	 */
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

}
