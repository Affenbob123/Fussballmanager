package fussballmanager.service.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fussballmanager.service.user.User;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	List<Chat> findByUser(User user);

}
