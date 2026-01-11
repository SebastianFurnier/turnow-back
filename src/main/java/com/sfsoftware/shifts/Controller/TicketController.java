package com.sfsoftware.shifts.Controller;

import com.sfsoftware.shifts.DTO.RequestShiftDTO;
import com.sfsoftware.shifts.DTO.ResponseTicketDTO;
import com.sfsoftware.shifts.DTO.SetNumberRequestDTO;
import com.sfsoftware.shifts.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, ResponseTicketDTO>> createShift() {
        ResponseTicketDTO responseTicketDTO = ticketService.createTicketQueue();

        return createSuccessResponse(responseTicketDTO);
    }

    @PostMapping("/call")
    public ResponseEntity<Map<String, ResponseTicketDTO>> callAndBlockCurrentTicket(
            @RequestBody RequestShiftDTO request) {
        ResponseTicketDTO responseTicketDTO = ticketService.callAndBlockCurrentTicket(request);
        return createSuccessResponse(responseTicketDTO);
    }

    @PutMapping("/served")
    public ResponseEntity<Map<String, ResponseTicketDTO>> servedTicket(@RequestBody RequestShiftDTO request) {
        ResponseTicketDTO responseTicketDTO = ticketService.servedTicket(request);

        return createSuccessResponse(responseTicketDTO);
    }

    @PutMapping("/edit")
    public ResponseEntity<Map<String, ResponseTicketDTO>> setCurrentNumber(
            @RequestBody SetNumberRequestDTO numberRequestDTO) {
        ResponseTicketDTO responseTicketDTO = ticketService.editShift(numberRequestDTO);

        return createSuccessResponse(responseTicketDTO);
    }

    @GetMapping("/ticketQueue/{privateCode}")
    public ResponseEntity<Map<String, ResponseTicketDTO>> getTicketQueue(@PathVariable String privateCode) {
        ResponseTicketDTO responseTicketDTO = ticketService.getTicketQueue(privateCode);

        return createSuccessResponse(responseTicketDTO);
    }

    private ResponseEntity<Map<String, ResponseTicketDTO>> createSuccessResponse(ResponseTicketDTO responseTicketDTO) {

        Map<String, ResponseTicketDTO> response = new HashMap<>();

        response.put("response", responseTicketDTO);

        return ResponseEntity.ok(response);
    }

}
