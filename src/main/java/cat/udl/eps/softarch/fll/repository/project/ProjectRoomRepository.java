package cat.udl.eps.softarch.fll.repository.project;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import cat.udl.eps.softarch.fll.domain.ProjectRoom;

@RepositoryRestResource
public interface ProjectRoomRepository extends CrudRepository<ProjectRoom, String>, PagingAndSortingRepository<ProjectRoom, String> {

	List<ProjectRoom> findAll();

}
