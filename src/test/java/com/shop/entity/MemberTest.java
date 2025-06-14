package com.shop.entity;

import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional //테스트 실행 후 롤백 처리
@TestPropertySource(locations = "classpath:application-test.properties") //테스트용으로 h2사용
public class MemberTest {
  
  
  @Autowired
  MemberRepository memberRepository;
  
  @PersistenceContext
  EntityManager em;
  
  @Test
  @DisplayName("Auditing 테스트")
  @WithMockUser(username = "gildong", roles = "USER")
  public void auditingTest(){
    Member newMember = new Member();
    memberRepository.save(newMember);
    
    em.flush();
    em.clear();
    
    Member member = memberRepository.findById(newMember.getId())
        .orElseThrow(EntityNotFoundException::new);
    
    System.out.println("register time : " + member.getRegTime());
    System.out.println("update time : " + member.getUpdateTime());
    System.out.println("create member : " + member.getCreatedBy());
    System.out.println("modify member : " + member.getModifiedBy());
  }
  
}