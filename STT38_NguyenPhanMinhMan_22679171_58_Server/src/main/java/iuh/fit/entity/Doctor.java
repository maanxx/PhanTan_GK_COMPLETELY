package iuh.fit.entity;

import lombok.*;

import java.io.Serializable;

@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends Person implements Serializable {
    private String speciality;

    private transient Department department;

    public Doctor(String id, String name, String phone, String speciality) {
        super(id, name, phone);
        this.speciality = speciality;
    }
}

