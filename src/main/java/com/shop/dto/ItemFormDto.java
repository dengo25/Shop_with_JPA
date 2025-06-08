package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
  
  private Long id;
  
  @NotBlank(message = "상품명은 필수 입력 값입니다.")
  private String itemNm;
  
  @NotBlank(message = "가격은 필수 입력 값입니다.")
  private Integer price;
  
  @NotBlank(message = "이름은 필수 입력 값입니다.")
  private String itemDetail;
  
  @NotBlank(message = "재고는 필수 입력 값입니다.")
  private Integer stockNumber;
  
  private ItemSellStatus itemSellStatus;
  
  //상품 수정할 떄 상품 이미지 정보를 저장하는 리스트
  private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
  
  //이미지 아이디를 저장하는 리스트, 수정 시에 이미지 아이디를 담아줄 용도로 사용
  private List<Long> itemImgIds = new ArrayList<>();
  
  private static ModelMapper modelMapper = new ModelMapper();
  
  public Item creatItem() {
    return modelMapper.map(this, Item.class);
  }
  
  public static ItemFormDto of(Item item) {
    return modelMapper.map(item, ItemFormDto.class);
  }
  
}
