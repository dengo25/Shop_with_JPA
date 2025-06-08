package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_img")
@Getter
@Setter
public class ItemImg extends BaseEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "item_img_id")
  private Long id;
  
  private String imgName;
  
  private String oriTmgName; //원본 이미지 파일명
  
  private String imgUrl; //이미지 조회 경로
  
  private String repimgYn; //대표 이미지 여부
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;
  
  public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
    this.oriTmgName = oriImgName;
    this.imgName = imgName;
    this.imgUrl = imgUrl;
  }
}
