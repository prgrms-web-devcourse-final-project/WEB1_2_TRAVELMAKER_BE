package edu.example.wayfarer.service;

import edu.example.wayfarer.converter.ScheduleItemConverter;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemResponseDTO;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemUpdateDTO;
import edu.example.wayfarer.entity.ScheduleItem;
import edu.example.wayfarer.repository.ScheduleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleItemServiceImpl implements ScheduleItemService {

    private final ScheduleItemRepository scheduleItemRepository;

    /**
     * 스케쥴 아이템 조회 메서드
     * scheduleItemId로 ScheduleItem 조회 후 ScheduleItemResponseDTO 로 변환하여 반환
     *
     * @param scheduleItemId 조회할 ScheduleItem 의 PK
     * @return ScheduleItemResponseDTO 조회된 Schedule 의 응답 데이터
     */
    @Override
    public ScheduleItemResponseDTO read(Long scheduleItemId) {
        // scheduleItemId 로 scheduleItem 조회
        ScheduleItem scheduleItem = scheduleItemRepository.findById(scheduleItemId)
                .orElseThrow(() -> new RuntimeException("ScheduleItem not found"));

        // 조회된 scheduleItem 을 ScheduleItemResponseDTO 로 변환 후 반환
        return ScheduleItemConverter.toScheduleItemResponseDTO(scheduleItem);
    }

    /**
     * 스케쥴 아이템 목록 조회
     * scheduleId 를 기준으로 ScheduleItem 조회 후 ScheduleItemResponseDTO 리스트로 변환하여 반환
     *
     * @param scheduleId ScheduleItem 을 조회할 기준
     * @return List<ScheduleItemResponseDTO> 조회된 ScheduleItem 리스트의 응답 데이터
     */
    @Override
    public List<ScheduleItemResponseDTO> getListBySchedule(Long scheduleId) {
        // scheduleId 를 기준으로 scheduleItem 리스트 조회
        List<ScheduleItem> scheduleItems = scheduleItemRepository.findByMarker_Schedule_ScheduleId(scheduleId);

        // 조회된 ScheduleItem 리스트를 ScheduleItemResponseDTO 리스트로 변환하여 반환
        return scheduleItems.stream()
                .map(ScheduleItemConverter::toScheduleItemResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleItemResponseDTO update(ScheduleItemUpdateDTO scheduleItemUpdateDTO) {
        return null;
    }

    // 독립적으로 ScheduleItem 이 생성되고 삭제되는 경우는 없기 때문에 create delete 는 생략
    // create -> Marker 의 confirm true 요청시 생성
    // delete -> Marker 의 confirm false 요청시 삭제
}
