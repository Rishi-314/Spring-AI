package com.rishi.ai.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "weights_config")
public class WeightsConfig {

    @Id
    @Column(name = "feature_name")
    private String featureName;

    private BigDecimal weight;

    @Column(name = "is_live")
    private Boolean isLive;

    public String getFeatureName() { return featureName; }
    public void setFeatureName(String featureName) { this.featureName = featureName; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public Boolean getIsLive() { return isLive; }
    public void setIsLive(Boolean isLive) { this.isLive = isLive; }
}