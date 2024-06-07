package edu.pw.serverexample.dtos;

import edu.pw.serverexample.entities.Form;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
public record FormDTO(String name, String surname, String about) {

    public static Form toEntity(FormDTO formDTO) {
        return new Form(null, formDTO.name(), formDTO.surname(), formDTO.about());
    }
}
