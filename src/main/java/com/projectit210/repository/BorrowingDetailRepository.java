package com.projectit210.repository;

import com.projectit210.entity.BorrowingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetail, Long> {

    List<BorrowingDetail> findByBorrowingRecordId(Long borrowingRecordId);

    @Query("SELECT d FROM BorrowingDetail d " +
           "JOIN FETCH d.equipment " +
           "WHERE d.borrowingRecord.id = :recordId")
    List<BorrowingDetail> findByBorrowingRecordIdWithEquipment(@Param("recordId") Long recordId);
}
