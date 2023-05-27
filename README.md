# jwp-subway-path

## API SPEC

### 최단 경로 조회

- URL: localhost:8080/path
- method: GET
- Request body

```json
{
  "fromStation": "잠실역",
  "toStation": "삼성역"
}
```

- Response body

```json
{
  "fare": 1650,
  "distance": 30,
  "stations": [
    {
      "stationName": "잠실역"
    },
    {
      "stationName": "잠실새내역"
    },
    {
      "stationName": "종합운동장역"
    },
    {
      "stationName": "삼성역"
    }
  ]
}
```

### 역 추가

- URL: localhost:8080/sections
- method: POST
- Request body

```json
{
  "lineId": 1,
  "leftStationName": "잠실역",
  "rightStationName": "선릉역",
  "distance": "3"
}
```

### 역 제거

- URL: localhost:8080/sections
- method: DELETE
- Request body

```json
{
  "lineId": 1,
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

### 최단 경로 조회

- 출발역과 도착역 사이의 최단거리 경로를 조회한다.
- 출발역과 도착역을 입력받는다.
- 최단거리를 찾아서 가중치를 전부 더한 값을 통해 거리를 계산한다.
-
- 

