package fussballmanager.service.finanzen;

import org.springframework.data.jpa.repository.JpaRepository;

import fussballmanager.service.team.Team;

public interface BilanzRepository extends JpaRepository<Bilanz, Long> {

}
