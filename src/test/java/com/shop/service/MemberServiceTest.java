package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //테스트 실행 후 롤백 처리
@TestPropertySource(locations = "classpath:application-test.properties") //테스트용으로 h2사용
class MemberServiceTest {
  
  @Autowired
  MemberService memberService;
  
  @Autowired
  PasswordEncoder passwordEncoder;
  
  public Member createMember() {
    MemberFormDto memberFormDto = new MemberFormDto();
    memberFormDto.setEmail("test@Email.com");
    memberFormDto.setName("홍길동");
    memberFormDto.setAddress("서울시 마포구 서교동");
    memberFormDto.setPassword("1234");
    return Member.createMember(memberFormDto, passwordEncoder);
  }
  
  @Test
  @DisplayName("회원가입 테스트")
  public void saveMemberTest() {
    Member member = createMember();
    Member savedMember = memberService.saveMember(member);
    
    assertEquals(member.getEmail(), savedMember.getEmail());
  }
}