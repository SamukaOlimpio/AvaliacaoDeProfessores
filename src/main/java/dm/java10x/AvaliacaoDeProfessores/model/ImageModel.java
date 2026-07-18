package dm.java10x.AvaliacaoDeProfessores.model;

import jakarta.persistence.*;

import java.util.Base64;

@Entity
@Table(name = "tb_image")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Column(columnDefinition = "TEXT")
    private String imageDataBase64;

    @ManyToOne
    @JoinColumn(name = "professorId")
    private ProfessorModel professorModel;

    // Construtores
    public ImageModel() {}

    public ImageModel(String name, String type, byte[] imageData) {
        this.name = name;
        this.type = type;
        this.imageDataBase64 = Base64.getEncoder().encodeToString(imageData);
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getImageDataBase64() { return imageDataBase64; }
    public void setImageDataBase64(String imageDataBase64) { this.imageDataBase64 = imageDataBase64; }

    public byte[] getImageData() {
        return Base64.getDecoder().decode(imageDataBase64);
    }
}
