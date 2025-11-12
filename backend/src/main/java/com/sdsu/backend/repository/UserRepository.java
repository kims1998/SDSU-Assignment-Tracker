package com.sdsu.backend.repository;

import com.sdsu.backend.model.User;
// import com.sdsu.backend.model.UserSettings;
import com.sdsu.backend.model.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByIdAndEmail(Long userID, String email);

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByPassword(String password);
    Optional<User> findUserByName(String name);
    List<User> findUserByActiveStatus(boolean activeStatus);
    void deleteByEmail(String email);

}