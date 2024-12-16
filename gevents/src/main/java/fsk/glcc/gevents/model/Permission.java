package fsk.glcc.gevents.model;

public enum Permission {

	EVENEMENT_READ("evenement:read"),
	EVENEMENT_CREATE("evenement:create"),
	EVENEMENT_UPDATE("evenement:update"),
	EVENEMENT_DELETE("evenement:delete");
	
	private final String permission;
	
	Permission(String permission) {
		// TODO Auto-generated constructor stub
		this.permission = permission;
	}

	public String getPermission() {
		return this.permission;
	}
}
