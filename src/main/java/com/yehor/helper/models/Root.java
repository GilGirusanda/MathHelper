package com.yehor.helper.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "root")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Root {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "root_id_seq")
    @SequenceGenerator(name = "root_id_seq", sequenceName = "root_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "value", nullable = false)
    private Double value;

    @ManyToOne
    @JoinColumn(name = "equation_id", nullable = false)
    private Equation equation;
}
