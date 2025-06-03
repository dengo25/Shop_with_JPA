package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,
    //QueryDslPredicateExecutor 인터페이스 상속을 추가
    QuerydslPredicateExecutor<Item> {
  
  List<Item> findByItemNm(String itemNm);
  
  List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
  
  List<Item> findByPriceLessThan(Integer price); // 파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회
  
  List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); //내림차순 정렬, 상품 가격 높은 순
  
  @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
  List<Item> findByItemDetail(@Param("itemDetail") String itemDetail); //itemDetail 변수를 “like % %” 사이에 “:itemDetail”
  
  //nativeQuery 사용해보기
  @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
  List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
