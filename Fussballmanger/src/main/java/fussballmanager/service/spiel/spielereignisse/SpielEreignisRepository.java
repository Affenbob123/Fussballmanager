package fussballmanager.service.spiel.spielereignisse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpielEreignisRepository extends JpaRepository<SpielEreignis, Long> {

}
