package com.matyuhin.books.repository;

import com.matyuhin.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
