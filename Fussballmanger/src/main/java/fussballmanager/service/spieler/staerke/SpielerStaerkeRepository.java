package fussballmanager.service.spieler.staerke;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpielerStaerkeRepository  extends JpaRepository<SpielerStaerke, Long> {

}
