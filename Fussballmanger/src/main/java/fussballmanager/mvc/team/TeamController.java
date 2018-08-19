package fussballmanager.mvc.team;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.spieler.spielerzuwachs.Trainingslager;
import fussballmanager.service.team.AusrichtungsTypen;
import fussballmanager.service.team.EinsatzTypen;
import fussballmanager.service.team.FormationsTypen;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class TeamController {
	
	private static final Logger LOG = LoggerFactory.getLogger(TeamController.class);
	
	@Autowired
	LandService landService;
	
	@Autowired
	LigaService ligaService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	

}
