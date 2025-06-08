package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties") //테스트용으로 h2사용
class OrderRepositoryTest {
  
  @Autowired
  OrderRepository orderRepository;
  
  @Autowired
  ItemRepository itemRepository;
  
  @Autowired
  MemberRepository memberRepository;
  
  @PersistenceContext
  EntityManager em;
  
  public Item createItem() {
    Item item = new Item();
    item.setItemNm("테스트 상품");
    item.setPrice(10000);
    item.setItemDetail("테스트 상품 상세 설명");
    item.setItemSellStatus(ItemSellStatus.SELL);
    item.setStockNumber(100);
    item.setRegTime(LocalDateTime.now());
    item.setUpdateTime(LocalDateTime.now());
    return item;
    
  }
  
  @Test
  @DisplayName("영속성 전이 테스트")
  public void cascadeTest() {
    Order order = new Order();
    for (int i = 0; i < 3; i++) {
      Item item = this.createItem();
      itemRepository.save(item);
      OrderItem orderItem = new OrderItem();
      orderItem.setItem(item);
      orderItem.setCount(10);
      orderItem.setOrderPrice(10000);
      orderItem.setOrder(order);
      order.getOrderItems().add(orderItem);
    }
    orderRepository.saveAndFlush(order); //여기서 order를 저장할 때 OrderItem도 저장됨
    em.clear();  //영속성 컨텍스트초기화
    
    
    Order savedOrder = orderRepository.findById(order.getId())
        .orElseThrow(EntityNotFoundException::new);
    Assertions.assertEquals(3, savedOrder.getOrderItems().size());
  }
  
  
  public Order createOrder(){
    Order order = new Order();
    for(int i=0;i<3;i++){
      Item item = createItem();
      itemRepository.save(item);
      OrderItem orderItem = new OrderItem();
      orderItem.setItem(item);
      orderItem.setCount(10);
      orderItem.setOrderPrice(1000);
      orderItem.setOrder(order);
      order.getOrderItems().add(orderItem);
    }
    Member member = new Member();
    memberRepository.save(member);
    order.setMember(member);
    orderRepository.save(order);
    return order;
  }
  
  @Test
  @DisplayName("고아객체 제거 테스트")
  public void orphanRemovalTest(){
    Order order = this.createOrder();
    order.getOrderItems().remove(0);
    em.flush();
  }
  
}