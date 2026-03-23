package cat.udl.eps.softarch.fll.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import cat.udl.eps.softarch.fll.repository.identity.UserRepository;

@Component
public class BasicUserDetailsService implements UserDetailsService {

	final UserRepository userRepository;

	public BasicUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}
}
