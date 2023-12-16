package com.yehor.helper.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equation_id_seq")
    @SequenceGenerator(name = "equation_id_seq", sequenceName = "equation_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "equation_value", nullable = false, length = 255)
    private String equationValue;

    @OneToMany(mappedBy = "equation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Root> rootSet = new HashSet<Root>();

}
