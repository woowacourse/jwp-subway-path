# jwp-subway-path

## 🎯 기능 요구사항

- [X]  역 등록
    - [X]  첫 등록인 경우
        - [X]  두 역을 한번에 등록한다
    - [X]  상행 종점인 경우
        - [X]  기존 상행 종점의 이전 역으로 등록한다
        - [X]  다음 역 까지의 거리를 등록한다
        - [X]  기존 상행 종점의 이전 거리를 등록한다
    - [X]  하행 종점인 경우
        - [X]  기존 하행 종점의 다음 역으로 등록한다
        - [X]  이전 역 까지의 거리를 등록한다
        - [X]  기존 하행 종점의 다음 거리를 등록한다
    - [X]  중간 역인 경우
        - [X]  다음 역과 이전 역 사이에 삽입 가능한 거리인지 검증한다
        - [X]  다음 역과 이전 역의 연결을 변경한다
        - [X]  다음 역과 이전 역 까지의 거리를 변경한다
- [X]  역 제거
    - [X]  상행 종점인 경우
        - [X]  다음 역의 이전 역을 삭제한다
    - [X]  하행 종점이 경우
        - [X]  이전 역의 다음 역을 삭제 한다
    - [X]  중간 역인 경우
        - [X]  이전 역과 다음 역을 연결한다
        - [X]  이전 역과 다음 역 사이의 거리를 변경한다
    - [X]  마지막 두 역인 경우
        - [X]  두 역을 모두 삭제한다
- [X]  노선 조회 개선
    - [X]  특정 노선의 역 목록을 조회한다
    - [X]  상행 종점을 찾는다
    - [X]  상행 종점부터 다음 역을 탐색한다
    - [X]  하행 종점에 도달하면 종료한다
- [X]  노선 목록 조회 개선
    - [X]  전체 노선의 역 목록을 조회한다
    - [X]  상행 종점을 찾는다
    - [X]  상행 종점부터 다음 역을 탐색한다
    - [X]  하행 종점에 도달하면 종료한다

## 🎯 추가된 기능 요구사항

- [X] 경로 조회 API 구현
- [X] 요금 조회 기능 추가
  - [X] 10km 까지 기본 요금 1250원 
  - [X] 11km ~ 50km 까지 5km 마다 100원 추가 (11km는 1350원)
  - [X] 51km ~ 8km 마다 100원 추가


## 🛠️설계

### DB

**station** :

| Column        | Type                  | Constraints      |
| ------------- | --------------------- |------------------|
| station_id    | BIGINT AUTO_INCREMENT | NOT NULL, PK     |
| name          | VARCHAR(255)          | NOT NULL, UNIQUE |

**line** :

| Column        | Type                  | Constraints      |
| ------------- | --------------------- |------------------|
| line_id       | BIGINT AUTO_INCREMENT | NOT NULL, PK     |
| name          | VARCHAR(255)          | NOT NULL, UNIQUE |
| color         | VARCHAR(20)           | NOT NULL         |

**section** :

| Column         | Type                  | Constraints  |
| -------------- | --------------------- |--------------|
| section_id     | BIGINT AUTO_INCREMENT | NOT NULL, PK |
| up_station_id  | BIGINT                | NOT NULL     |
| down_station_id| BIGINT                | NOT NULL     |
| distance       | INT                   | NOT NULL     |
| line_id        | BIGINT                | NOT NULL     |
| list_order     | INT                   | NOT NULL     |
