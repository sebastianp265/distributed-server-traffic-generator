package edu.pw.serverexample.controllers;

import edu.pw.serverexample.dtos.FormDTO;
import edu.pw.serverexample.entities.Form;
import edu.pw.serverexample.repositories.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    private final FormRepository formRepository;

    @PostMapping("/form")
    public void postForm(@RequestBody FormDTO formDTO) {
        Form form = FormDTO.toEntity(formDTO);
        formRepository.save(form);
    }

}
