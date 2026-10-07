package com.booksphere.repository;

import com.booksphere.model.Member;
import com.booksphere.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation: UserRepositoryImpl
 */
public class UserRepositoryImpl implements UserDAO {
    private final List<User> userStore = new CopyOnWriteArrayList<>();
    private int nextId = 1;

    public UserRepositoryImpl() {
        // Seed default users
        Member m1 = new Member(nextId++, "aarav_patel", "Aarav Patel", "aarav.patel@booksphere.edu", "MEM-2026-001", 5);
        Member m2 = new Member(nextId++, "priya_sharma", "Priya Sharma", "priya.sharma@booksphere.edu", "MEM-2026-002", 5);
        User librarian = new User(nextId++, "librarian_admin", "Dr. Meera Iyer", "admin@booksphere.org", "LIBRARIAN");

        userStore.add(m1);
        userStore.add(m2);
        userStore.add(librarian);
    }

    @Override
    public synchronized User save(User user) {
        if (user.getUserId() <= 0) {
            user.setUserId(nextId++);
        }
        userStore.add(user);
        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        return userStore.stream().filter(u -> u.getUserId() == id).findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return userStore.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username.trim()))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore);
    }

    @Override
    public List<Member> findAllMembers() {
        List<Member> members = new ArrayList<>();
        for (User u : userStore) {
            if (u instanceof Member) {
                members.add((Member) u);
            }
        }
        return members;
    }
}
