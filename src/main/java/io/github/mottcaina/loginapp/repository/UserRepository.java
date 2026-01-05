package io.github.mottcaina.loginapp.repository;

import io.github.mottcaina.loginapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
