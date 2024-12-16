package fsk.glcc.gevents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fsk.glcc.gevents.model.UserPrincipal;
import fsk.glcc.gevents.model.Utilisateur;
import fsk.glcc.gevents.repository.UtilisateurRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UtilisateurRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Utilisateur utilisateur = repo.findByEmail(email);
		if(utilisateur == null) {
			throw new UsernameNotFoundException("Utilisateur non existant");
		}
		return new UserPrincipal(utilisateur);
	}

}
