package hr.tvz.pios.modul.user;

import hr.tvz.pios.modul.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void processOAuthPostLogin(String username) {
        Optional<User> existUser = userRepository.getByUsername(username);

        if (existUser.isEmpty()) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail("github account");
            newUser.setRole(new Role(2L, "USER"));

            userRepository.insert(newUser);
        }

    }
}
