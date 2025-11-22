package com.demo.entity;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "Category")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryId")
    private Integer categoryId;

    @Column(name = "Categoryname")
    private String categoryname;

    @Column(name = "Categorycode")
    private String categorycode;

    @Column(name = "Images")
    private String images;

    @Column(name = "Status")
    private Boolean status;

    @OneToMany(mappedBy = "category")
    private List<Video> videos;

    public Category() {}

    // Constructor để tạo nhanh
    public Category(String name, String code, String images, Boolean status) {
        this.categoryname = name;
        this.categorycode = code;
        this.images = images;
        this.status = status;
    }

    // getters & setters
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryname() { return categoryname; }
    public void setCategoryname(String categoryname) { this.categoryname = categoryname; }

    public String getCategorycode() { return categorycode; }
    public void setCategorycode(String categorycode) { this.categorycode = categorycode; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public List<Video> getVideos() { return videos; }
    public void setVideos(List<Video> videos) { this.videos = videos; }
}
