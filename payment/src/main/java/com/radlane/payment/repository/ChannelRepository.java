package com.radlane.payment.repository;


import com.radlane.payment.model.entity.CryptoChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<CryptoChannel, Long> {

}