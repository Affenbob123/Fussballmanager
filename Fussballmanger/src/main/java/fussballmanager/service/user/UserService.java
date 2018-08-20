package fussballmanager.service.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.chat.ChatService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class UserService implements UserDetailsService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	ChatService chatService;
	
	private static final Collection<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
	private static final Collection<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");
	
	@Value("${fussballmanager.admin.login}")
	String adminLogin;

	@Value("${fussballmanager.admin.password}")
	String adminPassword;
	
	@PostConstruct
	public void checkAdminAccount() {
		if (!userRepository.findById(adminLogin).isPresent()) {		
			userRepository.save(new User(adminLogin, "{noop}" + adminPassword, true, "admin", ""));
		}
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Optional<User> findeUser = userRepository.findById(login);
		if (findeUser.isPresent()) {
			User user = findeUser.get();
			return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
					user.isAdmin() ? ADMIN_ROLES : USER_ROLES);
		} else {
			throw new UsernameNotFoundException("");
		}
	}
	
	public User findeUser(String login) {
		return userRepository.getOne(login);
	}
	
	public List<User> findeAlleNormalenUser() {
		return userRepository.findByIsAdmin(false);
	}
	
	public void legeUserAn(User user) {
		user.setPassword("{noop}" + user.getPassword());
		
		userRepository.save(user);
		LOG.info("User: {} wurde angelegt.", user.getLogin());
		
		teamService.erstelleStandardHauptteamFuerEinenUser(findeUser(user.getLogin()));
		chatService.fuegeUserAlleChatHinzu(user);
	}
	
	public void aktualisiereUser(User user) {
		user.setPassword("{noop}" + user.getPassword());
		userRepository.save(user);
	}
	
	public void loescheUser(User user) {
		userRepository.delete(user);
	}
	
	public String getLoginDurchUsername(String username) {
		for(User user : findeAlleNormalenUser()) {
			if(user.getUsername().equals(username)) {
				return user.getLogin();
			}
		}
		return null;
	}
	
	public void kauftProtage(User user, int protage) {
		user.setProtage(user.getProtage() + protage);
		aktualisiereUser(user);
	}
	
	public void aufgabenBeiSpieltagWechsel() {
		List<User> alleUser = findeAlleNormalenUser();
		
		for(User user : alleUser) {
			List<Team> alleTeamsDesUsers = teamService.findeAlleTeamsEinesUsers(user);
			int anzahlTeams = alleTeamsDesUsers.size();
			int zahlungsPflichtigeTeams = anzahlTeams - 6;
			
			if(zahlungsPflichtigeTeams > 0) {
				int protageKosten = zahlungsPflichtigeTeams / 3;
				user.setProtage(user.getProtage() - protageKosten);
				if(user.getProtage() < 0) {
					teamService.sperreTeamsAufgrundZuWenigerProtage(user);
				}
				aktualisiereUser(user);
			}
		}
	}
}
