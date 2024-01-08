package com.matyuhin.books.repository;

import com.matyuhin.books.entity.BooksInStore;
import com.matyuhin.books.entity.Store;
import com.matyuhin.books.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from STORES_CREATOR where STORE_ID = :storeId", nativeQuery = true)
    void deleteInStoreCreatorByStoreId(@Param("storeId") Long storeId);

}
