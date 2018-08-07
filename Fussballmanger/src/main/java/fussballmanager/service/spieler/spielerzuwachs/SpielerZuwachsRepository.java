package fussballmanager.service.spieler.spielerzuwachs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpielerZuwachsRepository extends JpaRepository<SpielerZuwachs, Long> {

}
