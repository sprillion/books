package com.matyuhin.books.repository;

import com.matyuhin.books.entity.BooksInStore;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BooksInStoreRepository extends JpaRepository<BooksInStore, Long> {

    List<BooksInStore> findByStoreId(Long storeId);
    @Modifying
    @Transactional
    @Query("delete from BooksInStore b where b.book.id = :bookId")
    void deleteByBookId(@Param("bookId") Long bookId);

    @Modifying
    @Transactional
    @Query("delete from BooksInStore b where b.book.id = :bookId and b.store.id = :storeId")
    void deleteByBookIdAndStoreId(@Param("bookId") Long bookId, @Param("storeId") Long storeId);

    @Modifying
    @Transactional
    @Query("delete from BooksInStore b where b.store.id = :storeId")
    void deleteByStoreId(@Param("storeId") Long storeId);

    @Transactional
    @Query(value = "select * from BOOKS_IN_STORE where BOOK_ID = :bookId and STORE_ID = :storeId", nativeQuery = true)
    BooksInStore selectBookInStore(@Param("bookId") Long bookId, @Param("storeId") Long storeId);
}
