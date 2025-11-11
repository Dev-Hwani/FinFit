# FinFit 가계부 웹 어플리케이션 만들기
## 프로젝트 소개

FinFit은 개인의 수입과 지출을 효율적이고 안전하게 관리하여 재무 습관을 개선할 수 있도록 설계된 웹 기반 가계부 애플리케이션입니다. 
스프링 부트를 학습하며 실제 프로젝트에 적용하기 위해 개발된 이 프로젝트는 작지만 보안이 강화된 실무 수준의 가계부 CRUD 기능을 제공합니다.

## 개발환경
- Java SE 17: 최신 Java 기능과 안정적인 서버 개발 환경 제공
- Gradle: 프로젝트 빌드 및 의존성 관리

## 기술 스택
- Backend: Java, Spring Boot, Spring Security, JPA, Hibernate
- Frontend: HTML, CSS, JavaScript, Bootstrap, Tymeleaf
- Database: MySQL
- Server: Tomcat

## 주요 기능
### 사용자 관리 (User Management)
- **회원가입 / 로그인 / 로그아웃**
  - Spring Security 기반 인증 처리
  - 비밀번호는 BCryptPasswordEncoder 암호화 저장
  - 이메일 기반 로그인
- **마이페이지 관리**
  - 사용자 정보 조회 및 수정
  - 비밀번호 변경, 연락처, 주소, 생년월일 등 정보 수정 가능
- **아이디 / 비밀번호 찾기**
  - 이메일과 전화번호를 통해 아이디 찾기
  - 임시 비밀번호 발급 및 안전하게 변경 가능
- **보안 강화**
  - Spring Security를 활용한 인증 및 권한 처리
  - 관리자 계정과 일반 사용자 계정 구분

### 가계부 관리 (Income & Expense Management)
- **CRUD 기능**
  - 수입/지출 항목 추가, 수정, 삭제 가능
  - 항목에 날짜, 카테고리, 금액, 설명 등을 입력
- **조회 및 필터링**
  - 특정 날짜, 월, 연도 기준으로 필터링 가능
  - 카테고리별 조회 가능
- **통계 및 요약**
  - 월별 수입, 지출, 잔액 집계
  - 카테고리별 금액 합계 계산
  - 달력 기반 이벤트 제공 (월별 내역 시각화)
- **실무 수준 API 제공**
  - AJAX를 이용한 월별 요약 API
  - 프론트엔드에서 통계와 데이터를 동적으로 조회 가능

 ## 화면 설명
  - 메인 페이지
 <img width="2559" height="1599" alt="image" src="https://github.com/user-attachments/assets/83e5793d-4e21-49d5-bc4b-666a52c85ad1" />

- 회원가입
<img width="2559" height="1599" alt="image" src="https://github.com/user-attachments/assets/a1eff1f2-7e00-48df-9dfd-59a12eb33e65" />

- 로그인
<img width="2559" height="1599" alt="image" src="https://github.com/user-attachments/assets/580bb8f7-5a7d-4947-a3a3-193b329ea0fc" />

- 가계부 페이지
<img width="2559" height="1599" alt="image" src="https://github.com/user-attachments/assets/96c463da-7815-4dc6-ab0c-a126efaab9c9" />

- 달력
<img width="2559" height="1599" alt="image" src="https://github.com/user-attachments/assets/cff8c718-bb6c-40e7-a159-12ff371213f4" />

- 가계부 상세 내역
<img width="2559" height="1599" alt="image" src="https://github.com/user-attachments/assets/ed0edce8-7c9e-4c67-baff-cc7dc69eeaa0" />

- 마이페이지
<img width="2559" height="1599" alt="image" src="https://github.com/user-attachments/assets/62000c87-597c-493b-bb0c-6ee82ad1e82b" />

## 프로젝트 구조
```
finfit/
├── src/
│   ├── main/
│   │   ├── java/com/mysite/finfit/
│   │   │   ├── FinfitApplication.java        # Spring Boot 메인 실행 클래스
│   │   │   ├── MainController.java           # 메인 페이지 컨트롤러
│   │   │   │
│   │   │   ├── account/                      # 💰 수입/지출 관리 도메인
│   │   │   │   ├── IncomeExpense.java
│   │   │   │   ├── IncomeExpenseController.java
│   │   │   │   ├── IncomeExpenseRepository.java
│   │   │   │   └── IncomeExpenseService.java
│   │   │   │
│   │   │   ├── budget/                       # 📊 예산 관리 도메인
│   │   │   │   ├── Budget.java
│   │   │   │   ├── BudgetController.java
│   │   │   │   ├── BudgetRepository.java
│   │   │   │   └── BudgetService.java
│   │   │   │
│   │   │   ├── user/                         # 👤 사용자 관리 도메인
│   │   │   │   ├── User.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── UserSecurityService.java
│   │   │   │   ├── UserRole.java
│   │   │   │   └── UserException.java
│   │   │   │
│   │   │   └── config/                       # ⚙️ 보안 및 설정 관련
│   │   │       └── SecurityConfig.java
│   │   │
│   │   └── resources/
│   │       ├── templates/                    # Thymeleaf HTML 템플릿
│   │       ├── static/css/                   # 정적 리소스 (CSS 등)
│   │       │   └── style.css
│   │       └── application.properties        # 프로젝트 환경 설정
│   │
│   └── test/java/com/mysite/finfit/
│       └── FinfitApplicationTests.java       # 단위 테스트 파일
│
├── build.gradle                              # Gradle 빌드 설정
├── settings.gradle                           # Gradle 프로젝트 설정
├── gradlew / gradlew.bat                     # Gradle 실행 스크립트
├── HELP.md                                   # Spring 기본 가이드 파일
└── README.md                                 # 프로젝트 설명 파일
```
