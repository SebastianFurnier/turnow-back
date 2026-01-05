package com.sfsoftware.shifts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseShiftDTO {
    private int currentShiftNumber;
    private String privateCode;
    private int lowestNumberOfShift;
    private int highestNumberOfShift;
}
