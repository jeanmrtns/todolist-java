package com.jeanmrtns.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IUserRepository extends JpaRepository<User, UUID> {
    public User findByEmail(String email);
}
