
본 프로젝트는 MSA로 구성한 과거 작업본입니다. 모놀리틱 작업본은 아래 리포지토리를 참고해주세요.<br>
https://github.com/hseong3243/online-study



# 나의 공부방

## 프로젝트 내용

동일한 학습 목표를 가진 학습자들과 그룹을 형성하고, 학습을 기록하여 도표로 보여줄 수 있는 웹 어플리케이션을 제공하고자 하였습니다.

## 주요 기능

### 1. 그룹 스터디

- 사용자는 그룹을 생성, 참가하여 같은 학습 목표를 가진 사용자들과 함께 할 수 있다.<br>
- 학습 티켓을 발행받아 학습 시간을 기록하고, 그룹원들의 학습 상태 변화(학습, 휴식, 종료)가 실시간으로 갱신<br>


### 2. 공부 기록

- 그룹 스터디를 통해 서버에 저장된 학습 티켓을 기반으로 학습별로 일간, 주간, 월간 기록 차트를 제공


## 발생문제 및 해결방법

### 1. 스터디 화면의 갱신을 위한 주기적인 요청은 비효율적이다.

- 요청 주기가 짧다면 단시간에 너무 많은 요청이 발생할 것이고, 요청 주기가 길다면 그룹 스터디 화면은 아무런 갱신이 발생하지 않는 의미 없는 페이지가 될 것입니다.<br>
- 따라서 Stomp 웹 소켓 통신을 이용하여 사용자 학습 티켓의 상태 변동이 발생할 때, 해당 사용자의 티켓 정보만을 스터디 그룹의 토픽에 발행해주는 것으로 해결하였습니다.

## 개발 스택

### Frontend

- Vue.js

### Backend

- Spring Boot<br>
- Spring Data JPA<br>
- Spring Cloud<br>
- Kafka<br>

### Database

- MariaDB

## 아키텍처




## 배운 점, 부족했던 점


- 마이크로서비스로 애플리케이션을 개발하는 것에 대한 장, 단점을 이해할 수 있었습니다.<br>
- 하나의 응답이 가지고 있는 데이터의 양이 많아지면서 자연스레 서비스간의 의존성이 강해졌습니다.<br>
- 테스트 코드 작성에 소홀했습니다.<br>

위와 같은 아쉬움으로 인해 현재 본 프로젝트를 모놀리틱으로 다시 구현하는 중에 있습니다.
