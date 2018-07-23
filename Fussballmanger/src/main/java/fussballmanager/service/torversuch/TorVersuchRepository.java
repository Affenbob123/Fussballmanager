package fussballmanager.service.torversuch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorVersuchRepository extends JpaRepository<Torversuch, Long> {

}
