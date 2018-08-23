package fussballmanager.service.auktionshaus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuktionshausEintragRepository extends JpaRepository<AuktionshausEintrag, Long> {

	List<AuktionshausEintrag> findByFuerProtage(boolean b);

}
