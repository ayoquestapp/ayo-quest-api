package br.com.ayo_quest.ayo_quest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlternativaDTO {

    private Long id;
    private String texto;
    private boolean correta;


}
