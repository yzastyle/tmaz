package org.my_tma_test_app.tmaz.repo;

import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<TelegramUser, Long> {

    Optional<TelegramUser> findById(Long id);
}
