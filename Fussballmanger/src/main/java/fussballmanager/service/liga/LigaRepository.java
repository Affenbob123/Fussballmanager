package fussballmanager.service.liga;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.land.Land;

@Repository
public interface LigaRepository extends JpaRepository<Liga, Long> {

	List<Liga> findByLand(Land land);
	
	Liga findByLandAndLigaNameTyp(Land land, LigenNamenTypen ligaNameTyp);
}
