package com.sfsoftware.shifts.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BlockedTicket {
    @Id
    private UUID ticket_id;
    private String counterName;
    private int ticketNumber;
    private String queuePrivateCode;

    public BlockedTicket (String counterName, int ticketNumber, String queuePrivateCode) {
        ticket_id = UUID.randomUUID();
        this.counterName = counterName;
        this.ticketNumber = ticketNumber;
        this.queuePrivateCode = queuePrivateCode;
    }
}
