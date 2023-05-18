# jwp-subway-path

## API SPEC

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

- [ ] 한 역에서 다른 역 까지 가는 가장 짧은 경로를 조회한다.
    - [ ] 경로와 요금을 함께 응답으로 반환한다.
    - [ ] 갈 수 있는 경로가 없는 경우, `xx역 -> xx역은 갈 수 없는 경로입니다.` 를 반환한다.

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
