package com.matyuhin.books.controller;

import com.matyuhin.books.entity.Book;
import com.matyuhin.books.entity.BooksInStore;
import com.matyuhin.books.entity.Store;
import com.matyuhin.books.entity.User;
import com.matyuhin.books.models.ActionType;
import com.matyuhin.books.models.ObjectType;
import com.matyuhin.books.models.Roles;
import com.matyuhin.books.repository.BookRepository;
import com.matyuhin.books.repository.BooksInStoreRepository;
import com.matyuhin.books.repository.StoreRepository;
import com.matyuhin.books.service.LogService;
import com.matyuhin.books.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BooksInStoreRepository booksInStoreRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;


    @GetMapping({"/listStores"})
    public ModelAndView getAllStores() {
        var mav = new ModelAndView("list-stores");
        User user = userService.getCurrentUser();
        List<Store> stores = storeRepository.findAll();
        if (!Objects.equals(user.getRoles().get(0).getName(), Roles.ADMIN.toString()))
        {
            stores = stores.stream().filter(s -> s.getCreator().get(0) == user).toList();
        }
        mav.addObject("stores", stores);
        return mav;
    }

    @GetMapping("/addStoreForm")
    public ModelAndView addStoreForm() {
        var mav = new ModelAndView("add-store-form");
        var store = new Store();
        mav.addObject("store", store);
        mav.addObject("par", "create");
        return mav;
    }

    @PostMapping("/saveStore")
    public String saveStore(@ModelAttribute Store store) {
        store.setCreator(Arrays.asList(userService.getCurrentUser()));
        boolean isCreate = store.getId() == 0;
        storeRepository.save(store);
        logService.AddLog(isCreate ? ActionType.CREATE : ActionType.EDIT, ObjectType.STORE, store.getId());
        return "redirect:/listStores";
    }

    @GetMapping("/showStoreUpdateForm")
    public ModelAndView showStoreUpdateForm(@RequestParam Long storeId) {
        var mav = new ModelAndView("add-store-form");
        var optionalStore = storeRepository.findById(storeId);
        var store = new Store();
        if (optionalStore.isPresent()) {
            store = optionalStore.get();
        }
        mav.addObject("store", store);
        mav.addObject("par", "edit");
        return mav;
    }

    @GetMapping("/deleteStore")
    public String deleteStore(@RequestParam Long storeId) {
        logService.AddLog(ActionType.DELETE, ObjectType.STORE, storeId);
        booksInStoreRepository.deleteByStoreId(storeId);
        storeRepository.deleteInStoreCreatorByStoreId(storeId);
        storeRepository.deleteById(storeId);
        return "redirect:/listStores";
    }

    @GetMapping({"/listBooksInStore"})
    public ModelAndView getAllBooks(@RequestParam Long storeId) {
        var mav = new ModelAndView("list-books-in-store");
        var booksInStore = booksInStoreRepository.findByStoreId(storeId);
        mav.addObject("booksInStore", booksInStore);
        mav.addObject("storeId", storeId);
        return mav;
    }

    @GetMapping("/addBookInStoreForm")
    public ModelAndView addBookForm(@RequestParam("storeId") Long storeId) {
        var mav = new ModelAndView("add-book-in-store-form");
        mav.addObject("books", bookRepository.findAll());
        BooksInStore booksInStore = new BooksInStore();
        booksInStore.setStore(storeRepository.findById(storeId).get());
        mav.addObject("bookInStore", booksInStore);
        mav.addObject("newBook", new Book());
        mav.addObject("par", "create");
        return mav;
    }

    @PostMapping("/saveBookInStore")
    public String saveBookInStore(@ModelAttribute BooksInStore book, @ModelAttribute Book newBook) {
        boolean isCreate = false;
        try {
            if (book.getCount() < 0) throw new Exception();
            BooksInStore booksInStore = booksInStoreRepository.selectBookInStore(newBook.getId(), book.getStore().getId());
            if (booksInStore != null){
                int count = book.getCount();
                book = booksInStore;
                book.setCount(count);
            }
            else {
                newBook = bookRepository.findById(newBook.getId()).get();
                book.setBook(newBook);
                book.setId(null);
                isCreate = true;
            }
        } catch (Exception e) {
            return "redirect:/addBookInStoreForm?failed&storeId=" + book.getStore().getId();
        }
        booksInStoreRepository.save(book);
        logService.AddLog(isCreate ? ActionType.ADD : ActionType.EDIT, ObjectType.BOOK_IN_STORE, book.getId());
        return "redirect:/listBooksInStore?storeId=" + book.getStore().getId();
    }

    @GetMapping("/showUpdateBookInStoreForm")
    public ModelAndView showUpdateForm(@RequestParam Long bookId, @RequestParam Long storeId) {
        var mav = new ModelAndView("add-book-in-store-form");
        BooksInStore booksInStore = booksInStoreRepository.selectBookInStore(bookId, storeId);
        mav.addObject("books", bookRepository.findAll());
        mav.addObject("bookInStore", booksInStore);
        mav.addObject("newBook", booksInStore.getBook());
        mav.addObject("par", "edit");
        return mav;
    }

    @GetMapping("/deleteBookInStore")
    public String deleteBookInStore(@RequestParam Long bookId, @RequestParam Long storeId) {
        BooksInStore booksInStore = booksInStoreRepository.selectBookInStore(bookId, storeId);
        logService.AddLog(ActionType.REMOVE, ObjectType.BOOK_IN_STORE, booksInStore.getId());
        booksInStoreRepository.deleteByBookIdAndStoreId(bookId, storeId);
        return "redirect:/listBooksInStore?storeId=" + storeId;
    }

}
