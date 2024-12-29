package com.poly.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.Variant;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {
    Set<Variant> findByProductId(UUID productId);

    Optional<Variant> findByIdAndProductId(UUID variantId, UUID productId);
}
