package com.dunnnan.reservations.model;

import jakarta.persistence.*;

@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = true)
    private String name;

    @Column(nullable = false, updatable = true)
    private String description;

    @Column(nullable = false, updatable = true, name = "image_name")
    private String imageName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = true)
    private ResourceType type;

//    @ManyToOne
//    @JoinColumn(name = "resource_type_id", updatable = true, nullable = false)
//    private ResourceType type;

    public Resource() {
    }

    public Resource(String name, String description, String imageName, ResourceType type) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }
}
