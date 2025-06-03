package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
  
  private final MemberService memberService;
  private final PasswordEncoder passwordEncoder;
  
  @GetMapping(value = "/new")
  public String memberForm(Model model) {
    model.addAttribute("memberFormDto", new MemberFormDto());
    return "member/memberForm";
  }
  
  @PostMapping(value = "/new")
  public String MemberForm(@Valid MemberFormDto memberFormDto,
                           BindingResult bindingResult, Model model) { //검사 후 결과는 bindingResult에 담는다.
    
    if(bindingResult.hasErrors()){  //bindingResult.hasErrors를 호출해 에러가 있다면 회원가입 페이지로 이동
      return "member/memberForm";
    }
    
    try {
      Member member = Member.createMember(memberFormDto, passwordEncoder);
      memberService.saveMember(member);
    } catch (IllegalStateException e) {
      model.addAttribute("errorMessage", e.getMessage());  //중복회원가입 예외 발생시 메세지를 뷰로 전당
      return "member/memberForm";
    }
    
    return "redirect:/";
  }
}
