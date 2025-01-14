<h1 align="center">MODO MODO<br><strong>모든 도시락, 모두의 도시락</strong></h1>
<h3 align="center">모두를 위한 도시락 예약 플랫폼</h3>
<h3 align="center">24/03/01 ~ 24/10/31</h3>


![localhost_3000_ (9)](https://github.com/user-attachments/assets/e12d2b02-ce8d-4c02-98c5-9d44150be74a)

---
## 1. 프로젝트 개요

**📊 시장 분석**
- 기존 프랜차이즈 매장은 개별 예약 사이트 운영으로 **효율성 및 다양성 부족**  
- 서비스 지역 제한 및 유지 비용 증가로 **경쟁력 약화**  
- 독립 사이트 운영 부담으로 **프랜차이즈 사업자 어려움 가중**  


**💡 필요성**  
- 다양한 도시락 브랜드를 한곳에 모아 **편리한 선택 제공**  
- **정기 결제 및 구독 서비스**로 사용자 편의성 극대화  
- **시간 절약과 건강한 식사** 제공  


**🚀 기대 효과**  
- **편리성 증대**: 원하는 도시락을 쉽고 빠르게 선택  
- **상권 활성화**: 고객과 가맹점 간 협력 강화  
- **고객 확보**: 신규 고객 유치와 기존 고객 유지  
- **비즈니스 성과 향상**: 마케팅과 매출 증대 효과 기대  
---
## 2. 팀원 소개 및 역할
백엔드, 프론트엔드 공통
- **서동현**: 전체적인 시스템 구성 및 API 설계, 결제 시스템 주도  
- **서은빈**: API 설계 및 데이터베이스 관리, AI 리뷰 요약 주도  
- **이여민**: 사용자 인터페이스 개발 및 디자인 최적화, UI/UX 개선 주도  
---
## 3. 개발 환경

- **백엔드**: ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white) ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-EE4C2C?style=for-the-badge&logo=IntelliJ%20IDEA&logoColor=white)

- **프론트엔드**: ![Next.js](https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=next.js&logoColor=white) ![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black) ![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=node.js&logoColor=white)

- **협업 툴**: ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) ![ERDCloud](https://img.shields.io/badge/ERDCloud-0072FF?style=for-the-badge&logo=erdcloud&logoColor=white)

---
## 4. 프로젝트 구조
<a href="https://www.erdcloud.com/d/8Lf2f63JR7jpDJMqQ" target="_blank">
    <img src="https://img.shields.io/badge/ERDCloud-0072FF?style=for-the-badge&logo=erdcloud&logoColor=white" alt="ERDCloud" />
</a>

---
## 5. 차별점 
- **결제** : 원하는 날짜, 시간대에 주문. 예약 가능 

- **장바구니** : 여러 업체의 메뉴 담기 가능 

- **AI 리뷰 요약** : 리뷰 리스트 기반 AI 선택지 반환 

---  
## 6. 설치 방법


- **프론트엔드**
```sh
npx create-next-app@latest //next.js 모듈 다운
npm install
npm run dev

```

- **백엔드**
```sh
Spring Boot 3.0 이상, JDK 17
application.properties 데이터베이스 연결

gradlew build
```

---
## 7. 난제 극복

- **결제**  
    다중 업체 결제는 결제 수단, 정산, 환불로 인해 복잡 -> 업체별 개별 결제로 변경하여 결제, 정산, 환불을 각각 관리

- **JSX -> TSX**  
    트렌디한 TSX 사용을 위해 TSX를 학습 후 문법 수정 

- **AI 리뷰 요약**  
    AI가 요구한 내용과 동떨어진 대답이 다수 출력 -> 선택지 기반의 응답 반환 방식으로 수정 + 예외 처리를 강화 + 데이터 추가 학습을 통한 상황에 맞는 답변을 개선


---
## 9. 프로젝트 후기

- **서동현**  
"1년이라는 시간 동안 팀원들과의 협력으로 프로젝트를 무사히 마친 것 같다. 혼자보다는 둘이 낫고 둘보다는 셋이 낫듯이 혼자 했었으면 이렇게 완성도 높게 만들지 못했을 것이다. 팀원들에게는 감사한 마음뿐이다."  

- **서은빈**  
"처음 경험하는 장기 프로젝트였는데, 팀원들의 협력 덕분에 성공적으로 마무리할 수 있었던 것 같다. 이번 프로젝트가 앞으로의 도전에 있어서도 큰 원동력이 될 것이라 생각한다."

- **이여민**  
"이번 장기 프로젝트를 통해 협력과 개발 경험을 쌓을 수 있었다. 개발을 하면서 서버와 프론트 및 권한 변경을 배울 수 있었다. 팀원들과의 토론을 통해 완성도 높은 결과물을 만들 수 있어 이 경험을 바탕으로 거름이 되겠다."

---


  
