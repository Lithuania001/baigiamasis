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

@Component
@RequiredArgsConstructor
public class DataFiller implements CommandLineRunner {

   private final PasswordEncoder passwordEncoder;
   private final UserRepository userRepository;
   private final RoleRepository roleRepository;

   @Override
   @Transactional
   public void run(String... args) {
      if (userRepository.findByUsername("admin").isEmpty()) {
         createUser("admin", "role_admin");
      }

      if (userRepository.findByUsername("user").isEmpty()) {
         createUser("user", "role_user");
      }
   }

   private void createUser(String name, String role) {
      User user = new User();
      user.setUsername(name);
      user.setPassword(passwordEncoder.encode(CharBuffer.wrap(name)));
      user.setRoles(List.of(findOrCreateRole(role)));
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
