package fussballmanager.service.personal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fussballmanager.service.team.Team;


@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long> {

	List<Personal> findByTeamAndPersonalTyp(Team team, PersonalTypen personalTyp);

}
