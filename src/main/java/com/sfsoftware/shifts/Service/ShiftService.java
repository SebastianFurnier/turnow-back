package com.sfsoftware.shifts.Service;

import com.sfsoftware.shifts.DTO.RequestShiftDTO;
import com.sfsoftware.shifts.DTO.SetNumberRequestDTO;
import com.sfsoftware.shifts.Model.Shift;
import com.sfsoftware.shifts.DTO.ResponseShiftDTO;
import com.sfsoftware.shifts.Repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ResponseShiftDTO createShift() {
        Shift newShift = new Shift(0, 100);

        shiftRepository.save(newShift);

        return newShift.toDTO();
    }

    public ResponseShiftDTO incrementCurrentNumber(String privateCode) {
        Shift shift = shiftRepository.findByPrivateCode(privateCode);

        shift.incrementNumberOfShift();

        shiftRepository.save(shift);

        ResponseShiftDTO updatedShift = shift.toDTO();

        messagingTemplate.convertAndSend(
                "/topic/shifts/" + privateCode,
                updatedShift
        );

        return updatedShift;
    }

    public ResponseShiftDTO editShift(SetNumberRequestDTO numberRequestDTO) {
        Shift shift = shiftRepository.findByPrivateCode(numberRequestDTO.getPrivateCode());

        shift.setCurrentNumberOfShift(numberRequestDTO.getNewCurrentNumber(),
                numberRequestDTO.getNewLowestNumber(), numberRequestDTO.getNewHighestNumber());
        shift.setLowestNumberOfShift(numberRequestDTO.getNewLowestNumber());
        shift.setHighestNumberOfShift(numberRequestDTO.getNewHighestNumber());

        shiftRepository.save(shift);

        ResponseShiftDTO updatedShift = shift.toDTO();

        messagingTemplate.convertAndSend(
                "/topic/shifts/" + numberRequestDTO.getPrivateCode(),
                updatedShift
        );

        return updatedShift;
    }

    public ResponseShiftDTO getShift(String privateCode) {
        Shift shift = shiftRepository.findByPrivateCode(privateCode);

        return shift.toDTO();
    }

    public ResponseShiftDTO callNext(RequestShiftDTO requestShiftDTO) {
        String privateCode = requestShiftDTO.getPrivateCode();
        Shift shift = shiftRepository.findByPrivateCode(privateCode);

        ResponseShiftDTO responseShiftDTO = shift.toDTO();

        responseShiftDTO.setNameOfStall(requestShiftDTO.getNameOfStall());

        responseShiftDTO.callNextShift();

        messagingTemplate.convertAndSend(
                "/topic/shifts/" + privateCode,
                responseShiftDTO
        );

        return responseShiftDTO;
    }
}

