# FISA_SeatHelper
FISA 5기 클라우드 엔지니어링 반장을 도와주기 위한 자동 자리배치 프로그램

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

## 🎯 프로젝트 개요
- **목표** : 클라우드 엔지니어링 수강생 자리배치 진행하기
- **핵심 내용** : 저시력자를 고려하여 최대한 앞자리에 배치함과 동시에 가급적이면 기존에 짝꿍이 아니였던 사람들과의 자리배치를 랜덤하게 하고자 한다.

## 🧩 과정
1주차에는 학생들의 자리 배치를 랜덤하게 섞어 배치하도록 구현하였다. 이번 프로젝트에서는 최대한 같은 짝꿍을 한 인원이 적고, 안경을 쓴 사람이 우선적으로 앞으로 배치되도록 하는 것이 목표이다.

## 📜 기존의 Data Files
- src/database/student.txt — 학생 정보 파일
- src/database/seat.txt — 현재 자리 배치 정보 파일

## 💾 개선한 Data Files
- src/database/student.txt — 
- src/database/seat.txt — 

## 🛠️Tech Stack
- Java 17+
- MySQL

## ✅ 데이터베이스 구조
### 🗄️ 학생(참여자) 기본 테이블

```sql
CREATE TABLE student (
    no INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    birth_year INT,
    mbti VARCHAR(4),
    wears_glasses BOOLEAN
);
```
🟢 옆자리 기록 테이블 (핵심!)
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
이후, 기존에 수집했던 수강생들의 정보를 입력하였다. 
```
CREATE TABLE student (
    no INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    age INT,
    mbti VARCHAR(4),
    glass BOOLEAN
);

INSERT INTO student (no, name, age, mbti, glass) VALUES
(1, '강한솔', 1995, 'ESFP', TRUE),
(2, '고태우', 2001, 'INFJ', FALSE),
(3, '김동민', 2001, 'ENFJ', FALSE),
(4, '김문석', 2001, 'ESFJ', FALSE),
(5, '김현수', 1998, 'ESTJ', FALSE),
(6, '문민경', 2002, 'ISFP', FALSE),
(7, '박여명', 2000, 'ISTJ', FALSE),
(8, '박지원', 1998, 'ISFJ', FALSE),
(9, '서민지', 2001, 'ESFJ', TRUE),
(10, '신기범', 2000, 'ISTP', FALSE),
(11, '신준수', 2000, 'ENFP', FALSE),
(12, '이기현', 1999, 'ISTJ', FALSE),
(13, '이노운', 1998, 'ENTP', FALSE),
(14, '이영주', 2002, 'INTP', TRUE),
(15, '이용훈', 2000, 'ISTP', FALSE),
(16, '이정이', 2001, 'INFJ', FALSE),
(17, '이제현', 1998, 'INFJ', TRUE),
(18, '이조은', 2001, 'ISFJ', FALSE),
(19, '임유진', 1999, 'ISTJ', FALSE),
(20, '임채준', 1999, 'ISTJ', FALSE),
(21, '장송하', 2004, 'ESFP', TRUE),
(22, '전수민', 2000, 'INTP', TRUE),
(23, '정다빈', 2002, 'ENFJ', FALSE),
(24, '정서현', 2001, 'ISTJ', FALSE),
(25, '최소영', 2001, 'ENTP', TRUE),
(26, '최홍석', 2000, 'INFJ', FALSE),
(27, '홍윤기', 2000, 'ISTJ', FALSE),
(28, '홍혜원', 2001, 'INTJ', FALSE),
(29, '황병길', 2001, 'INTP', FALSE),
(30, '황지환', 2000, 'INTP', TRUE);

INSERT INTO partner_history (student_no, partner_no, date)
SELECT s1.no AS student_no, s2.no AS partner_no, CURDATE()
FROM student s1
JOIN student s2 ON s1.no != s2.no;
```


## ✅ 주요 로직
자리 배치를 4*8의 배열로 인지하는 상황에서 설명하겠다.
  1. 1열 4,5행에는 안경을 쓴 인원 중 랜덤으로 배치한다.
  2. 2~4열 4,5행에는 전체 인원 중 랜덤으로 배치한다.
  3. 1열(가장 앞자리)부터 4열까지 각 배치된 인원을 기준으로 좌우로 짝궁을 찾는다
     1. partner_history테이블에서 현재 배치된 인원을 student_no로 조회하여 partner_no가 가장 적게 나온 횟수의 학생을 찾는다
     2. 해당 테이블에서 안경을 쓴 인원을 우선 정렬하여 1명을 뽑아온다.

자리는 앞열부터 채워가므로 안경 쓴 인원은 앞 열에 우선 배치된다.



## ✅ 좌석 배치 결과 예시
```````````````````
// console
1 │ [김현수] [신기범] [이노운] [홍혜원]  │   │ [장송하] [이정이] [임채준] [최소영]
2 │ [김동민] [이제현] [강한솔] [이기현]  │   │ [서민지] [정다빈] [임유진] [최홍석]
3 │ [정서현] [황지환] [황병길] [고태우]  │   │ [이영주] [문민경] [이용훈] [박지원]
4 │ [박여명] [전수민] [빈자리] [빈자리]  │   │ [김문석] [신준수] [홍윤기] [이조은]

````````````````````

## ✅ 결과

## ✅ 고찰 및 트러블슈팅
초기에 프로젝트를 진행할 때는 좌석 무작위 배치를 계획하였지만 저시력자와 기존 짝꿍이였던 것에 대한 여부를 반영하여 자리배치를 진행하는 부분에 대한 이슈가 있었다.
- #### 자리를 어떻게 배치할 것인가
  - 무작위로 대입하면 된다는 기존 생각과는 달리 마지막에 자리를 배치시킬 때 무한루프가 발생한다는 이슈가 있었다.
- #### 데이터베이스 구성을 어떻게 할 것인가
  - 기본 테이블만으로는 이전에 짝꿍을 해보지 않았던 사람들과의 배치에
   어려움이 있었다.
  - 학생들의 정보를 저장하는 기본 테이블(student) 외에도 학생들과 서로 짝꿍이 된 횟수를 기록하는 테이블(partner_history)을 추가로 구현하여 해결하였다.

## ✅ 향후 확장 가능성
향후 웹연동을 활용하여 확장 가능하기에 기회가 된다면 토이프로젝트를 진행 예정이다.
