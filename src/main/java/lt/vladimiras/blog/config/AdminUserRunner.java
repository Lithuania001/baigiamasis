package lt.vladimiras.blog.config;

import lombok.RequiredArgsConstructor;
import lt.vladimiras.blog.model.Role;
import lt.vladimiras.blog.model.User;
import lt.vladimiras.blog.repository.RoleRepository;
import lt.vladimiras.blog.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminUserRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Optional<User> admin = userRepository.findByUsername("admin");
        if (admin.isPresent()) {
            return;
        }

        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap("admin")));
        user.setRoles(List.of(findOrCreateRole("role_admin")));
        userRepository.save(user);
    }

    private Role findOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role adminRole = new Role();
            adminRole.setName(roleName);
            return roleRepository.save(adminRole);
        });
    }
}
