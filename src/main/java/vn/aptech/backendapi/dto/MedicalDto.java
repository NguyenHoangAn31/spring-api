package vn.aptech.backendapi.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MedicalDto {
    private int id;
    private String name;
    private String content;
    private int patientId;
}
