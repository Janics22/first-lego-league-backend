package cat.udl.eps.softarch.fll.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rounds")
public class Round extends UriEntity<Long> {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Setter
	@Column(unique = true)
	private int number;

	@Getter
	@OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("round-matches")
	private List<Match> matches = new ArrayList<>();

	@Setter
	@Getter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "edition_id")
	@JsonIdentityReference(alwaysAsId = true)
	private Edition edition;

	public Round() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setMatches(List<Match> matches) {
		this.matches.clear();
		if (matches != null) {
			matches.forEach(this::addMatch);
		}
	}

	public void addMatch(Match match) {
		matches.add(match);
		match.setRound(this);
	}

	public void removeMatch(Match match) {
		matches.remove(match);
		match.setRound(null);
	}

}
