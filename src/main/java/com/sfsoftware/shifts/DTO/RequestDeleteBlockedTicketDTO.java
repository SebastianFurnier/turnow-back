package com.sfsoftware.shifts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestDeleteBlockedTicketDTO {
    private UUID id;
    private String privateCode;
}
