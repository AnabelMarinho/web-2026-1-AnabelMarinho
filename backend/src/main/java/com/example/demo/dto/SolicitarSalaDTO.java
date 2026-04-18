package com.example.demo.dto;

import com.example.demo.enums.DiaSemana;
import com.example.demo.enums.SolicitarSalaStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
public class SolicitarSalaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Long discenteId;
    private Long salaId;

    @Schema(example = "SEGUNDA")
    private DiaSemana diaSemana;

    @Schema(type = "string", example = "16:00")
    private LocalTime horarioInicio;

    @Schema(type = "string", example = "18:00")
    private LocalTime horarioFim;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SolicitarSalaStatus status;
}