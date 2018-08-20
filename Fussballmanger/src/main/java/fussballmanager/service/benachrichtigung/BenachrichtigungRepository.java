package fussballmanager.service.benachrichtigung;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fussballmanager.service.team.Team;

public interface BenachrichtigungRepository  extends JpaRepository<Benachrichtigung, Long> {

	List<Benachrichtigung> findByGelesenAndEmpfaengerIn(boolean b, List<Team> teamsDesEmpfaengers);
}
