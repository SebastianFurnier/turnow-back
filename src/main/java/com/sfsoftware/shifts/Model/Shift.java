package com.sfsoftware.shifts.Model;

import com.sfsoftware.shifts.DTO.ResponseShiftDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Shift {
    @Id
    private UUID id;
    private int currentShiftNumber;
    private int lowestNumberOfShift;
    private int highestNumberOfShift;
    private String privateCode;

    public Shift(int lowestNumberOfShift, int highestNumberOfShift) {
        validateAndAssignData(lowestNumberOfShift, highestNumberOfShift);
        currentShiftNumber = this.lowestNumberOfShift;
        privateCode = generateRandomString();
        this.id = UUID.randomUUID();
    }

    private void validateAndAssignData(int lowestNumberOfShift, int highestNumberOfShift) {

        this.lowestNumberOfShift = Math.max(lowestNumberOfShift, 0);

        if (highestNumberOfShift <= lowestNumberOfShift) {
            this.highestNumberOfShift = 100;
        } else {
            this.highestNumberOfShift = highestNumberOfShift;
        }

        if (currentShiftNumber < lowestNumberOfShift || currentShiftNumber > highestNumberOfShift) {
            currentShiftNumber = lowestNumberOfShift;
        }
    }

    private String generateRandomString() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom randomNumber = new SecureRandom();
        int stringLength = 8;

        StringBuilder sb = new StringBuilder(stringLength);

        for (int i = 0; i < stringLength; i++) {
            int index = randomNumber.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    public void incrementNumberOfShift() {
        if (currentShiftNumber == highestNumberOfShift) {
            currentShiftNumber = lowestNumberOfShift;
        } else {
            currentShiftNumber++;
        }
    }

    public void setCurrentNumberOfShift(int newValue, int newLowestValue, int newHighestValue) {
        if (newValue < newLowestValue || newValue > newHighestValue) {
            throw new RuntimeException();
        }
        this.currentShiftNumber = newValue;
    }

    public void setLowestNumberOfShift (int lowestNumberOfShift) {
        validateAndAssignData(lowestNumberOfShift, this.highestNumberOfShift);
    }

    public void setHighestNumberOfShift(int highestNumberOfShift) {
        validateAndAssignData(this.lowestNumberOfShift, highestNumberOfShift);
    }

    public ResponseShiftDTO toDTO() {
        return new ResponseShiftDTO(currentShiftNumber, privateCode, lowestNumberOfShift, highestNumberOfShift, "");
    }
}
