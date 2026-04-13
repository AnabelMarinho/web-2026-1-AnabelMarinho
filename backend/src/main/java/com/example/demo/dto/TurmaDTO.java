package com.example.demo.dto;

import com.example.demo.enums.TipoSala;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    private Long salaId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nomeSala;

    @Schema(example = "PRESENCIAL")
    private TipoSala tipo;

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
