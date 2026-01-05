package com.sfsoftware.shifts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestShiftDTO {
    private int lowestShiftValue;
    private int highestShiftValue;
}
