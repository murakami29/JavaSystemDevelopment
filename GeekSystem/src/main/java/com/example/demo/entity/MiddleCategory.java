package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "middle_categories")
@Data
public class MiddleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // LazyロードでN+1問題を防止
    @JoinColumn(name = "large_category_id", nullable = false)
    @JsonIgnore // 無限ループを防ぐために追加
    private LargeCategory largeCategory;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 作成タイムスタンプ

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 更新タイムスタンプ
    
    @OneToMany(mappedBy = "middleCategory")
    @JsonIgnore
    private List<SmallCategory> smallCategories;
    
    // toStringメソッドをオーバーライド
    @Override
    public String toString() {
        return "MiddleCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
