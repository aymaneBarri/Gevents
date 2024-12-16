package fsk.glcc.gevents.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
	PARTICIPANT(Set.of(
				Permission.EVENEMENT_READ
			)),
//	PARTICIPANT(Collections.emptySet()),
	ORGANISATEUR(Set.of(
				Permission.EVENEMENT_CREATE,
				Permission.EVENEMENT_READ,
				Permission.EVENEMENT_UPDATE,
				Permission.EVENEMENT_DELETE
			));
	
	Role(Set<Permission> permissions){
		this.permissions = permissions;
	}
	
	private final Set<Permission> permissions;
	
	public Set<Permission> getPermissions(){
		return this.permissions;
	}
	
	public List<SimpleGrantedAuthority> getAuthorities(){
		var authorities = getPermissions()
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
				.collect(Collectors.toList());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		
		return authorities;
	}
}
