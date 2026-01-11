package com.sfsoftware.shifts.Repository;

import com.sfsoftware.shifts.Model.BlockedTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlockedTicketRepository extends JpaRepository<BlockedTicket, UUID> {
    List<BlockedTicket> findAllByQueuePrivateCode(String privateCode);
}
