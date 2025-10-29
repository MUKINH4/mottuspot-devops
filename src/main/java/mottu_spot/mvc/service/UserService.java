package mottu_spot.mvc.service;

import mottu_spot.mvc.model.User;
import mottu_spot.mvc.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Tentando carregar usuário: {}", username);
        
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

            logger.debug("Usuário encontrado: {} com role: {}", user.getUsername(), user.getRole());
            
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                    .build();
        } catch (Exception e) {
            logger.error("Erro ao carregar usuário {}: {}", username, e.getMessage());
            throw new UsernameNotFoundException("Erro ao carregar usuário: " + username, e);
        }
    }

    public User findByUsername(String username) {
        try {
            return userRepository.findByUsername(username).orElse(null);
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário {}: {}", username, e.getMessage());
            return null;
        }
    }
}
