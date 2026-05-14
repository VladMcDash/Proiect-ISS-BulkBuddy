package com.bulkbuddy.repository;

import com.bulkbuddy.domain.entity.User;
import com.bulkbuddy.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their unique username.
     *
     * @param username the username to search for
     * @return an Optional containing the user, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Check whether a user with the given username already exists.
     *
     * @param username the username to check
     * @return true if a user with this username exists
     */
    boolean existsByUsername(String username);

    /**
     * Find all users with a specific role.
     *
     * @param role the role to filter by
     * @return list of users with the given role
     */
    List<User> findByRole(Role role);
}
