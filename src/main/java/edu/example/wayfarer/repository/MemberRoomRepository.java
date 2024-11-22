package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.MemberRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {
    Optional<MemberRoom> findByMember_EmailAndRoom_RoomId(String email, String roomId);
}
