package fussballmanager.service.team.stadion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StadionRepository extends JpaRepository<Stadion, Long> {

	List<Stadion> findByAktuellAusgebauterTypNotNull();

	List<Stadion> findBySitzplatzAusbauTageGreaterThan(int i);

}
