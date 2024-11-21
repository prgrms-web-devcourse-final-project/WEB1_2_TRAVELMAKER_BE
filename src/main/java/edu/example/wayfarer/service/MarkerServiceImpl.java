package edu.example.wayfarer.service;

import edu.example.wayfarer.converter.MarkerConverter;
import edu.example.wayfarer.dto.MarkerListDTO;
import edu.example.wayfarer.dto.MarkerRequestDTO;
import edu.example.wayfarer.dto.MarkerResponseDTO;
import edu.example.wayfarer.dto.MarkerUpdateDTO;
import edu.example.wayfarer.entity.Marker;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.entity.Schedule;
import edu.example.wayfarer.repository.MarkerRepository;
import edu.example.wayfarer.repository.MemberRepository;
import edu.example.wayfarer.repository.MemberRoomRepository;
import edu.example.wayfarer.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MarkerServiceImpl implements MarkerService {

    private final MarkerRepository markerRepository;
    private final MemberRepository memberRepository;
    private final MemberRoomRepository memberRoomRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 마커 생성 메서드
     * 주어진 MarkerRequestDTO 를 사용하여 Member, Schedule, MemberRoom 를 조회한 뒤
     * 새로운 Marker 를 생성하고 저장한 후, 이를 MarkerResponseDTO 로 변환하여 반환합니다.
     *
     * @param markerRequestDTO Marker 생성 요청 데이터
     * @return MarkerResponseDTO 생성된 Marker 응답 데이터
     */
    @Override
    public MarkerResponseDTO createMarker(MarkerRequestDTO markerRequestDTO) {
        // 마커 생성을 위한 Member 조회
        Member member = memberRepository.findById(markerRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 마커 생성을 위한 Schedule 정보 조회
        Schedule schedule = scheduleRepository.findById(markerRequestDTO.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // 해당 멤버의 memberRoom.color 조회
        String color = memberRoomRepository.findByMember_EmailAndRoom_RoomId(
                member.getEmail(),
                schedule.getRoom().getRoomId()
        ).getColor();

        // Marker 저장
        Marker savedMarker = markerRepository.save(
                MarkerConverter.toMarker(
                        markerRequestDTO,
                        member,
                        schedule,
                        color
                )
        );

        // Marker 를 MarkerResponseDTO 로 변환 후 반환
        return MarkerConverter.toMarkerResponseDTO(savedMarker);
    }

    /**
     * 마커 조회 메서드
     * markerId로 Marker 조회 후 MarkerResponseDTO 로 변환하여 반환
     *
     * @param markerId 조회할 Marker 의 PK
     * @return MarkerResponseDTO 조회된 Marker 의 응답 데이터
     */
    @Override
    public MarkerResponseDTO readMarker(Long markerId) {
        // markerId 로 Marker 조회
        Marker foundMarker = markerRepository.findById(markerId)
                .orElseThrow(() -> new RuntimeException("Marker not found"));

        // 조회된 Marker 를 MarkerResponseDTO 로 변환하여 반환
        return MarkerConverter.toMarkerResponseDTO(foundMarker);
    }

    /**
     * 마커 목록 조회 메서드
     * scheduleId 를 기준으로 조회하여 MarkerResponse 리스트로 반환
     *
     * @param scheduleId 조회할 기준
     * @return List<MarkerResponseDTO> 조회된 Marker 들의 응답데이터 리스트
     */
    @Override
    public List<MarkerResponseDTO> readMarkers(Long scheduleId) {
        // scheduleId 로 Marker 리스트 조회
        List<Marker> markers = markerRepository.findBySchedule_ScheduleId(scheduleId);

        // 조회된 Marker 리스트를 MakerResponseDTO 로 변환하여 반환
        return markers.stream()
                .map(MarkerConverter::toMarkerResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 전체 마커 목록 조회 메서드
     * roomId 기준으로 모든 Marker 조회
     * 1. roomId 에 해당하는 모든 Schedule 조회
     * 2. 각 Schedule 의 scheduleId 로 Marker 리스트 조회
     * 3. Marker 를 MarkerResponseDTO 로 변환하고 리스트에 담음
     * 4. scheduleId 와 MarkerResponseDTO 리스트로 MarkerListDTO 생성
     *
     * @param roomId Marker 를 조회할 기준
     * @return List<MarkerListDTO> 해당하는 Room 의 모든 Marker 들의 응답데이터 리스트
     */
    @Override
    public List<MarkerListDTO> readAllMarkers(String roomId) {
        // 1. roomId 에 해당하는 모든 Schedule 조회
        return scheduleRepository.findByRoom_RoomId(roomId).stream()
                .map(schedule -> {
                    // 2. 각 Schedule 의 scheduleId 로 Marker 리스트 조회
                    List<MarkerResponseDTO> markerResponseDTOS
                            = markerRepository.findBySchedule_ScheduleId(schedule.getScheduleId()).stream()
                            // 3. Marker 를 MarkerResponseDTO 로 변환하고 리스트에 담음
                            .map(MarkerConverter::toMarkerResponseDTO)
                            .toList();

                    // 4. scheduleId 와 MarkerResponseDTO 리스트로 MarkerListDTO 생성
                    return MarkerConverter.toMarkerListDTO(schedule.getScheduleId(), markerResponseDTOS);
                })
                .toList();
    }

    /**
     * 마커 상태 변경 메서드
     * @param markerUpdateDTO
     * @return
     */
    @Override
    public MarkerResponseDTO updateMarker(MarkerUpdateDTO markerUpdateDTO) {

        return null;
    }

    @Override
    public void deleteMarker(Long markerId) {

    }
}
