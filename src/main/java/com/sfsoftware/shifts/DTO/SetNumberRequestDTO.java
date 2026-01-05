package com.sfsoftware.shifts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SetNumberRequestDTO {
    private int newCurrentNumber;
    private int newLowestNumber;
    private int newHighestNumber;
    private String privateCode;
}
