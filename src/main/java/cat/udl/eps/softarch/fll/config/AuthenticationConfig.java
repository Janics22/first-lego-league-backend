package cat.udl.eps.softarch.fll.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import cat.udl.eps.softarch.fll.domain.User;
import cat.udl.eps.softarch.fll.repository.identity.UserRepository;

@Configuration
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

	@Value("${default-password}")
	String defaultPassword;

	final BasicUserDetailsService basicUserDetailsService;
	final UserRepository userRepository;

	public AuthenticationConfig(BasicUserDetailsService basicUserDetailsService, UserRepository userRepository) {
		this.basicUserDetailsService = basicUserDetailsService;
		this.userRepository = userRepository;
	}

	@Override
	public void init(AuthenticationManagerBuilder auth) {
		auth.userDetailsService(basicUserDetailsService)
				.passwordEncoder(User.passwordEncoder);
	}
}
