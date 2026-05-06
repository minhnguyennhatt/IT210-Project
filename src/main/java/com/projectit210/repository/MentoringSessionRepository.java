package com.projectit210.repository;

import com.projectit210.entity.MentoringSession;
import com.projectit210.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MentoringSessionRepository extends JpaRepository<MentoringSession, Long> {

    /**
     * Kiểm tra xung đột slot: giảng viên đã có lịch trong cùng khung giờ chưa (chỉ tính các trạng thái active)
     */
    @Query("SELECT COUNT(s) > 0 FROM MentoringSession s " +
           "WHERE s.lecturer.id = :lecturerId " +
           "AND s.sessionDate = :sessionDate " +
           "AND s.startTime = :startTime " +
           "AND s.status <> 'CANCELLED'")
    boolean existsConflictingSlot(@Param("lecturerId") Long lecturerId,
                                  @Param("sessionDate") LocalDate sessionDate,
                                  @Param("startTime") LocalTime startTime);

    List<MentoringSession> findByStudentIdOrderBySessionDateDescStartTimeDesc(String studentId);

    List<MentoringSession> findByLecturerIdOrderBySessionDateDescStartTimeDesc(Long lecturerId);

    List<MentoringSession> findByLecturerIdAndStatus(Long lecturerId, SessionStatus status);

    @Query("SELECT s FROM MentoringSession s " +
           "JOIN FETCH s.student " +
           "JOIN FETCH s.lecturer l " +
           "JOIN FETCH l.user " +
           "JOIN FETCH l.department " +
           "WHERE s.student.id = :studentId " +
           "ORDER BY s.sessionDate DESC, s.startTime DESC")
    List<MentoringSession> findByStudentIdWithDetails(@Param("studentId") String studentId);

    @Query("SELECT s FROM MentoringSession s " +
           "JOIN FETCH s.student " +
           "JOIN FETCH s.lecturer l " +
           "JOIN FETCH l.user " +
           "WHERE s.lecturer.id = :lecturerId AND s.status = :status " +
           "ORDER BY s.sessionDate ASC, s.startTime ASC")
    List<MentoringSession> findByLecturerIdAndStatusWithDetails(@Param("lecturerId") Long lecturerId,
                                                                 @Param("status") SessionStatus status);

    /**
     * Lấy các slot đã được đặt của giảng viên trong 1 ngày (chỉ tính active, không tính CANCELLED)
     */
    @Query("SELECT s FROM MentoringSession s " +
           "WHERE s.lecturer.id = :lecturerId " +
           "AND s.sessionDate = :sessionDate " +
           "AND s.status <> 'CANCELLED'")
    List<MentoringSession> findBookedSlots(@Param("lecturerId") Long lecturerId,
                                           @Param("sessionDate") LocalDate sessionDate);
}
