# 💺 FISA_SeatHelper
FISA 5기 클라우드 엔지니어링 반장을 도와주기 위한 자동 자리배치 프로그램

---

## 👥팀원
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/GodNowoon">
        <img src="https://github.com/GodNowoon.png" width="100px;" alt="이노운"/><br />
        <sub><b>이노운</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/0-zoo">
        <img src="https://github.com/0-zoo.png" width="100px;" alt="이영주"/><br />
        <sub><b>이영주</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/dldydgns">
        <img src="https://github.com/dldydgns.png" width="100px;" alt="이용훈"/><br />
        <sub><b>이용훈</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/ddddabi">
        <img src="https://github.com/ddddabi.png" width="100px;" alt="정다빈"/><br />
        <sub><b>정다빈</b></sub>
      </a>
    </td>
  </tr>
</table>

---

## 🎯 프로젝트 개요
- **목표** : 클라우드 엔지니어링 수강생들의 효율적이고 공정한 자리 배치를 도와주는 프로그램을 개발
- **핵심 내용** :
  - 안경 착용자를 고려하여 최대한 앞자리에 배치
  - 기존에 함께 앉았던 학생과의 중복을 피하면서 새로운 조합을 만들어 교류 활성화
 
본 프로그램은 단순 무작위가 아닌, **안경 착용자 고려와 팀워크 증진**을 모두 고려한 맞춤형 자리 배치 로직을 갖추고 있습니다.

## 🧩 개발 과정

1. **1차 목표**: 학생 명단을 기반으로 단순 무작위 자리 배치 로직 구현  
2. **2차 목표**: 저시력자(안경 착용자)를 앞자리에 우선 배치하는 로직 추가  
3. **3차 목표**: 기존에 함께 앉았던 기록을 분석하여 최대한 새로운 파트너와 배치되도록 DB 연동

이렇게 점진적으로 기능을 개선하며 사용자 편의성과 프로그램을 강화하였습니다.

## 📜 Local Data Files
- src/database/seat.txt — 현재 자리 배치 정보 파일


## 🛠️Tech Stack
<div>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
</div>

---

## ✅ 데이터베이스 구조
### ERD
<img width="605" height="217" alt="image" src="https://github.com/user-attachments/assets/47afb25d-2e9c-4d49-b751-311230abb98f" />


### 🗄️ 학생(참여자) 기본 테이블

학생 개별 정보를 저장합니다. 이름, 생년, MBTI, 안경 착용 여부 등의 정보를 관리합니다.

```
CREATE TABLE student (
    no INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    birth_year INT,
    mbti VARCHAR(4),
    wears_glasses BOOLEAN
);
```

### 🗄️ 파트너 히스토리 테이블
학생들의 짝꿍 기록을 관리합니다. 이 데이터를 활용해 중복 최소화 배치를 구현합니다.

```
CREATE TABLE partner_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_no INT NOT NULL,
    partner_no INT NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (student_no) REFERENCES student(no),
    FOREIGN KEY (partner_no) REFERENCES student(no)
);
```

#### 예시 데이터 입력


```
CREATE TABLE student (
    no INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    age INT,
    mbti VARCHAR(4),
    glass BOOLEAN
);

INSERT INTO student (no, name, age, mbti, glass) VALUES
(1, '홍길동', 1995, 'ESFP', TRUE),
(2, '감길동', 2001, 'INFJ', FALSE),
(3, '이길동', 2001, 'ENFJ', FALSE),
(4, '김길동', 2001, 'ESFJ', FALSE),
(5, '박길동', 1998, 'ESTJ', FALSE),
.....
(26, '정길동', 2000, 'INFJ', FALSE),
(27, '장길동', 2000, 'ISTJ', FALSE),
(28, '황길동', 2001, 'INTJ', FALSE),
(29, '차길동', 2001, 'INTP', FALSE),
(30, '최길동', 2000, 'INTP', TRUE);

INSERT INTO partner_history (student_no, partner_no, date)
SELECT s1.no AS student_no, s2.no AS partner_no, CURDATE()
FROM student s1
JOIN student s2 ON s1.no != s2.no;
```


## ✅ 주요 로직
자리 배치는 4행 8열 배열 형태로 설계되어 있습니다.

1️⃣ 앞 열 중앙 자리에는 안경 착용자 중 랜덤으로 배치

2️⃣ 그 외 자리는 랜덤 배치 + partner_history 분석을 통해 기존 파트너 제외

3️⃣ 각 자리 기준으로 새로운 파트너를 선정
- partner_history 테이블에서 최소 짝꿍 횟수를 기준으로 정렬
- 안경 착용자 우선 순위 고려

4️⃣ 좌측 기둥 자리는 구조상 빈자리로 유지

이 로직을 통해 저시력자 배려와 새로운 네트워킹 기회를 동시에 제공합니다.


## ✅ 좌석 배치 결과 예시

```
1 │ [김철수] [이영희] [박민수] [최수진]  │   │ [정가은] [오세훈] [유재석] [강호동]
2 │ [한지민] [김유진] [송지호] [이도현]  │   │ [박하늘] [전세진] [윤시우] [백지원]
3 │ [신예린] [조민재] [권하늘] [양지웅]  │   │ [임세린] [황도윤] [문태윤] [홍정우]
4 │ [배지훈] [최예린] [빈자리] [빈자리]  │   │ [김태현] [서지훈] [남도현] [노지민]
```

## ✅  ConsoleView 결과

<img width="1005" height="339" alt="image" src="https://github.com/user-attachments/assets/d4c1f433-7bfc-4255-9a92-6b654c41f01a" />


## ✅ 고찰 및 트러블슈팅
- 자리 배치 방식 고민

단순 랜덤 배치로는 마지막 자리에서 무한 루프가 발생할 위험이 있었습니다. partner_history 기반 선택 방식으로 해결했습니다.

- DB 설계 문제

기존 단일 student 테이블로는 파트너 히스토리를 관리하기 어려웠습니다. partner_history 테이블 추가로 확장성을 해결했습니다.

- 저시력자 배려

앞자리 우선 배치 로직을 위해 중앙 자리부터 채우도록 로직을 수정했습니다.

## ✅ 향후 확장 가능성
- 웹 기반 UI로 자리 배치 결과 시각화
- 자리 히스토리 분석 리포트 및 통계 기능

