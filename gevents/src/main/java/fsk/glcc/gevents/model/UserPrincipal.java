package fsk.glcc.gevents.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserPrincipal implements UserDetails{

	private Utilisateur utilisateur;
	
	public UserPrincipal() {
		
	}
	
	public UserPrincipal(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return utilisateur.getRole().getAuthorities();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return utilisateur.getMotDePasse();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return utilisateur.getEmail();
	}
	
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

}
