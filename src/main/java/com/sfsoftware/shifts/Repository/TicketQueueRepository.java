package com.sfsoftware.shifts.Repository;

import com.sfsoftware.shifts.Model.TicketQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketQueueRepository extends JpaRepository<TicketQueue, UUID> {
    TicketQueue findByPrivateCode(String privateCode);
}
