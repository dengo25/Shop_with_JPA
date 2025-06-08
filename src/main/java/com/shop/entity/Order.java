package com.shop.entity;

import com.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
  
  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;
  
  private LocalDateTime orderDate; //주문일
  
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus; //주문 상태
  
  @OneToMany(mappedBy = "order") //연관관계의 주인이 order
  private List<OrderItem> orderItems = new ArrayList<>();
  
  private LocalDateTime refTime;
  
  private LocalDateTime updateTime;
}
