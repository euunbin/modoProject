package com.example.modoproject.Review.repository;

import com.example.modoproject.Review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMerchantUidAndExternalId(String merchantUid, String externalId);
}
