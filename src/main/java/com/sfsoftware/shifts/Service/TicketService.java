package com.sfsoftware.shifts.Service;

import com.sfsoftware.shifts.DTO.RequestShiftDTO;
import com.sfsoftware.shifts.DTO.SetNumberRequestDTO;
import com.sfsoftware.shifts.Model.BlockedTicket;
import com.sfsoftware.shifts.Model.TicketQueue;
import com.sfsoftware.shifts.DTO.ResponseTicketDTO;
import com.sfsoftware.shifts.Repository.BlockedTicketRepository;
import com.sfsoftware.shifts.Repository.TicketQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketQueueRepository ticketQueueRepository;
    private final BlockedTicketRepository blockedTicketRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ResponseTicketDTO createTicketQueue() {
        TicketQueue newTicketQueue = new TicketQueue(1, 100);

        ticketQueueRepository.save(newTicketQueue);

        return newTicketQueue.toDTO(new ArrayList<>());
    }

    @Transactional
    public ResponseTicketDTO callAndBlockCurrentTicket(RequestShiftDTO requestDTO) {

        String privateCode = requestDTO.getPrivateCode();
        String countName = requestDTO.getCounterName();

        TicketQueue ticketQueue = ticketQueueRepository.findByPrivateCode(privateCode);
        List<BlockedTicket> blockedTicketsList = blockedTicketRepository.findAllByQueuePrivateCode(privateCode);

        List<BlockedTicket> newBlockedTicketsList = ticketQueue.blockCurrentTicketNumber(countName, blockedTicketsList);

        ticketQueueRepository.save(ticketQueue);
        blockedTicketRepository.saveAll(newBlockedTicketsList);

        ResponseTicketDTO updatedShift = ticketQueue.toDTO(newBlockedTicketsList);

        messagingTemplate.convertAndSend(
                "/topic/shifts/" + privateCode,
                updatedShift
        );

        return updatedShift;
    }

    public ResponseTicketDTO editShift(SetNumberRequestDTO numberRequestDTO) {
        TicketQueue ticketQueue = ticketQueueRepository.findByPrivateCode(numberRequestDTO.getPrivateCode());
        List<BlockedTicket> blockedTicketList = blockedTicketRepository.findAllByQueuePrivateCode(
                numberRequestDTO.getPrivateCode());

        ticketQueue.setCurrentTicketNumber(numberRequestDTO.getNewCurrentNumber(),
                numberRequestDTO.getNewLowestNumber(), numberRequestDTO.getNewHighestNumber());
        ticketQueue.setLowestTicketNumber(numberRequestDTO.getNewLowestNumber());
        ticketQueue.setHighestTicketNumber(numberRequestDTO.getNewHighestNumber());

        ticketQueueRepository.save(ticketQueue);

        ResponseTicketDTO updatedShift = ticketQueue.toDTO(blockedTicketList);

        messagingTemplate.convertAndSend(
                "/topic/shifts/" + numberRequestDTO.getPrivateCode(),
                updatedShift
        );

        return updatedShift;
    }

    public ResponseTicketDTO getTicketQueue(String privateCode) {
        TicketQueue ticketQueue = ticketQueueRepository.findByPrivateCode(privateCode);
        List<BlockedTicket> blockedTicketList = blockedTicketRepository.findAllByQueuePrivateCode(privateCode);

        return ticketQueue.toDTO(blockedTicketList);
    }

    @Transactional
    public ResponseTicketDTO servedTicket(RequestShiftDTO requestShiftDTO) {

        System.out.println(requestShiftDTO.getTicketId());

        String privateCode = requestShiftDTO.getPrivateCode();
        TicketQueue ticketQueue = ticketQueueRepository.findByPrivateCode(privateCode);

        Optional<BlockedTicket> blockedTicket = blockedTicketRepository.findById(requestShiftDTO.getTicketId());

        blockedTicket.ifPresent(blockedTicketRepository::delete);

        List<BlockedTicket> blockedTicketList = blockedTicketRepository.findAllByQueuePrivateCode(privateCode);

        ResponseTicketDTO responseTicketDTO = ticketQueue.toDTO(blockedTicketList);

        messagingTemplate.convertAndSend(
                "/topic/shifts/" + privateCode,
                responseTicketDTO
        );

        return responseTicketDTO;
    }
}

