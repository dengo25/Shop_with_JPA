package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
  
  @Id
  @Column(name = "item_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;  //상품코드
  
  @Column(nullable = false, length =50)
  private String itemNm;
  
  @Column(name = "price",nullable = false)
  private int price;
  
  @Column(nullable = false)
  private int stockNumber;
  
  @Lob
  @Column(nullable = false) //not null제약 조건
  private String itemDetail;
  
  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus; //상품 판매 상태
  
//  private LocalDateTime regTime;
//  private LocalDateTime updateTime; //수정시간
}
