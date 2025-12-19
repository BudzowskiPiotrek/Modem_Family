package metroMalaga.Model;

/**
 * Represents a user role within the system, defining specific access levels.
 * This class handles the permission set for different users, determining 
 * their ability to download, modify, or delete data based on their assigned role.
 */
public class Rol {
	private int id;
	private String permiso;
	private String nombre;
	private boolean canDownload;
	private boolean canModify;
	private boolean canDelete;

	/**
	 * Constructs a Rol with basic information and default restricted permissions.
	 * @param id      The unique database identifier for the role.
	 * @param permiso The permission level string (e.g., "admin", "usuario").
	 * @param nombre  The display name of the role.
	 */
	public Rol(int id, String permiso, String nombre) {
		super();
		this.id = id;
		this.permiso = permiso;
		this.nombre = nombre;
		this.canDownload = true;
		this.canModify = false;
		this.canDelete = false;
	}

	/**
	 * Constructs a Rol with a full set of explicit permissions.
	 * @param id          The unique database identifier for the role.
	 * @param permiso     The permission level string.
	 * @param nombre      The display name of the role.
	 * @param canDownload Permission to download files or data.
	 * @param canModify   Permission to update existing records.
	 * @param canDelete   Permission to remove records from the system.
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
	 * Retrieves the role identifier.
	 * @return The integer ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Updates the role identifier.
	 * @param id The new integer ID.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Retrieves the permission string.
	 * @return The permission level.
	 */
	public String getPermiso() {
		return permiso;
	}

	/**
	 * Updates the permission string.
	 * @param permiso The new permission level.
	 */
	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}

	/**
	 * Retrieves the display name of the role.
	 * @return The role name.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Updates the display name of the role.
	 * @param nombre The new role name.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Checks if the role has download privileges.
	 * @return true if allowed, false otherwise.
	 */
	public boolean isCanDownload() {
		return canDownload;
	}

	/**
	 * Updates the download privilege for the role.
	 * @param canDownload The new boolean permission state.
	 */
	public void setCanDownload(boolean canDownload) {
		this.canDownload = canDownload;
	}

	/**
	 * Checks if the role has modification privileges.
	 * @return true if allowed, false otherwise.
	 */
	public boolean isCanModify() {
		return canModify;
	}

	/**
	 * Updates the modification privilege for the role.
	 * @param canModify The new boolean permission state.
	 */
	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	/**
	 * Checks if the role has deletion privileges.
	 * @return true if allowed, false otherwise.
	 */
	public boolean isCanDelete() {
		return canDelete;
	}

	/**
	 * Updates the deletion privilege for the role.
	 * @param canDelete The new boolean permission state.
	 */
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

}