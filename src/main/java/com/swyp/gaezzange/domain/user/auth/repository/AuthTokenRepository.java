package com.swyp.gaezzange.domain.user.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String /* UUID */> {
}
