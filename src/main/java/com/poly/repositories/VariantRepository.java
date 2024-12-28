package com.poly.repositories;

import com.poly.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {
    Set<Variant> findByProductId(UUID productId);

    Optional<Variant> findByIdAndProductId(UUID variantId, UUID productId);
}
