package org.example.scholarshipmanager.repository;

import org.example.scholarshipmanager.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pentru entitatea Department.
 * Extinde JpaRepository pentru a oferi operații CRUD și interogări standard
 * asupra tabelului departments.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
