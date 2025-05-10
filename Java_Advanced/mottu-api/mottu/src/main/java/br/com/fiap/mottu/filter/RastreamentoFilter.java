// Caminho do arquivo: br\com\fiap\mottu\filter\RastreamentoFilter.java
package br.com.fiap.mottu.filter;

public record RastreamentoFilter(
        String ips,
        String gprs,
        String veiculoPlaca // Filtro por veículo associado
) {}