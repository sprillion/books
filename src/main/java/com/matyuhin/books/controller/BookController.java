package com.matyuhin.books.controller;

import com.matyuhin.books.entity.Book;
import com.matyuhin.books.models.ActionType;
import com.matyuhin.books.models.ObjectType;
import com.matyuhin.books.repository.BookRepository;
import com.matyuhin.books.repository.BooksInStoreRepository;
import com.matyuhin.books.repository.StoreRepository;
import com.matyuhin.books.service.LogService;
import com.matyuhin.books.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Slf4j
@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BooksInStoreRepository booksInStoreRepository;
    @Autowired
    private LogService logService;
    @Autowired
    private UserService userService;

    @GetMapping({"/listBooks"})
    public ModelAndView getAllBooks() {
        var mav = new ModelAndView("list-books");
        mav.addObject("books", bookRepository.findAll());
        //mav.addObject("user", userService.getCurrentUser());
        return mav;
    }

    @GetMapping("/addBookForm")
    public ModelAndView addBookForm() {
        var mav = new ModelAndView("add-book-form");
        var book = new Book();
        mav.addObject("book", book);
        mav.addObject("par", "create");
        return mav;
    }

    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute Book book) {
        try {
            if (Objects.equals(book.getTitle(), "") || Objects.equals(book.getAuthor(), "") || book.getPrice() < 0)
                throw new Exception();
            boolean isCreate = book.getId() == 0;
            bookRepository.save(book);
            logService.AddLog(isCreate ? ActionType.CREATE : ActionType.EDIT, ObjectType.BOOK, book.getId());
        } catch (Exception e){
            return "redirect:/addBookForm?failed";
        }

        return "redirect:/listBooks";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long bookId) {
        var mav = new ModelAndView("add-book-form");
        var optionalBook = bookRepository.findById(bookId);
        var book = new Book();
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        }
        mav.addObject("book", book);
        mav.addObject("par", "edit");
        return mav;
    }

    @GetMapping("/deleteBook")
    public String deleteBook(@RequestParam Long bookId) {
        booksInStoreRepository.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);
        logService.AddLog(ActionType.DELETE, ObjectType.BOOK, bookId);
        return "redirect:/listBooks";
    }
}
