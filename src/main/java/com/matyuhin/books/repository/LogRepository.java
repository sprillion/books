package com.matyuhin.books.repository;

import com.matyuhin.books.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
