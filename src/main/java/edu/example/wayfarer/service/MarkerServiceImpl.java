package edu.example.wayfarer.service;

import edu.example.wayfarer.converter.MarkerConverter;
import edu.example.wayfarer.dto.MarkerListDTO;
import edu.example.wayfarer.dto.MarkerRequestDTO;
import edu.example.wayfarer.dto.MarkerResponseDTO;
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

import java.util.List;

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
     * @param markerRequestDTO 마커 생성 요청 데이터
     * @return MarkerResponseDTO 생성된 마커 응답 데이터
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
        String color = memberRoomRepository.findColorByEmailAndRoom_Id(
                member.getEmail(),
                schedule.getRoom().getRoomId()
        );

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
     * @param markerId 조회할 머커의 PK
     * @return MarkerResponseDTO 조회된 마커의 응답 데이터
     */
    @Override
    public MarkerResponseDTO readMarker(Long markerId) {
        // markerId 로 Marker 조회
        Marker foundMarker = markerRepository.findById(markerId)
                .orElseThrow(() -> new RuntimeException("Marker not found"));

        // 조회된 Marker 를 MarkerResponseDTO 로 변환하여 반환
        return MarkerConverter.toMarkerResponseDTO(foundMarker);
    }

    @Override
    public List<MarkerResponseDTO> readMarkers(Long scheduleId) {
        return List.of();
    }

    @Override
    public List<MarkerListDTO> readAllMarkers(String roomId) {
        return List.of();
    }

    @Override
    public MarkerResponseDTO updateMarker(MarkerRequestDTO markerRequestDTO) {
        return null;
    }

    @Override
    public void deleteMarker(Long markerId) {

    }
}
