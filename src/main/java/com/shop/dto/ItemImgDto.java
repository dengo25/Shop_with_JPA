package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
  
  private Long id;
  
  private String imgName;
  
  private String oriImgName;
  
  private String imgUrl;
  
  private String repImgYn;
  
  private static ModelMapper modelMapper = new ModelMapper();
  
  //ItemImg 엔티티 객체를 파라미터로 받아서 ImteImg객체의 자료혀오가 멤버변수의 이름이 같을 때
  //ItemImgDto로 값을 복사해서 반환.
  public static ItemImgDto of(ItemImg itemImg) {
    return modelMapper.map(itemImg,ItemImgDto.class);
  }
  
}