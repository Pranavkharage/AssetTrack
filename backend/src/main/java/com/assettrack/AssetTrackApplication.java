package com.assettrack;

import com.assettrack.entity.Category;
import com.assettrack.entity.Role;
import com.assettrack.entity.User;
import com.assettrack.repository.CategoryRepository;
import com.assettrack.repository.RoleRepository;
import com.assettrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AssetTrackApplication {
    @Value("${app.bootstrap-admin.email:}")
    private String bootstrapAdminEmail;

    @Value("${app.bootstrap-admin.password:}")
    private String bootstrapAdminPassword;

    public static void main(String[] args) {
        SpringApplication.run(AssetTrackApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(RoleRepository roles, UserRepository users,
                           CategoryRepository categories, PasswordEncoder encoder) {
        return args -> {
            Role admin = roles.findByName("ADMIN")
                    .orElseGet(() -> roles.save(new Role("ADMIN")));
            roles.findByName("EMPLOYEE")
                    .orElseGet(() -> roles.save(new Role("EMPLOYEE")));

            for (String name : new String[]{"Laptop", "Desktop", "Monitor", "Mobile",
                    "Peripheral", "Networking", "Other"}) {
                categories.findByName(name)
                        .orElseGet(() -> categories.save(new Category(name, "Default category")));
            }

            // Create the initial administrator only when credentials are explicitly
            // supplied through environment variables. Never ship a known default password.
            if (bootstrapAdminEmail != null && !bootstrapAdminEmail.isBlank()
                    && bootstrapAdminPassword != null && bootstrapAdminPassword.length() >= 12
                    && !users.existsByEmail(bootstrapAdminEmail)) {
                User user = new User();
                user.setFullName("AssetTrack Administrator");
                user.setEmail(bootstrapAdminEmail.trim().toLowerCase());
                user.setPassword(encoder.encode(bootstrapAdminPassword));
                user.setRole(admin);
                user.setActive(true);
                users.save(user);
            }
        };
    }
}
