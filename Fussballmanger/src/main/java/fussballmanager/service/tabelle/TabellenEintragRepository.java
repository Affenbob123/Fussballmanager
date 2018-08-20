package fussballmanager.service.tabelle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.team.Team;

@Repository
public interface TabellenEintragRepository extends JpaRepository<TabellenEintrag, Long> {

	List<TabellenEintrag> findByLigaAndSaison(Liga liga, Saison saison);

	TabellenEintrag findByTeamAndSaison(Team team, Saison saison);

	List<TabellenEintrag> findByLiga(Liga liga);
}
