package fussballmanager.service.tor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorRepository extends JpaRepository<Tor, Long> {

}
