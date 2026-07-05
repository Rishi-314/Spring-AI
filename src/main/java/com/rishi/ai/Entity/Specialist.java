package com.rishi.ai.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "specialists")
public class Specialist {

    @Id
    @Column(name = "specialist_id")
    private Integer specialistId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String gender; // nullable

    private String specialization;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "consultation_fee")
    private BigDecimal consultationFee;

    private String languages; // comma-separated

    private BigDecimal rating;

    @Column(name = "total_reviews")
    private Integer totalReviews;

    @Column(name = "has_availability")
    private Boolean hasAvailability;

    @OneToMany(mappedBy = "specialist", fetch = FetchType.EAGER)
    private List<SpecialistTreatment> treatments;

    // Getters and setters
    public Integer getSpecialistId() { return specialistId; }
    public void setSpecialistId(Integer specialistId) { this.specialistId = specialistId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }
    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    public Boolean getHasAvailability() { return hasAvailability; }
    public void setHasAvailability(Boolean hasAvailability) { this.hasAvailability = hasAvailability; }
    public List<SpecialistTreatment> getTreatments() { return treatments; }
    public void setTreatments(List<SpecialistTreatment> treatments) { this.treatments = treatments; }
}