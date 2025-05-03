package br.com.fiap.mattu.model.zona;

import br.com.fiap.mattu.model.box.Box;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_ZONA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ZONA")
    private long idZona;

    @Column(name = "NOME", nullable = false, length = 50)
    private String nome;

    @Column(name = "STATUS", nullable = false)
    private boolean status;

    @Column(name = "DATA_ENTRADA")
    private LocalDateTime dataEntrada;

    @Column(name = "DATA_SAIDA")
    private LocalDateTime dataSaida;

    @Column(name = "OBSERVACAO", length = 255)
    private String observacao;

    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Box> boxes;
}
