package com.finalproject.hrmsbackend.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finalproject.hrmsbackend.entities.abstracts.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "positions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Position implements BaseEntity<Short> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "positions_id_generator")
    @SequenceGenerator(name = "positions_id_generator", sequenceName = "positions_id_seq", allocationSize = 1, initialValue = 25)
    @Column(name = "id")
    private short id;

    @NotBlank(message = "cannot be empty")
    @Column(name = "title", unique = true)
    private String title;

    @OneToMany(mappedBy = "position")
    @JsonIgnoreProperties("position")
    private List<JobAdvertisement> jobAdvertisements;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }
}
