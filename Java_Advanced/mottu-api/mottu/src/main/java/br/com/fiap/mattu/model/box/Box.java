package br.com.fiap.mattu.model.box;

import br.com.fiap.mattu.model.zona.Zona;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_BOX")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BOX")
    private long idBox;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "STATUS", nullable = false)
    private boolean status;

    @Column(name = "DATA_ENTRADA")
    private LocalDateTime dataEntrada;

    @Column(name = "DATA_SAIDA")
    private LocalDateTime dataSaida;

    @Column(name = "OBSERVACAO", length = 500)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TB_ZONA_ID_ZONA", nullable = false)
    private Zona zona;
}
