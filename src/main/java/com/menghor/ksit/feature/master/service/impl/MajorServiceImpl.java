package com.menghor.ksit.feature.master.service.impl;

import com.menghor.ksit.enumations.Status;
import com.menghor.ksit.exceptoins.error.NotFoundException;
import com.menghor.ksit.feature.master.dto.filter.MajorFilterDto;
import com.menghor.ksit.feature.master.dto.request.MajorRequestDto;
import com.menghor.ksit.feature.master.dto.response.MajorResponseDto;
import com.menghor.ksit.feature.master.dto.response.MajorResponseListDto;
import com.menghor.ksit.feature.master.dto.update.MajorUpdateDto;
import com.menghor.ksit.feature.master.mapper.MajorMapper;
import com.menghor.ksit.feature.master.model.DepartmentEntity;
import com.menghor.ksit.feature.master.model.MajorEntity;
import com.menghor.ksit.feature.master.repository.DepartmentRepository;
import com.menghor.ksit.feature.master.repository.MajorRepository;
import com.menghor.ksit.feature.master.service.MajorService;
import com.menghor.ksit.feature.master.specification.MajorSpecification;
import com.menghor.ksit.utils.database.CustomPaginationResponseDto;
import com.menghor.ksit.utils.pagiantion.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MajorServiceImpl implements MajorService {
    private final MajorRepository majorRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorMapper majorMapper;

    @Override
    @Transactional
    public MajorResponseDto createMajor(MajorRequestDto majorRequestDto) {
        log.info("Creating new major with code: {}, name: {}, departmentId: {}",
                majorRequestDto.getCode(), majorRequestDto.getName(), majorRequestDto.getDepartmentId());

        MajorEntity major = majorMapper.toEntity(majorRequestDto);

        DepartmentEntity department = findDepartmentById(majorRequestDto.getDepartmentId());
        major.setDepartment(department);

        MajorEntity savedMajor = majorRepository.save(major);
        log.info("Major created successfully with ID: {}", savedMajor.getId());

        return majorMapper.toResponseDto(savedMajor);
    }

    @Override
    public MajorResponseDto getMajorById(Long id) {
        log.info("Fetching major by ID: {}", id);

        MajorEntity major = findMajorById(id);

        log.info("Retrieved major with ID: {}", id);
        return majorMapper.toResponseDto(major);
    }

    @Override
    @Transactional
    public MajorResponseDto updateMajorById(Long id, MajorUpdateDto majorRequestDto) {
        log.info("Updating major with ID: {}", id);

        // Find the existing entity
        MajorEntity existingMajor = findMajorById(id);

        // Use MapStruct to update only non-null fields
        majorMapper.updateEntityFromDto(majorRequestDto, existingMajor);

        // Handle department separately if provided
        if (majorRequestDto.getDepartmentId() != null) {
            DepartmentEntity department = findDepartmentById(majorRequestDto.getDepartmentId());
            existingMajor.setDepartment(department);
        }

        // Save the updated entity
        MajorEntity updatedMajor = majorRepository.save(existingMajor);
        log.info("Major updated successfully with ID: {}", id);

        return majorMapper.toResponseDto(updatedMajor);
    }

    @Override
    @Transactional
    public MajorResponseDto deleteMajorById(Long id) {
        log.info("Deleting major with ID: {}", id);

        MajorEntity major = findMajorById(id);

        majorRepository.delete(major);
        log.info("Major deleted successfully with ID: {}", id);

        return majorMapper.toResponseDto(major);
    }

    @Override
    public CustomPaginationResponseDto<MajorResponseListDto> getAllMajors(MajorFilterDto filterDto) {
        log.info("Fetching all majors with filter: {}", filterDto);

        // Validate and prepare pagination using PaginationUtils
        Pageable pageable = PaginationUtils.createPageable(
                filterDto.getPageNo(),
                filterDto.getPageSize(),
                "createdAt",
                "DESC"
        );

        // Create specification from filter criteria
        Specification<MajorEntity> spec = MajorSpecification.combine(
                filterDto.getSearch(),
                filterDto.getStatus(),
                filterDto.getDepartmentId()
        );

        // Execute query with specification and pagination
        Page<MajorEntity> majorPage = majorRepository.findAll(spec, pageable);

        // Apply status correction for any null statuses
        majorPage.getContent().forEach(major -> {
            if (major.getStatus() == null) {
                log.debug("Correcting null status to ACTIVE for major ID: {}", major.getId());
                major.setStatus(Status.ACTIVE);
                majorRepository.save(major);
            }
        });

        // Map to response DTO
        CustomPaginationResponseDto<MajorResponseListDto> response = majorMapper.toMajorAllResponseDto(majorPage);
        log.info("Retrieved {} majors (page {}/{})",
                response.getContent().size(),
                response.getPageNo(),
                response.getTotalPages());

        return response;
    }

    /**
     * Helper method to find a major by ID or throw NotFoundException
     */
    private MajorEntity findMajorById(Long id) {
        return majorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Major not found with ID: {}", id);
                    return new NotFoundException("Major id " + id + " not found. Please try again.");
                });
    }

    /**
     * Helper method to find a department by ID or throw NotFoundException
     */
    private DepartmentEntity findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department not found with ID: {}", id);
                    return new NotFoundException("Department id " + id + " not found");
                });
    }
}