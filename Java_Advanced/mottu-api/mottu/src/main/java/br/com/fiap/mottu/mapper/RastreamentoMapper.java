package br.com.fiap.mottu.mapper;

import br.com.fiap.mottu.dto.rastreamento.RastreamentoRequestDto;
import br.com.fiap.mottu.dto.rastreamento.RastreamentoResponseDto;
import br.com.fiap.mottu.model.Rastreamento;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal; // Importe BigDecimal!

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface RastreamentoMapper {

    @Mapping(target = "idRastreamento", ignore = true)
    @Mapping(target = "veiculoRastreamentos", ignore = true)
    @Mapping(target = "ipsX", source = "ipsX")
    @Mapping(target = "ipsY", source = "ipsY")
    @Mapping(target = "ipsZ", source = "ipsZ")
    @Mapping(target = "gprsLatitude", source = "gprsLatitude")
    @Mapping(target = "gprsLongitude", source = "gprsLongitude")
    @Mapping(target = "gprsAltitude", source = "gprsAltitude")
    Rastreamento toEntity(RastreamentoRequestDto rastreamentoRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idRastreamento", ignore = true)
    @Mapping(target = "veiculoRastreamentos", ignore = true)
    @Mapping(target = "ipsX", source = "ipsX")
    @Mapping(target = "ipsY", source = "ipsY")
    @Mapping(target = "ipsZ", source = "ipsZ")
    @Mapping(target = "gprsLatitude", source = "gprsLatitude")
    @Mapping(target = "gprsLongitude", source = "gprsLongitude")
    @Mapping(target = "gprsAltitude", source = "gprsAltitude")
    Rastreamento partialUpdate(RastreamentoRequestDto dto, @MappingTarget Rastreamento rastreamento);

    @Mapping(target = "ipsX", source = "ipsX")
    @Mapping(target = "ipsY", source = "ipsY")
    @Mapping(target = "ipsZ", source = "ipsZ")
    @Mapping(target = "gprsLatitude", source = "gprsLatitude")
    @Mapping(target = "gprsLongitude", source = "gprsLongitude")
    @Mapping(target = "gprsAltitude", source = "gprsAltitude")
    RastreamentoResponseDto toResponseDto(Rastreamento rastreamento);
}