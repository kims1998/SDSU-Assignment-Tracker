package com.sdsu.backend.repository;

import com.sdsu.backend.model.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AssignmentRepository extends MongoRepository <Assignment, String> {
    List<Assignment> findByUserIdOrderByDueDateAsc(String userId);
    
}
