package com.menghor.ksit.feature.master.model;

import com.menghor.ksit.enumations.Status;
import com.menghor.ksit.feature.school.model.ScheduleEntity;
import com.menghor.ksit.utils.database.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "rooms")
public class RoomEntity extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<ScheduleEntity> schedules = new ArrayList<>();
}
