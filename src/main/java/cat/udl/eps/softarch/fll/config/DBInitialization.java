package cat.udl.eps.softarch.fll.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import cat.udl.eps.softarch.fll.domain.Administrator;
import cat.udl.eps.softarch.fll.domain.User;
import cat.udl.eps.softarch.fll.repository.identity.AdministratorRepository;
import cat.udl.eps.softarch.fll.repository.identity.UserRepository;
import jakarta.annotation.PostConstruct;

@Configuration
public class DBInitialization {
	@Value("${default-password}")
	String defaultPassword;

	@Value("${spring.profiles.active:}")
	private String activeProfiles;

	private final UserRepository userRepository;
	private final AdministratorRepository administratorRepository;

	public DBInitialization(UserRepository userRepository, AdministratorRepository administratorRepository) {
		this.userRepository = userRepository;
		this.administratorRepository = administratorRepository;
	}

	@PostConstruct
	public void initializeDatabase() {
		// Default administrator
		if (!administratorRepository.existsById("admin")) {
			Administrator admin = new Administrator();
			admin.setId("admin");
			admin.setEmail("admin@sample.app");
			admin.setPassword(defaultPassword);
			admin.encodePassword();
			administratorRepository.save(admin);
		}
		if (Arrays.asList(activeProfiles.split(",")).contains("test")) {
			// Testing instances
			if (!userRepository.existsById("demo")) {
				User user = new User();
				user.setEmail("demo@sample.app");
				user.setId("demo");
				user.setPassword(defaultPassword);
				user.encodePassword();
				userRepository.save(user);
			}
		}
	}
}
