package com.sdsu.backend.repository;

import com.sdsu.backend.model.User;
// import com.sdsu.backend.model.UserSettings;
import com.sdsu.backend.model.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}