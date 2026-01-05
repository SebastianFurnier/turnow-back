package com.sfsoftware.shifts.Repository;

import com.sfsoftware.shifts.Model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    Shift findByPrivateCode(String privateCode);
}
