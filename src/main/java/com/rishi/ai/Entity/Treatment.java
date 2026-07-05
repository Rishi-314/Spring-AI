package com.rishi.ai.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "treatments")
public class Treatment {

    @Id
    @Column(name = "treatment_id")
    private Integer treatmentId;

    @Column(name = "treatment_name")
    private String treatmentName;

    public Integer getTreatmentId() { return treatmentId; }
    public void setTreatmentId(Integer treatmentId) { this.treatmentId = treatmentId; }
    public String getTreatmentName() { return treatmentName; }
    public void setTreatmentName(String treatmentName) { this.treatmentName = treatmentName; }
}