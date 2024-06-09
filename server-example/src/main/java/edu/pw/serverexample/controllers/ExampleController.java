package edu.pw.serverexample.controllers;

import edu.pw.serverexample.dtos.FormDTO;
import edu.pw.serverexample.entities.File;
import edu.pw.serverexample.entities.Form;
import edu.pw.serverexample.repositories.FileRepository;
import edu.pw.serverexample.repositories.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    private final FormRepository formRepository;
    private final FileRepository fileRepository;

    @PostMapping("/form")
    public void postForm(@RequestBody FormDTO formDTO) {
        Form form = FormDTO.toEntity(formDTO);
        formRepository.save(form);
    }

    @PostMapping("/file")
    public int postFile(@RequestPart(value = "file") MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();

        File fileEntity = new File(null, file.getBytes());

        // fake processing to add some load
        int sum = 0;
        for (byte firstByte : bytes) {
            for (byte secondByte : bytes) {
                sum += firstByte * secondByte;
            }
        }
        fileRepository.save(fileEntity);

        return sum;
    }

}
