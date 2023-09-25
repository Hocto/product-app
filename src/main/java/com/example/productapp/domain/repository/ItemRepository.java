package com.example.productapp.domain.repository;

import com.example.productapp.domain.model.stock.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT * FROM item WHERE LOWER(name)=LOWER(:name) AND is_sold=:isSold", nativeQuery = true)
    Optional<Item> findByNameAndIsSold(@Param("name") String name, @Param("isSold") boolean isSold);
}
