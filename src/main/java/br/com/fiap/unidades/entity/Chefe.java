package br.com.fiap.unidades.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "TB_CHEFE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CHEFE_UNIDADE_PERIODO", columnNames = {"UNIDADE_ID", "DT_INICIO_CHEFE", "DT_FIM_CHEFE"})
        })

public class Chefe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CHEFE")
    @SequenceGenerator(name = "SQ_CHEFE", sequenceName = "SQ_CHEFE", allocationSize = 1)
    @Column(name = "ID_CHEFE")
    private Long id;

    @Column(name = "SUBSTITUTO_CHEFE")
    private Boolean substituto;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "UNIDADE_ID")
    private Unidade unidade;

    @Column(name = "DT_INICIO_CHEFE")
    private LocalDateTime inicio;

    @Column(name = "DT_FIM_CHEFE")
    private LocalDateTime fim;

}
