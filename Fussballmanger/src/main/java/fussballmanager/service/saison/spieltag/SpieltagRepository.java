package fussballmanager.service.saison.spieltag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpieltagRepository extends JpaRepository<Spieltag, Long>{

}
