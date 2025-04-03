package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.BookOrderDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BookOrder;
import com.librarymanagement.entity.Librarian;
import com.librarymanagement.entity.BookOrder.OrderStatus;
import com.librarymanagement.repository.BookOrderRepository;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.LibrarianRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookOrderService {

    @Autowired
    private BookOrderRepository bookOrderRepository;
    
    @Autowired
    private LibrarianRepository librarianRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    /**
     * Create a new book order
     */
    public BookOrderDTO createOrder(BookOrderDTO orderDTO) {
        Optional<Librarian> librarianOpt = librarianRepository.findById(orderDTO.getOrderedById());
        
        if (librarianOpt.isEmpty()) {
            return null;
        }
        
        Librarian librarian = librarianOpt.get();
        
        BookOrder order = new BookOrder();
        order.setBookTitle(orderDTO.getBookTitle());
        order.setBookAuthor(orderDTO.getBookAuthor());
        order.setIsbn(orderDTO.getIsbn());
        order.setQuantity(orderDTO.getQuantity());
        order.setUnitPrice(orderDTO.getUnitPrice());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderedBy(librarian);
        order.setSupplierName(orderDTO.getSupplierName());
        order.setNotes(orderDTO.getNotes());
        
        BookOrder savedOrder = bookOrderRepository.save(order);
        
        return convertToDTO(savedOrder);
    }
    
    /**
     * Get all book orders
     */
    public List<BookOrderDTO> getAllOrders() {
        List<BookOrder> orders = bookOrderRepository.findAll();
        List<BookOrderDTO> orderDTOs = new ArrayList<>();
        
        for (BookOrder order : orders) {
            orderDTOs.add(convertToDTO(order));
        }
        
        return orderDTOs;
    }
    
    /**
     * Get orders by status
     */
    public List<BookOrderDTO> getOrdersByStatus(String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<BookOrder> orders = bookOrderRepository.findByStatus(orderStatus);
            List<BookOrderDTO> orderDTOs = new ArrayList<>();
            
            for (BookOrder order : orders) {
                orderDTOs.add(convertToDTO(order));
            }
            
            return orderDTOs;
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * Get an order by ID
     */
    public BookOrderDTO getOrderById(Long id) {
        Optional<BookOrder> orderOpt = bookOrderRepository.findById(id);
        
        if (orderOpt.isEmpty()) {
            return null;
        }
        
        return convertToDTO(orderOpt.get());
    }
    
    /**
     * Update order status
     */
    public BookOrderDTO updateOrderStatus(Long id, String status) {
        Optional<BookOrder> orderOpt = bookOrderRepository.findById(id);
        
        if (orderOpt.isEmpty()) {
            return null;
        }
        
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            BookOrder order = orderOpt.get();
            order.setStatus(orderStatus);
            
            // If delivered, set delivery date and update book stock
            if (orderStatus == OrderStatus.DELIVERED) {
                order.setDeliveryDate(LocalDateTime.now());
                
                // Check if book exists by ISBN
                List<Book> existingBooks = bookRepository.findByIsbn(order.getIsbn());
                
                if (!existingBooks.isEmpty()) {
                    // Update existing book stock
                    Book book = existingBooks.get(0);
                    book.setStock(book.getStock() + order.getQuantity());
                    bookRepository.save(book);
                } else {
                    // Create new book
                    Book book = new Book();
                    book.setTitle(order.getBookTitle());
                    book.setAuthor(order.getBookAuthor());
                    book.setIsbn(order.getIsbn());
                    book.setPrice(BigDecimal.valueOf(order.getUnitPrice()));
                    book.setStock(order.getQuantity());
                    bookRepository.save(book);
                }
            }
            
            BookOrder savedOrder = bookOrderRepository.save(order);
            return convertToDTO(savedOrder);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    // Helper method to convert entity to DTO
    private BookOrderDTO convertToDTO(BookOrder order) {
        BookOrderDTO orderDTO = new BookOrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setBookTitle(order.getBookTitle());
        orderDTO.setBookAuthor(order.getBookAuthor());
        orderDTO.setIsbn(order.getIsbn());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setUnitPrice(order.getUnitPrice());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setStatus(order.getStatus().toString());
        orderDTO.setDeliveryDate(order.getDeliveryDate());
        orderDTO.setOrderedById(order.getOrderedBy().getId());
        orderDTO.setOrderedByName(order.getOrderedBy().getUsername());
        orderDTO.setSupplierName(order.getSupplierName());
        orderDTO.setNotes(order.getNotes());
        
        return orderDTO;
    }
} 