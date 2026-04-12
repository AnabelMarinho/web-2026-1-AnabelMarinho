package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TurmaDTO {

    private String nomeTurma;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    @Schema(type = "string", example = "19:00")
    private LocalTime horarioInicio;

    @Schema(type = "string", example = "21:00")
    private LocalTime horarioFim;

    private String sala;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long instrutorId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nomeInstrutor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long cursoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nomeCurso;

}
