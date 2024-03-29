package com.ddooby.gachiillgi.domain.repository;

import com.ddooby.gachiillgi.domain.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
