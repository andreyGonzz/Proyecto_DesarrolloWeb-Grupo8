package com.proyecto.proyectoweb.repository;

import com.proyecto.proyectoweb.model.UserD;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserD, Long> {
    Optional<UserD> findByEmail(String email);
}
