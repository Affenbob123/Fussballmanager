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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TeamService teamService;
	
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

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> findeMitspieler = userRepository.findById(username);
		if (findeMitspieler.isPresent()) {
			User user = findeMitspieler.get();
			return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
					user.isAdmin() ? ADMIN_ROLES : USER_ROLES);
		} else {
			throw new UsernameNotFoundException("");
		}
	}
	
	public User findeUser(String login) {
		return userRepository.getOne(login);
	}
	
	public User findeUserDurchUserName(String username) {
		return userRepository.getOne(getLoginDurchUsername(username));
	}
	
	public List<User> findeAlleNormalenUser() {
		return userRepository.findByIsAdmin(false);
	}
	
	public void legeUserAn(User user) {
		teamService.standardTeamsErstellen(user);
		
		user.setPassword("{noop}" + user.getPassword());
		userRepository.save(user);
		LOG.info("User: {} wurde angelegt.", user.getLogin());
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
}
