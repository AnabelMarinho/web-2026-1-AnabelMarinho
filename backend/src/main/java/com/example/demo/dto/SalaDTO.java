package com.example.demo.dto;

import com.example.demo.enums.SalaStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SalaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(example = "Sala 01")
    private String nomeSala;

    @Schema(example = "30")
    private Integer capacidade;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SalaStatus status;
}
