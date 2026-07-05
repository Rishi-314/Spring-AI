package com.rishi.ai.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rishi.ai.Entity.WeightsConfig;
import com.rishi.ai.Repository.WeightsConfigRepository;

@Service
public class WeightsQueryService {

    private final WeightsConfigRepository weightsConfigRepository;

    public WeightsQueryService(WeightsConfigRepository weightsConfigRepository) {
        this.weightsConfigRepository = weightsConfigRepository;
    }

    /**
     * Returns a map of feature_name -> weight for all "live" features.
     */
    public Map<String, BigDecimal> getLiveWeights() {
        List<WeightsConfig> configs = weightsConfigRepository.findByIsLiveTrue();

        Map<String, BigDecimal> weights = new HashMap<>();
        for (WeightsConfig config : configs) {
            weights.put(config.getFeatureName(), config.getWeight());
        }

        return weights;
    }
}
