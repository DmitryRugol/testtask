package by.dmitryrugol.testtask.jwt.service;

import java.util.ArrayList;

import by.dmitryrugol.testtask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	UserRepository userRepository;

	public JwtUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		by.dmitryrugol.testtask.entity.User u = userRepository
				.findByEmailOrPhone(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		return new User(username, u.getPassword(), new ArrayList<>());

//		if ("javainuse".equals(username)) {
////			return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
////					new ArrayList<>());
//			return new User("javainuse", "$2a$12$FxdqRboT0oZMv7x6KZB9RuobpsYlAu4OIoGUUBPOd26tbyIOgGmaK",
//					new ArrayList<>());
//
//		} else {
//			throw new UsernameNotFoundException("User not found with username: " + username);
//		}
	}

}