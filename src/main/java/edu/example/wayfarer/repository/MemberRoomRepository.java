package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.MemberRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {
    @Query("select mb from MemberRoom mb where mb.room.roomId = :roomId")
    public MemberRoom findByRoomId(@Param("roomId") String roomId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MemberRoom mr WHERE mr.room.roomId = :roomId")
    void deleteByRoomId(@Param("roomId") String roomId);
}
