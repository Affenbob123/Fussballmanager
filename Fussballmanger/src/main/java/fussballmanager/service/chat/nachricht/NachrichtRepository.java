package fussballmanager.service.chat.nachricht;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NachrichtRepository extends JpaRepository<Nachricht, Long> {

}
