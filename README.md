# jwp-subway-path

## 도메인

- Path
    - id, 하행역, 거리를 가지는 경로 객체

- Line
    - id, 이름, 색, Map<Station, Path> 컬렉션을 가짐

- Station
    - id, 이름을 가짐

- ShortestWayCalculator
    - Line 객체를 파라미터로 받아 최단 경로와 거리를 계산하는 객체

- Direction
    - 노선에 경로 추가 시 상행, 하행을 결정하는 ENUM

- AddPathStrategy
    - 노선에 경로 추가시 분기와 중복 코드를 제거하기 위해 도입

## API

- GET /lines/{id}
    - 특정 노선 조회
    - 지하철역을 경로에 따라 순서대로 반환
    - Response status OK

```json
{
  "id": 1,
  "name": "1호선",
  "color": "파랑",
  "stations": [
    {
      "id": 1,
      "name": "강남"
    },
    {
      "id": 2,
      "name": "잠실"
    }
  ]
}
```

- GET /lines
    - 전체 노선 조회
    - 지하철역을 경로에 따라 순서대로 반환
    - Response Status OK

```json
[
  {
    "id": 1,
    "name": "1호선",
    "color": "파랑",
    "stations": [
      {
        "id": 1,
        "name": "강남"
      },
      {
        "id": 2,
        "name": "잠실"
      }
    ]
  },
  {
    "id": 2,
    "name": "2호선",
    "color": "초록",
    "stations": [
      {
        "id": 3,
        "name": "부산"
      },
      {
        "id": 4,
        "name": "서면"
      }
    ]
  }
]
```

- POST /lines/{id}/stations
    - 노선에 역을 추가한다.
    - RequestBody

```json
{
  "targetStationId": 1,
  "addStationId": 2,
  "distance": 3,
  "direction": "UP"
}
```

- Response Status OK

- DELETE /lines/{line-id}/stations/{station-id}
    - 노선의 역을 삭제한다.
    - Response Status OK

- GET /fee?start={id}&end={id}
- Response OK

```json
{
  "fee": 1250,
  "stations": [
    {
      "id": 1,
      "name": "수원"
    },
    {
      "id": 2,
      "name": "잠실나루"
    }
  ]
}

```
