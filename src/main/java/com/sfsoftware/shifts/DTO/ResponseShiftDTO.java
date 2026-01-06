package com.sfsoftware.shifts.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ResponseShiftDTO {
    private int currentShiftNumber;
    private String privateCode;
    private int lowestNumberOfShift;
    private int highestNumberOfShift;
    private String nameOfStall;
    private boolean callingNextShift;

    public ResponseShiftDTO (int currentShiftNumber, String privateCode,
                             int lowestNumberOfShift, int highestNumberOfShift,
                             String nameOfStall) {
        this.currentShiftNumber = currentShiftNumber;
        this.privateCode = privateCode;
        this.lowestNumberOfShift = lowestNumberOfShift;
        this.highestNumberOfShift = highestNumberOfShift;
        this.nameOfStall = nameOfStall;
        this.callingNextShift = false;
    }

    public void setNameOfStall(String nameOfStall) {
        this.nameOfStall = nameOfStall;
    }

    public void callNextShift()  {
        callingNextShift = true;
    }
}
