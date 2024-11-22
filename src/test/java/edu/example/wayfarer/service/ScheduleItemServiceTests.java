package edu.example.wayfarer.service;


import edu.example.wayfarer.dto.scheduleItem.ScheduleItemResponseDTO;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemUpdateDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScheduleItemServiceTests {
    @Autowired
    private ScheduleItemService scheduleItemService;

    @Test
    @Order(1)
    public void testRead() {
        Long scheduleItemId = 1L;

        ScheduleItemResponseDTO scheduleItemResponseDTO = scheduleItemService.read(scheduleItemId);

        System.out.println(scheduleItemResponseDTO);
    }

    @Test
    @Order(2)
    public void testGetListByScheduleId() {
        Long scheduleId = 1L;

        List<ScheduleItemResponseDTO> results = scheduleItemService.getListBySchedule(scheduleId);

        System.out.println(results);
    }

    @Test
    @Order(3)
    public void testUpdate() {
        ScheduleItemUpdateDTO updateDTO = new ScheduleItemUpdateDTO();
        updateDTO.setScheduleItemId(1L);
        updateDTO.setName("Updated Name");
        updateDTO.setContent("Updated Content");

        ScheduleItemResponseDTO result = scheduleItemService.update(updateDTO);
        System.out.println(result);
    }
}
