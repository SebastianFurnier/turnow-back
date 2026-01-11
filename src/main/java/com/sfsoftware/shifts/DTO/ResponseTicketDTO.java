package com.sfsoftware.shifts.DTO;

import com.sfsoftware.shifts.Model.BlockedTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ResponseTicketDTO {
    private int currentShiftNumber;
    private String privateCode;
    private int lowestNumberOfShift;
    private int highestNumberOfShift;
    private List<BlockedTicket> blockedTicketList;

    public ResponseTicketDTO(int currentShiftNumber, String privateCode,
                             int lowestNumberOfShift, int highestNumberOfShift, List<BlockedTicket> blockedTickets) {
        this.currentShiftNumber = currentShiftNumber;
        this.privateCode = privateCode;
        this.lowestNumberOfShift = lowestNumberOfShift;
        this.highestNumberOfShift = highestNumberOfShift;
        this.blockedTicketList = blockedTickets;
    }
}
