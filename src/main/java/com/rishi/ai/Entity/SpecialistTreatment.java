package com.rishi.ai.Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "specialist_treatments")
public class SpecialistTreatment {

    @EmbeddedId
    private SpecialistTreatmentId id;

    @ManyToOne
    @MapsId("specialistId")
    @JoinColumn(name = "specialist_id")
    private Specialist specialist;

    @ManyToOne
    @MapsId("treatmentId")
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    public SpecialistTreatmentId getId() { return id; }
    public void setId(SpecialistTreatmentId id) { this.id = id; }
    public Specialist getSpecialist() { return specialist; }
    public void setSpecialist(Specialist specialist) { this.specialist = specialist; }
    public Treatment getTreatment() { return treatment; }
    public void setTreatment(Treatment treatment) { this.treatment = treatment; }

    @Embeddable
    public static class SpecialistTreatmentId implements Serializable {
        private Integer specialistId;
        private Integer treatmentId;

        public SpecialistTreatmentId() {}
        public SpecialistTreatmentId(Integer specialistId, Integer treatmentId) {
            this.specialistId = specialistId;
            this.treatmentId = treatmentId;
        }

        public Integer getSpecialistId() { return specialistId; }
        public void setSpecialistId(Integer specialistId) { this.specialistId = specialistId; }
        public Integer getTreatmentId() { return treatmentId; }
        public void setTreatmentId(Integer treatmentId) { this.treatmentId = treatmentId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SpecialistTreatmentId)) return false;
            SpecialistTreatmentId that = (SpecialistTreatmentId) o;
            return Objects.equals(specialistId, that.specialistId) &&
                   Objects.equals(treatmentId, that.treatmentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(specialistId, treatmentId);
        }
    }
}
