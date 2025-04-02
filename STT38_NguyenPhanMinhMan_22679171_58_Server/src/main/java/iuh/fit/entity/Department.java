package iuh.fit.entity;

import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department implements Serializable {
    private String id;
    private String name;
    private  String location;
}
