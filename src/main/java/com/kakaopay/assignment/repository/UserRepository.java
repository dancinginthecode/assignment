package com.kakaopay.assignment.repository;

import com.kakaopay.assignment.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by sangwon on 20. 12. 22..
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
