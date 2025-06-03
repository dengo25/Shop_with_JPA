package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties") //테스트용으로 h2사용
class ItemRepositoryTest {
  
  @PersistenceContext  //Querydsl 설정시 주입, 영속성 컨텍스트를 사용하기위해 EntityManaget 빈 주입
  EntityManager em;
  
  @Autowired
  ItemRepository itemRepository; //Bean 주입
  
  
  @Test
  @DisplayName("상품 저장 테스트")
  public void createItemTest() {
    Item item = new Item();
    item.setItemNm("테스트 상품");
    item.setPrice(10000);
    item.setItemDetail("테스트 상품 상세 설명");
    item.setItemSellStatus(ItemSellStatus.SELL);
    item.setStockNumber(100);
    item.setRegTime(LocalDateTime.now());
    item.setUpdateTime(LocalDateTime.now());
    Item savedItem = itemRepository.save(item);
    System.out.println(savedItem.toString());
    
  }
  
  @Test
  @DisplayName("상품 저장 테스트2")
  public void createItemList() {
    for (int i = 1; i <= 10; i++) {
      Item item = new Item();
      item.setItemNm("테스트 상품" + i);
      item.setPrice(10000 + i);
      item.setItemDetail("테스트 상품 상세 설명" + i);
      item.setItemSellStatus(ItemSellStatus.SELL);
      item.setStockNumber(100);
      item.setRegTime(LocalDateTime.now());
      item.setUpdateTime(LocalDateTime.now());
      Item savedItem = itemRepository.save(item);
    }
  }
  
  @Test
  @DisplayName("상품 저장 테스트3")
  public void createItemList3() {
    for (int i = 1; i <= 5; i++) {
      Item item = new Item();
      item.setItemNm("테스트 상품" + i);
      item.setPrice(10000 + i);
      item.setItemDetail("테스트 상품 상세 설명" + i);
      item.setItemSellStatus(ItemSellStatus.SELL);
      item.setStockNumber(100);
      item.setRegTime(LocalDateTime.now());
      item.setUpdateTime(LocalDateTime.now());
      itemRepository.save(item);
    }
    
    for (int i = 6; i <= 10; i++) {
      Item item = new Item();
      item.setItemNm("테스트 상품" + i);
      item.setPrice(10000 + i);
      item.setItemDetail("테스트 상품 상세 설명" + i);
      item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
      item.setStockNumber(0);
      item.setRegTime(LocalDateTime.now());
      item.setUpdateTime(LocalDateTime.now());
      itemRepository.save(item);
    }
  }
  
  
  @Test
  @DisplayName("상품명 조회 테스트")
  public void findByItemNmTest() {
    this.createItemList();
    List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
    for (Item item : itemList) {
      System.out.println(item.toString());
      
    }
  }
  
  @Test
  @DisplayName("상품명, 상품상세설명 or 테스트")
  public void findByNmOrItemDetailTest() {
    this.createItemList();
    
    List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트상품1", "테스트상품 상세 설명5");
    for (Item item : itemList) {
      System.out.println(item.toString());
      
    }
  }
  
  @Test
  @DisplayName("가격 Less Than 테스트")
  public void findByPriceLessThanTest() {
    this.createItemList();
    List<Item> itemList = itemRepository.findByPriceLessThan(10005);
    for (Item item : itemList) {
      System.out.println(item.toString());
      
    }
  }
  
  @Test
  @DisplayName("가격 내림차순 조회 테스트")
  public void findByPriceLessThanOrderByPriceDesc() {
    this.createItemList();
    List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
    for (Item item : itemList) {
      System.out.println(item.toString());
      
    }
  }
  
  @Test
  @DisplayName("@Query를 이용한 상품 조회 테스트") //높은 순으로 조회
  public void findByItemDetailTest() {
    this.createItemList();
    List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
    for (Item item : itemList) {
      System.out.println(item.toString());
    }
  }
  
  @Test
  @DisplayName("native Query 속성을 이용한 상품 조회 테스트")
  public void findByItemDetailByNative() {
    this.createItemList();
    List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
    for (Item item : itemList) {
      System.out.println(item.toString());
    }
  }
  
  @Test
  @DisplayName("Querydsl 조회 테스트1")
  public void queryDslTest() {
    this.createItemList();
    
    //JPAQueryFacotry를 이용하여 쿼리를 동적으로 생성합니다. 생성자의 파라미터로는 EntityManager 객체를 넣어줌
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    
    //Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용합니다.
    QItem qItem = QItem.item;
    
    JPAQuery<Item> query = queryFactory.selectFrom(qItem)
        .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
        .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
        .orderBy(qItem.price.desc());
    
    List<Item> itemList = query.fetch(); //fetch() 메소드 실행 시점에 쿼리문이 실행
    for (Item item : itemList) {
      
      System.out.println(item.toString());
      
    }
  }
  
  @Test
  @DisplayName("상품 Querydsl 조회 테스트 3")
  public void queryDslTest2() {
    this.createItemList3();
    BooleanBuilder booleanBuilder = new BooleanBuilder(); //BooleanBuilder는 쿼리에 들어갈 조건을 만들어주는 빌더
    QItem item = QItem.item;
    String itemDetail = "테스트 상품 상세 설명";
    int price = 10003;
    String itemSellStat = "SELL";
    
    booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
    booleanBuilder.and(item.price.gt(price));
    
    if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
      booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
    }
    
    //데이터를 페이징해 조회하도록 PageRequest.of() 메소드를 이용해 Pageble 객체를 생성
    Pageable pageable = PageRequest.of(0, 5);
    //QueryDslPredicateExecutor 인터페이스에서 정의한 findAll() 메소드를 이용해 조건에 맞는 데이터를 Page를 받아옴
    Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
    
    System.out.println("total elements : " + itemPagingResult.getTotalElements());
    
    List<Item> resultItemList = itemPagingResult.getContent();
    for (Item resultItem : resultItemList) {
      System.out.println(resultItem.toString());
      
    }
  }
}