package vn.aptech.backendapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="medicals")
public class Medical extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String content;
    // 1 bệnh nhân có nhiều triệu chứng bệnh

    @ManyToOne
    @JoinColumn(name = "partient_id", referencedColumnName = "id")
    private Partient partient;
}
