package com.menghor.ksit.feature.master.controller;


import com.menghor.ksit.exceptoins.response.ApiResponse;
import com.menghor.ksit.feature.master.dto.filter.MajorFilterDto;
import com.menghor.ksit.feature.master.dto.request.MajorRequestDto;
import com.menghor.ksit.feature.master.dto.response.MajorResponseDto;
import com.menghor.ksit.feature.master.dto.response.MajorResponseListDto;
import com.menghor.ksit.feature.master.dto.update.MajorUpdateDto;
import com.menghor.ksit.feature.master.service.MajorService;
import com.menghor.ksit.utils.database.CustomPaginationResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/majors")
public class MajorController {
    private final MajorService majorService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<MajorResponseDto> createMajor(@Valid @RequestBody MajorRequestDto majorRequestDto) {
        log.info("Received request to create new major: {}", majorRequestDto);
        MajorResponseDto majorResponseDto = majorService.createMajor(majorRequestDto);
        log.info("Major created successfully with ID: {}", majorResponseDto.getId());
        return new ApiResponse<>(
                "Success",
                "Major created successfully...!",
                majorResponseDto
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<MajorResponseDto> getMajorById(@PathVariable Long id) {
        log.info("Received request to get major by ID: {}", id);
        MajorResponseDto majorResponseDto = majorService.getMajorById(id);
        log.info("Successfully retrieved major with ID: {}", id);
        return new ApiResponse<>(
                "Success",
                "Get major by id "+ id + " successfully...!",
                majorResponseDto
        );
    }

    @PostMapping("/updateById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<MajorResponseDto> updateMajorById(@PathVariable Long id, @Valid @RequestBody MajorUpdateDto majorRequestDto) {
        log.info("Received request to update major with ID: {}, update data: {}", id, majorRequestDto);
        MajorResponseDto majorResponseDto = majorService.updateMajorById(id, majorRequestDto);
        log.info("Successfully updated major with ID: {}", id);
        return new ApiResponse<>(
                "Success",
                "Update major by id " + id + " successfully...!",
                majorResponseDto
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<MajorResponseDto> deleteMajorById(@PathVariable Long id) {
        log.info("Received request to delete major with ID: {}", id);
        MajorResponseDto majorResponseDto = majorService.deleteMajorById(id);
        log.info("Successfully deleted major with ID: {}", id);
        return new ApiResponse<>(
                "Success",
                "Delete major by id " + id + " successfully...!",
                majorResponseDto
        );
    }

    @PostMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<CustomPaginationResponseDto<MajorResponseListDto>> getAllMajors(@RequestBody MajorFilterDto filterDto) {
        log.info("Received request to fetch all majors with filter: {}", filterDto);
        CustomPaginationResponseDto<MajorResponseListDto> majorResponseDto = majorService.getAllMajors(filterDto);
        log.info("Successfully fetched {} majors", majorResponseDto.getTotalPages());
        return new ApiResponse<>(
                "Success",
                "All majors fetched successfully...!",
                majorResponseDto
        );
    }
}