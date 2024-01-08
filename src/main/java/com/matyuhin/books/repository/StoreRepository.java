package com.matyuhin.books.repository;

import com.matyuhin.books.entity.BooksInStore;
import com.matyuhin.books.entity.Store;
import com.matyuhin.books.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
