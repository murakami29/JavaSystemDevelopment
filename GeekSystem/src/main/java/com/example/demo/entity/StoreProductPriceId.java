package com.example.demo.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class StoreProductPriceId implements Serializable {

	@Column(name = "product_id")
	private Long productId;
    
	@Column(name = "store_id")
	private Long storeId;

    // Default constructor, getters, setters, equals, and hashCode（右クリック→ソースより作成）

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoreProductPriceId other = (StoreProductPriceId) obj;
		return Objects.equals(productId, other.productId) && Objects.equals(storeId, other.storeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, storeId);
	}

}
