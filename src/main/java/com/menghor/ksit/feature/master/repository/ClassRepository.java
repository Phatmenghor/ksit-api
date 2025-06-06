package com.menghor.ksit.feature.master.repository;

import com.menghor.ksit.feature.master.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClassRepository extends JpaRepository<ClassEntity, Long>, JpaSpecificationExecutor<ClassEntity> {
}