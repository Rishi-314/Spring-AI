package com.rishi.ai.Repository;

import com.rishi.ai.Entity.WeightsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightsConfigRepository extends JpaRepository<WeightsConfig, String> {
    List<WeightsConfig> findByIsLiveTrue();
}