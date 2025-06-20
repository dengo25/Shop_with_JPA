package com.shop.repository;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Cart;
import com.shop.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties") //테스트용으로 h2사용
class CartRepositoryTest {
  
  @Autowired
  CartRepository cartRepository;
  
  @Autowired
  MemberRepository memberRepository;
  
  @Autowired
  PasswordEncoder passwordEncoder;
  
  @PersistenceContext
  EntityManager em;
  
  public Member createMember() {
    MemberFormDto memberFormDto = new MemberFormDto();
    memberFormDto.setEmail("test@email.com");
    memberFormDto.setName("홍길동");
    memberFormDto.setAddress("서울시 종로구");
    memberFormDto.setPassword("1234");
    return Member.createMember(memberFormDto, passwordEncoder);
  }
  
  
  @Test
  @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
  public void findCartAndMemberTest() {
    Member member = createMember();
    memberRepository.save(member);
    
    Cart cart = new Cart();
    cart.setMember(member);
    cartRepository.save(cart);
    
    em.flush(); //db에 강제반영
    em.clear();
    
    Cart savedCart = cartRepository.findById(cart.getId())
        .orElseThrow(EntityNotFoundException::new);
    Assertions.assertEquals(savedCart.getMember().getId(), member.getId());
  }
}