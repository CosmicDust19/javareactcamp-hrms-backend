package com.finalproject.hrmsbackend.entities.concretes.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finalproject.hrmsbackend.entities.concretes.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateJobExperienceAddDto {

    private int candidateId;

    @NotBlank(message = "cannot be empty")
    @Size(min = 2, max = 100, message = "invalid workplace")
    private String workPlace;

    @Valid
    @JsonIgnoreProperties(value = {"jobAdvertisements"})
    private Position position;

    @Min(value = 1900, message = "invalid start year")
    @Max(value = 2030, message = "invalid start year")
    private short startYear;

    @Min(value = 1900, message = "invalid quit year")
    @Max(value = 2030, message = "invalid quit year")
    private Short quitYear;
}
