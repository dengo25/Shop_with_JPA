package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem {
  
  @Id
  @GeneratedValue
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;  //하나의 상품은 여러 상품주문에 포함
  
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;
  
  private int orderPrice;
  
  private int count;
  
  private LocalDateTime regTime;
  
  private LocalDateTime updateTime;
}
