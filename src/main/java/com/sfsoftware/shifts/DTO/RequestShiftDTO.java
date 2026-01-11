package com.sfsoftware.shifts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class RequestShiftDTO {
    private String privateCode;
    private String counterName;
    private UUID ticketId;
}
