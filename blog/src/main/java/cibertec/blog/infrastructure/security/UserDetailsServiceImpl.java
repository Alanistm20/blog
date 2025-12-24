package cibertec.blog.infrastructure.security;

import cibertec.blog.infrastructure.database.repositories.UserJpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userRepo;

    public UserDetailsServiceImpl(UserJpaRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no existe"));

        // IMPORTANTE: Spring espera ROLE_XXXX
        var authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return User.withUsername(user.getUsername())
                .password(user.getPassword())     // BCrypt en BD
                .authorities(authority)
                .disabled(!user.isEnabled())
                .build();
    }
}
