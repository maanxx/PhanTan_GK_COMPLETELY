package iuh.fit.entity;

import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends Person implements Serializable  {
    private String dateOfBirth;
    private String address;
    private Gender gender;
}
