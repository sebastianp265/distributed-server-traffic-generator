package edu.pw.serverexample.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Form {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String surname;

    private String about;

}
