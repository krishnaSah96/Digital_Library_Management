package com.booksphere.repository;

import com.booksphere.model.Member;
import com.booksphere.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Interface: UserDAO
 * Abstraction for Member and User persistence
 */
public interface UserDAO {
    User save(User user);
    Optional<User> findById(int id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    List<Member> findAllMembers();
}
