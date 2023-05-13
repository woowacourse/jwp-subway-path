# jwp-subway-path

## 도메인

- Path
    - id, 상행역, 하행역, 거리를 가지는 경로 객체

- Line
    - id, 이름, 색, Map<Station, Path> 컬렉션을 가짐

- Station
    - id, 이름을 가짐

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
