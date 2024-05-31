package com.example.modoproject.BusinessOwnerDashBoard.entity;

// Menu.java (Entity)
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;

@Entity// 데이터 마이그레이션 합집합, 업체마다
//회사마다 api를 만든다, 요청하는 것을 자동으로 필요한 것만
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long companyId; //업체ID, 가맹점 식별 코드
    private String name;
    private int price;
    private Date createdAt;
    private String imageUrl;
    private String merchanUid; // 새로운 필드 추가

    public String getMerchanUid() {
        return merchanUid;
    }

    public void setMerchanUid(String merchanUid) {
        this.merchanUid = merchanUid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
