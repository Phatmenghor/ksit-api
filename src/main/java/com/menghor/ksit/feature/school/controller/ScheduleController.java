package com.menghor.ksit.feature.school.controller;

import com.menghor.ksit.exceptoins.response.ApiResponse;
import com.menghor.ksit.feature.school.dto.filter.ScheduleFilterDto;
import com.menghor.ksit.feature.school.dto.request.ScheduleRequestDto;
import com.menghor.ksit.feature.school.dto.response.ScheduleResponseDto;
import com.menghor.ksit.feature.school.dto.response.ScheduleResponseListDto;
import com.menghor.ksit.feature.school.dto.update.ScheduleUpdateDto;
import com.menghor.ksit.feature.school.service.ScheduleService;
import com.menghor.ksit.utils.database.CustomPaginationResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto) {
        log.info("REST request to create schedule: {}", requestDto);
        ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto);
        return new ApiResponse<>(
                "success",
                "Schedule created successfully",
                responseDto
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<ScheduleResponseDto> getScheduleById(@PathVariable Long id) {
        log.info("REST request to get schedule by ID: {}", id);
        ScheduleResponseDto responseDto = scheduleService.getScheduleById(id);
        return new ApiResponse<>(
                "success",
                "Schedule retrieved successfully",
                responseDto
        );
    }

    @PostMapping("/updateById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleUpdateDto updateDto) {
        log.info("REST request to update schedule with ID {}: {}", id, updateDto);
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(id, updateDto);
        return new ApiResponse<>(
                "success",
                "Schedule updated successfully",
                responseDto
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<ScheduleResponseDto> deleteSchedule(@PathVariable Long id) {
        log.info("REST request to delete schedule with ID: {}", id);
        ScheduleResponseDto responseDto = scheduleService.deleteSchedule(id);
        return new ApiResponse<>(
                "success",
                "Schedule deleted successfully",
                responseDto
        );
    }

    @PostMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'STAFF')")
    public ApiResponse<CustomPaginationResponseDto<ScheduleResponseListDto>> getAllSchedules(@RequestBody ScheduleFilterDto filterDto) {
        log.info("REST request to search schedules with filter: {}", filterDto);
        CustomPaginationResponseDto<ScheduleResponseListDto> responseDto = scheduleService.getAllSchedules(filterDto);
        return new ApiResponse<>(
                "success",
                "Schedules retrieved successfully",
                responseDto
        );
    }

    @GetMapping("/class/{classId}")
    public ApiResponse<List<ScheduleResponseDto>> getSchedulesByClassId(@PathVariable Long classId) {
        log.info("REST request to get schedules for class ID: {}", classId);
        List<ScheduleResponseDto> schedules = scheduleService.getSchedulesByClassId(classId);
        return new ApiResponse<>(
                "success",
                "Class schedules retrieved successfully",
                schedules
        );
    }
}