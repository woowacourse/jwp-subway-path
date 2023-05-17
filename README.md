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

## 기능 요구 사항

### 역

- 역을 등록할 수 있다.
- 역을 제거할 수 있다.

### 노선과 경로

- 노선은 하나의 직선이다.(순환하지 않는다.)
- 노선을 조회할 수 있다.
    - 노선 조회 시 역을 순서대로 조회한다.
- 모든 노선을 조회할 수 있다.
    - 노선 조회 시 역을 순서대로 조회한다.
- 노선을 등록할 수 있다.
- 노선을 제거할 수 있다.
    - 노선 제거 시에 노선의 모든 경로가 제거 된다.
- 노선에 역(경로)를 등록할 수 있다.
    - 기준역, 추가할 역, 거리, 추가할 역의 방향을 고려한다.
    - 역들의 사이에 추가될 경우 등록할 경로의 거리가 기존 거리보다 짧아야 한다.
    - 노선에 역이 없다면 최초 등록 시 두개의 역이 경로로 등록된다.
- 노선의 역(경로)를 제거할 수 있다.
    - 중간에 등록된 역이 제거된다면 제거된 역의 양 쪽의 역의 경로 거리가 합산된다.
    - 노선에 역이 두개 있다면 한개를 제거할 때 두 역이 모두 제거된다.
    - 역이 제거될 때 노선에 등록된 역도 제거된다.

### 최단 경로 조회

- 두개 역 사이의 가장 짧은 거리를 계산한다.
    - 경로와 거리, 비용도 같이 계산한다.
    - 비용 정책 변경을 고려한다.

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
