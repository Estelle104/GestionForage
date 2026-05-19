package com.example.forage.dto;

import java.sql.Timestamp;
import java.util.List;

public class DevisCreationDTO {
    private String demandeReference;
    private Long typeId;
    private String observation;
    private Timestamp createdAt;
    private List<DetailDevisDTO> details;

    public DevisCreationDTO(String demandeReference, Long typeId, String observation, Timestamp createdAt,
            List<DetailDevisDTO> details) {
        this.demandeReference = demandeReference;
        this.typeId = typeId;
        this.observation = observation;
        this.createdAt = createdAt;
        this.details = details;
    }

    public DevisCreationDTO() {
    }

    public DevisCreationDTO(String demandeReference, Long typeId, String observation, List<DetailDevisDTO> details) {
        this.demandeReference = demandeReference;
        this.typeId = typeId;
        this.observation = observation;
        this.details = details;
    }

    public String getDemandeReference() {
        return demandeReference;
    }

    public void setDemandeReference(String demandeReference) {
        this.demandeReference = demandeReference;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<DetailDevisDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailDevisDTO> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "DevisCreationDTO [demandeReference=" + demandeReference + ", typeId=" + typeId
                + ", observation=" + observation + ", details=" + details + "]";
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
