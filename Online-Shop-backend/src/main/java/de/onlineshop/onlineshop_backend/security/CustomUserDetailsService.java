package de.onlineshop.onlineshop_backend.security;

import de.onlineshop.onlineshop_backend.model.User;          // ✅ your entity
import de.onlineshop.onlineshop_backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;            // ✅ explicit
import org.springframework.security.core.userdetails.UserDetailsService;   // ✅ explicit
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)  // ✅ now this is your entity
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email));

        // You can still return Spring Security's User implementation:
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
