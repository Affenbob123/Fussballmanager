package fussballmanager.service.spieler.staerke;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.spieler.Spieler;

@Repository
public interface SpielerStaerkeRepository  extends JpaRepository<SpielerStaerke, Long> {

	List<SpielerStaerke> findAllByOrderByReinStaerkeDesc(Pageable pageable);
}
