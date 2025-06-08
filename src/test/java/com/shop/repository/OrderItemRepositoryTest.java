package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
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
class OrderItemRepositoryTest {
  
  @Autowired
  OrderItemRepository orderItemRepository;
  
  @Autowired
  ItemRepository itemRepository;
  
  @Autowired
  MemberRepository memberRepository;
  
  @Autowired
  OrderRepository orderRepository;
  
  
  @PersistenceContext
  EntityManager em;
  
  public Item createItem(){
    Item item = new Item();
    item.setItemNm("테스트 상품");
    item.setPrice(10000);
    item.setItemDetail("상세설명");
    item.setItemSellStatus(ItemSellStatus.SELL);
    item.setStockNumber(100);
    item.setRegTime(LocalDateTime.now());
    
    item.setUpdateTime(LocalDateTime.now());
    return item;
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
  @DisplayName("지연 로딩 테스트")
  public void lazyLoadingTest(){
    Order order = this.createOrder();
    Long orderItemId = order.getOrderItems().get(0).getId();
    em.flush();
    em.clear();
    OrderItem orderItem = orderItemRepository.findById(orderItemId)
        .orElseThrow(EntityNotFoundException::new);
    System.out.println("Order class : " + orderItem.getOrder().getClass());
    System.out.println("===========================");
    orderItem.getOrder().getOrderDate();
    System.out.println("===========================");
  }
  
}