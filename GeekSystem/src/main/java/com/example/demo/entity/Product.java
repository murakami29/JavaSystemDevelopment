package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "small_category_id", nullable = false)
    private SmallCategory smallCategory;

    @ManyToOne(fetch = FetchType.LAZY) // N+1問題を防ぐためにLazyロードを使用
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Lob
    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "product_image",nullable = true)
    private String productImage;

    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice = BigDecimal.ZERO;

    @Column(name = "manufacturer_suggested_retail_price", precision = 10, scale = 2)
    private BigDecimal manufacturerSuggestedRetailPrice = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 作成タイムスタンプ

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 更新タイムスタンプ
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<StoreProductInventory> storeProductInventories;
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderHistory> orderHistories;
    
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "'}"; // 必要なプロパティだけ返す
    }
}
