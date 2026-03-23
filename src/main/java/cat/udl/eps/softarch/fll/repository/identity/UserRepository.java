package cat.udl.eps.softarch.fll.repository.identity;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import cat.udl.eps.softarch.fll.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "Repository for managing User entities")
@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, String>, PagingAndSortingRepository<User, String> {

	/*
	 * Interface provides automatically, as defined in
	 * https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html and
	 * https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html the methods: count,
	 * delete, deleteAll, deleteById, existsById, findAll, findAllById, findById, save, saveAll,...
	 *
	 * Additional methods like findByUsernameContaining can be defined following:
	 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	 */

	@Operation(summary = "Search users by username",
			description = "Returns a list of Users whose usernames contain the specified text.")
	List<User> findByIdContaining(@Param("text") String text);
}
