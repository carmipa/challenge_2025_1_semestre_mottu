package br.com.fiap.mattu.model.rastreamento;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TB_RASTREAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rastreamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RASTREAMENTO")
    private long idRastreamento;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "IPS_X", precision = 7, scale = 3)
    private BigDecimal ipsX;

    @Column(name = "IPS_Y", precision = 7, scale = 3)
    private BigDecimal ipsY;

    @Column(name = "IPS_Z", precision = 7, scale = 3)
    private BigDecimal ipsZ;

    @Column(name = "DATA_HORA")
    private OffsetDateTime dataHora;

    @Column(name = "GPRS_LATITUDE", precision = 10, scale = 6)
    private BigDecimal gprslatitude;

    @Column(name = "GPRS_LONGITUDE", precision = 10, scale = 6)
    private BigDecimal gprsLongitude;

    @Column(name = "GPRS_ALTITUDE", precision = 7, scale = 3)
    private BigDecimal gprsAltitude;
}
