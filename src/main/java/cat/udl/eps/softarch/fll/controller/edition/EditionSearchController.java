package cat.udl.eps.softarch.fll.controller.edition;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cat.udl.eps.softarch.fll.domain.edition.Edition;
import cat.udl.eps.softarch.fll.repository.edition.EditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EditionSearchController {

	private final EditionRepository editionRepository;

	@GetMapping("/editions/search")
	public ResponseEntity<Object> searchByVenueName(
		@RequestParam(name = "venueName") String venueName
	) {
		List<Edition> editions = editionRepository.findByVenueNameContainingIgnoreCase(venueName);
		return ResponseEntity.ok(Map.of("_embedded", Map.of("editions", editions)));
	}
}