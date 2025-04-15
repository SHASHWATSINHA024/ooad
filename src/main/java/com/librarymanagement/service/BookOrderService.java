package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.BookOrderDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BookOrder;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.BookOrder.OrderStatus;
import com.librarymanagement.repository.BookOrderRepository;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookOrderService {

    @Autowired
    private BookOrderRepository bookOrderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    /**
     * Create a new book order
     */
    public BookOrderDTO createOrder(BookOrderDTO orderDTO) {
        Optional<User> userOpt = userRepository.findById(orderDTO.getOrderedById());
        
        if (userOpt.isEmpty()) {
            return null;
        }
        
        User user = userOpt.get();
        
        BookOrder order = new BookOrder();
        order.setBookTitle(orderDTO.getBookTitle());
        order.setBookAuthor(orderDTO.getBookAuthor());
        order.setIsbn(orderDTO.getIsbn());
        order.setQuantity(orderDTO.getQuantity());
        order.setUnitPrice(orderDTO.getUnitPrice());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderedBy(user);
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
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
    
    /**
     * Convert entity to DTO
     */
    private BookOrderDTO convertToDTO(BookOrder order) {
        BookOrderDTO dto = new BookOrderDTO();
        dto.setId(order.getId());
        dto.setBookTitle(order.getBookTitle());
        dto.setBookAuthor(order.getBookAuthor());
        dto.setIsbn(order.getIsbn());
        dto.setQuantity(order.getQuantity());
        dto.setUnitPrice(order.getUnitPrice());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().toString());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setOrderedById(order.getOrderedBy().getId());
        dto.setSupplierName(order.getSupplierName());
        dto.setNotes(order.getNotes());
        return dto;
    }
} 