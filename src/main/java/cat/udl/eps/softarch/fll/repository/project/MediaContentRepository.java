package cat.udl.eps.softarch.fll.repository.project;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import cat.udl.eps.softarch.fll.domain.Edition;
import cat.udl.eps.softarch.fll.domain.MediaContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "MediaContents", description = "Repository for managing MediaContent entities")
@RepositoryRestResource
public interface MediaContentRepository extends CrudRepository<MediaContent, String>, PagingAndSortingRepository<MediaContent, String> {

	@Operation(summary = "Search media content by type",
			description = "Returns a list of MediaContent with the specified type.")
	List<MediaContent> findByType(@Param("type") String type);

	@Operation(summary = "Search media content by edition",
			description = "Returns a list of MediaContent belonging to the specified edition.")
	List<MediaContent> findByEdition(@Param("edition") Edition edition);

}
