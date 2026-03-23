package cat.udl.eps.softarch.fll.repository.identity;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import cat.udl.eps.softarch.fll.domain.Administrator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Administrators", description = "Repository for managing Administrator entities")
@RepositoryRestResource
public interface AdministratorRepository
		extends CrudRepository<Administrator, String>, PagingAndSortingRepository<Administrator, String> {

	@Operation(summary = "Search administrators by username",
			description = "Returns a list of Administrators whose usernames contain the specified text.")
	List<Administrator> findByIdContaining(@Param("text") String text);
}
