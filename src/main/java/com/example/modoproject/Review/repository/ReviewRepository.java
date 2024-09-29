package com.example.modoproject.Review.repository;

import com.example.modoproject.Review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMerchantUidAndExternalId(String merchantUid, String externalId);

    List<Review> findByMerchantUidIn(List<String> merchantUids);

    List<Review> findByMerchantUid(String merchantUid);

    List<Review> findByCompanyId(String companyId);

    @Query("SELECT r FROM Review r ORDER BY r.createdDateTime DESC")
    List<Review> findTop6ByOrderByCreatedDateDesc();
}
