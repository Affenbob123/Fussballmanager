package fussballmanager.service.spieler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpielerRepository extends JpaRepository<Spieler, Long> {

}
