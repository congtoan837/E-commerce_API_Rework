package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String url;
    @Column
    private String shortDescription;
    @Column
    private int price;
    @Column
    private int priceTop;
    @Column
    private int discount;
    @Column
    private int discountRate;
    @Column
    private double ratingAverage;
    @Column
    private int reviewCount;

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<Image> images;
}
