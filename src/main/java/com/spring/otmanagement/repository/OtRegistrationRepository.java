package com.spring.otmanagement.repository;

import com.spring.otmanagement.entity.OtRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtRegistrationRepository extends JpaRepository<OtRegistration, Long> {
    List<OtRegistration> findByEmployee_Id(Long id);
    List<OtRegistration> findByManager_Id(Long id);
    List<OtRegistration> findByEmployee_IdOrManager_Id(Long employeeId, Long managerId);
}
