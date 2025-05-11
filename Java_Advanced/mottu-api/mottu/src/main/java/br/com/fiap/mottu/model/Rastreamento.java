package br.com.fiap.mottu.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal; // Importe BigDecimal!
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_RASTREAMENTO", schema = "CHALLENGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rastreamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RASTREAMENTO")
    @EqualsAndHashCode.Include
    private Long idRastreamento;

    // Campos para IPS com BigDecimal e precisão 7,3
    @Column(name = "IPS_X", nullable = false, precision = 7, scale = 3)
    private BigDecimal ipsX;

    @Column(name = "IPS_Y", nullable = false, precision = 7, scale = 3)
    private BigDecimal ipsY;

    @Column(name = "IPS_Z", nullable = false, precision = 7, scale = 3)
    private BigDecimal ipsZ;

    // Campos para GPRS com BigDecimal e precisão 11,6 (Latitude/Longitude) e 7,2 (Altitude)
    @Column(name = "GPRS_LATITUDE", nullable = false, precision = 11, scale = 6)
    private BigDecimal gprsLatitude;

    @Column(name = "GPRS_LONGITUDE", nullable = false, precision = 11, scale = 6)
    private BigDecimal gprsLongitude;

    @Column(name = "GPRS_ALTITUDE", nullable = false, precision = 7, scale = 2)
    private BigDecimal gprsAltitude;

    @OneToMany(mappedBy = "rastreamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private Set<br.com.fiap.mottu.model.relacionamento.VeiculoRastreamento> veiculoRastreamentos = new HashSet<>();
}