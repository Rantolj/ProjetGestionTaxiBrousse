package com.mmebaovola.taxibrousse.dto;

import java.math.BigDecimal;
import java.util.List;

public class CaResponseDto {

    private BigDecimal total;
    private List<SocieteCaDto> perSociete;

    public CaResponseDto() {
    }

    public CaResponseDto(BigDecimal total, List<SocieteCaDto> perSociete) {
        this.total = total;
        this.perSociete = perSociete;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<SocieteCaDto> getPerSociete() {
        return perSociete;
    }

    public void setPerSociete(List<SocieteCaDto> perSociete) {
        this.perSociete = perSociete;
    }
}

