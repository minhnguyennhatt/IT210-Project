package com.projectit210.repository;

import com.projectit210.entity.BorrowingRecord;
import com.projectit210.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    List<BorrowingRecord> findByStudentId(String studentId);

    List<BorrowingRecord> findByStatus(BorrowStatus status);

    Optional<BorrowingRecord> findByMentoringSessionId(Long mentoringSessionId);

    @Query("SELECT b FROM BorrowingRecord b " +
           "JOIN FETCH b.student " +
           "JOIN FETCH b.mentoringSession s " +
           "JOIN FETCH s.lecturer l " +
           "JOIN FETCH l.user " +
           "LEFT JOIN FETCH b.details d " +
           "LEFT JOIN FETCH d.equipment " +
           "WHERE b.status = :status " +
           "ORDER BY b.createdAt DESC")
    List<BorrowingRecord> findByStatusWithDetails(@Param("status") BorrowStatus status);

    @Query("SELECT b FROM BorrowingRecord b " +
           "JOIN FETCH b.student " +
           "LEFT JOIN FETCH b.details d " +
           "LEFT JOIN FETCH d.equipment " +
           "ORDER BY b.createdAt DESC")
    List<BorrowingRecord> findAllWithDetails();
}
