package com.example.userservice.repository;

import com.example.userservice.entity.JoinUser;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinUserRepository extends JpaRepository<JoinUser, Long> {

    List<JoinUser> findAllBy(Pageable pageable);

    Boolean existsByLoginId(String loginId);
}
