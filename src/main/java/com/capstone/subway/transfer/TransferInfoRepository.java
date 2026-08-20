package com.capstone.subway.transfer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferInfoRepository extends JpaRepository<TransferInfo, Long> {

    List<TransferInfo> findAll();
}
