# jwp-subway-path

## 테스트 환경 지하철 노선도

<img width="762" alt="image" src="https://github.com/HangangNow/HangangNowServer/assets/68818952/0fa6e7ff-5251-4e34-997d-13dbe2020563">

## 프로덕션 DB 설정

### DB setting

```shell
1. install [h2database](http://h2database.com/html/main.html)

2. cd Downloads/h2/bin

3. ./h2.sh

4. cd ~

5. touch subway.mv.db

6. connect H2 server with server mode  

```

### SCHEMA

```sql
create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    extraFare bigint not null,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,정
    line_id bigint not null,
    left_station_id bigint not null,
    right_station_id bigint not null,
    distance bigint not null,

    primary key(id),
    foreign key (line_id) references line (id),
    foreign key (left_station_id) references station (id),
    foreign key (right_station_id) references station (id)
);


```

## API SPEC

### 경로조회

- URL: localhost:8080/lines?startStation=?&endStation=?&?age=?
- method: GET
- Response body

```json
{
  "routes": [
    {
      "id": "1",
      "name": "강남역"
    },
    {
      "id": "4",
      "name": "서울역"
    },
    {
      "id": "2",
      "name": "잠실역"
    }
  ],
  "distance": 10,
  "fare": 2250
}
```

### 노선 생성

- URL: localhost:8080/lines
- method: POST
- Request body

```json
{
  "name": "1호선"
}
```

### 역 추가

- URL: localhost:8080/stations
- method: POST
- Request body

```json
{
  "name": "잠실역"
}
```

### 노선에 역 추가

- URL: localhost:8080/lines/{lineId}/sections
- method: POST
- Request body

```json
{
  "leftStationName": "잠실역",
  "rightStationName": "선릉역",
  "distance": "3"
}
```

### 노선에 역 제거

- URL: localhost:8080/lines/{lineId}/sections
- method: DELETE
- Request body

```json
{
  "stationName": "잠실역"
}
```

### 한 노선의 모든 역 조회

- URL: localhost:8080/lines/{id}/stations
- method: GET
- Response body

```json
{
  "id": 1,
  "name": "2호선",
  "extraFare": 100,
  "stations": [
    {
      "id": 1,
      "name": "잠실역"
    },
    {
      "id": 2,
      "name": "선릉역"
    }
  ]
}
```

### 전체 노선의 모든 역 조회

- URL: localhost:8080/lines/stations
- method: GET
- Response body

```json
[
  {
    "id": 1,
    "name": "2호선",
    "extraFare": 100,
    "stations": [
      {
        "id": 1,
        "name": "잠실역"
      },
      {
        "id": 2,
        "name": "선릉역"
      }
    ]
  },
  {
    "id": 2,
    "name": "3호선",
    "extraFare": 500,
    "stations": [
      {
        "id": 3,
        "name": "교대역"
      },
      {
        "id": 4,
        "name": "수서역"
      }
    ]
  }
]
```

## 기능 목록

### 노선에 역을 추가 한다.

- [x] 노선에 역이 존재하지 않는 경우 반드시 2개의 역을 등록해야 한다.
- [x] 노선에 역이 2개 이상 존재하는 경우
    - [x] 역의 좌, 우 위치에 역을 등록한다.
        - [x] 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.

### 노선에 역을 제거한다.

- [x] 노선에 역이 2개 존재하는 경우 두 역 모두 제거한다.
- [x] 노선에 역이 2개 이상 존재하는 경우
    - [x] 역을 제거하고 삭제하는 역의 좌, 우 역을 연결한다.

### 노선 조회

- [x] 노선에 포함된 역을 순서대로 보여준다.

### 전체 노선 조회

- [x] 모든 노선에 포함된 역을 순서대로 보여준다.

### 경로 조회

- [x] 한 역에서 다른 역 까지 가는 가장 짧은 경로를 조회한다.
    - [x] 경로와 요금을 함께 응답으로 반환한다.
        - [x] 10km 이하: 1250원
        - [x] 10~50km: 5km 마다 100원 추가
        - [x] 50km 초과: 8km 마다 100원 추가
    - [x] 이동 간 노선 별 추가 금액은, 가장 높은 금액을 기준으로 계산한다.
    - [x] 연령별 요 할인 정책을 반영한다.
      - [x] 청소년은 운임에서 350원을 공제한 금액의 20%할인
      - [x] 어린이는 운임에서 350원을 공제한 금액의 50%할인
    - [x] 갈 수 있는 경로가 없는 경우, `xx역 -> xx역은 갈 수 없는 경로입니다.` 를 반환한다.

## 도메인 기능 목록

### Distance

- [x] 역 간의 거리는 0~100 사이만 가능하다.

### Station

- [x] 역 이름은 2이상 10이하만 가능하다.

### Line

- [x] 노선에 역이 존재하는지 확인한다.
- [x] 구간에 역이 존재하는지 확인한다.
- [x] 역으로 구간을 찾을 수 있다.
- [x] 구간이 하나만 있는지 확인한다.
- [x] 해당 역이 종점인지 확인한다.
- [x] 전체 역을 상행 -> 하행 순서대로 반환한다.
- [x] 노선간 추가 요금을 가질 수 있다.
