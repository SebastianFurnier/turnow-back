package com.sfsoftware.shifts.Controller;

import com.sfsoftware.shifts.DTO.RequestShiftDTO;
import com.sfsoftware.shifts.DTO.ResponseShiftDTO;
import com.sfsoftware.shifts.DTO.SetNumberRequestDTO;
import com.sfsoftware.shifts.Service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, ResponseShiftDTO>> createShift() {
        ResponseShiftDTO responseShiftDTO = shiftService.createShift();

        return createSuccessResponse(responseShiftDTO);
    }

    @PostMapping("/increment/{privateCode}")
    public ResponseEntity<Map<String, ResponseShiftDTO>> incrementShiftCurrentNumber(@PathVariable String privateCode) {
        ResponseShiftDTO responseShiftDTO = shiftService.incrementCurrentNumber(privateCode);

        return createSuccessResponse(responseShiftDTO);
    }

    @PutMapping("/call")
    public ResponseEntity<Map<String, ResponseShiftDTO>> callNextShift(@RequestBody RequestShiftDTO request) {
        ResponseShiftDTO responseShiftDTO = shiftService.callNext(request);

        return createSuccessResponse(responseShiftDTO);
    }

    @PutMapping("/edit")
    public ResponseEntity<Map<String, ResponseShiftDTO>> setCurrentNumber(
            @RequestBody SetNumberRequestDTO numberRequestDTO) {
        ResponseShiftDTO responseShiftDTO = shiftService.editShift(numberRequestDTO);

        return createSuccessResponse(responseShiftDTO);
    }

    @GetMapping("/shift/{privateCode}")
    public ResponseEntity<Map<String, ResponseShiftDTO>> getShift(@PathVariable String privateCode) {
        ResponseShiftDTO responseShiftDTO = shiftService.getShift(privateCode);

        return createSuccessResponse(responseShiftDTO);
    }

    private ResponseEntity<Map<String, ResponseShiftDTO>> createSuccessResponse(ResponseShiftDTO responseShiftDTO) {

        Map<String, ResponseShiftDTO> response = new HashMap<>();

        response.put("response", responseShiftDTO);

        return ResponseEntity.ok(response);
    }
}
