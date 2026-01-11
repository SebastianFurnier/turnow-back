package com.sfsoftware.shifts.Model;

import com.sfsoftware.shifts.DTO.ResponseTicketDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class TicketQueue {
    @Id
    private UUID id;
    private int currentTicketNumber;
    private int lowestTicketNumber;
    private int highestTicketNumber;
    private String privateCode;

    public TicketQueue(int lowestTicketNumber, int highestTicketNumber) {
        validateAndAssignData(lowestTicketNumber, highestTicketNumber);
        privateCode = generateRandomString();
        this.id = UUID.randomUUID();
    }

    private void validateAndAssignData(int lowestTicketNumber, int highestTicketNumber) {

        this.lowestTicketNumber = Math.max(lowestTicketNumber, 1);

        if (highestTicketNumber <= lowestTicketNumber) {
            this.highestTicketNumber = 100;
        } else {
            this.highestTicketNumber = highestTicketNumber;
        }

        if (currentTicketNumber < lowestTicketNumber || currentTicketNumber > highestTicketNumber) {
            currentTicketNumber = lowestTicketNumber;
        }
    }

    private String generateRandomString() {
        String chars = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz0123456789";
        SecureRandom randomNumber = new SecureRandom();
        int stringLength = 8;

        StringBuilder sb = new StringBuilder(stringLength);

        for (int i = 0; i < stringLength; i++) {
            int index = randomNumber.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    private void incrementCurrentTicketNumber() {
        if (currentTicketNumber == highestTicketNumber) {
            currentTicketNumber = lowestTicketNumber;
        } else {
            currentTicketNumber++;
        }
    }

    public void setCurrentTicketNumber(int newValue, int newLowestValue, int newHighestValue) {
        if (newValue < newLowestValue || newValue > newHighestValue) {
            throw new RuntimeException();
        }
        this.currentTicketNumber = newValue;
    }

    public void setLowestTicketNumber(int lowestTicketNumber) {
        validateAndAssignData(lowestTicketNumber, this.highestTicketNumber);
    }

    public void setHighestTicketNumber(int highestTicketNumber) {
        validateAndAssignData(this.lowestTicketNumber, highestTicketNumber);
    }

    public ResponseTicketDTO toDTO(List<BlockedTicket> blockedTicketList) {
        return new ResponseTicketDTO(currentTicketNumber, privateCode, lowestTicketNumber,
                highestTicketNumber, blockedTicketList);
    }

    public List<BlockedTicket> blockCurrentTicketNumber(String counterName, List<BlockedTicket> blockedTicketsList) {
        BlockedTicket blockedTicket = new BlockedTicket(counterName, currentTicketNumber, privateCode);
        blockedTicketsList.add(blockedTicket);

        incrementCurrentTicketNumber();

        return blockedTicketsList;
    }
}
