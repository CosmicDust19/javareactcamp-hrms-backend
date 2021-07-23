package com.finalproject.hrmsbackend.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finalproject.hrmsbackend.entities.abstracts.CvProp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "candidates_languages", uniqueConstraints = {@UniqueConstraint(columnNames = {"candidate_id", "language_id"})})
public class CandidateLanguage implements CvProp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidates_languages_id_generator")
    @SequenceGenerator(name = "candidates_languages_id_generator", sequenceName = "candidates_languages_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    @JsonIgnoreProperties(value = {"candidateCvs", "candidateImages", "candidateJobExperiences",
            "candidateLanguages", "candidateSchools", "candidateSkills"})
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false, unique = true)
    private Language language;

    @Column(name = "language_level", nullable = false, length = 2)
    private String languageLevel;

}
