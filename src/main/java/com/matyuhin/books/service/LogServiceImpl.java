package com.matyuhin.books.service;

import com.matyuhin.books.entity.BooksInStore;
import com.matyuhin.books.entity.Log;
import com.matyuhin.books.models.ActionType;
import com.matyuhin.books.models.ObjectType;
import com.matyuhin.books.repository.BookRepository;
import com.matyuhin.books.repository.BooksInStoreRepository;
import com.matyuhin.books.repository.LogRepository;
import com.matyuhin.books.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class LogServiceImpl implements LogService {

    private final UserService userService;
    private final LogRepository logRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private BooksInStoreRepository booksInStoreRepository;


    public LogServiceImpl(UserService userService, LogRepository logRepository) {
        this.userService = userService;
        this.logRepository = logRepository;
    }

    @Override
    public void AddLog(ActionType actionType, ObjectType objectType, long objectId) {
        Log log = new Log();
        log.setActionType(actionType);
        log.setObjectType(objectType);
        log.setUser(userService.getCurrentUser());
        log.setObjectName(getObjectName(objectType, objectId));
        log.setTime(new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Calendar.getInstance().getTime()));
        logRepository.save(log);
    }

    private String getObjectName(ObjectType objectType, long id) {
        switch (objectType) {

            case BOOK -> {
                return bookRepository.findById(id).get().getTitle();
            }
            case STORE -> {
                return storeRepository.findById(id).get().getName();
            }
            case BOOK_IN_STORE -> {
                BooksInStore booksInStore = booksInStoreRepository.findById(id).get();
                return booksInStore.getBook().getTitle() + " in " + booksInStore.getStore().getName();
            }
        }
        return "";
    }
}
