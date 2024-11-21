package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.MemberRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {
    String findColorByEmailAndRoom_Id(String email, String RoomId);
}
