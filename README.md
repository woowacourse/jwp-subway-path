# jwp-subway-path

## 기능 구현 목록

노선에 역 등록

- [x] 노선에 역을 등록할 때 두 개의 역, 거리, 노선 이름을 저장한다.
    - [x] 노선에 역을 등록할 수 있다.
    - [x] 노선에 등록할 두 개의 역이 모두 중복된 이름이면 예외가 발생한다.
    - [x] 이름이 공백이면 예외가 발생한다.
    - [x] 등록 시 출발역과 도착역이 같으면 예외가 발생한다.
    - [x] 거리가 0보다 작거나 같으면 예외가 발생한다.
    - [x] 노선에 역을 등록할 때 어디든 등록할 수 있다.
        - [x] 기존 역과의 사이의 거리보다 크거나 같으면 예외가 발생한다.
    - [x] 하나의 역은 여러 노선에 등록이 될 수 있다.

노선에 역 제거

- [x] 노선에 역을 삭제할 때 노선과 역으로 삭제할 수 있다.
    - [x] 삭제할 역이 노선에 없으면 예외가 발생한다.
    - [x] 노선에 역이 2개만 있으면 노선 전체를 삭제한다.
- [x] 노선에 역을 삭제하면 거리가 역 사이의 거리가 삭제된 거리만큼 더해진다.
- [x] 노선에서 역을 제거할 경우 정상 재배치 되어야 한다.
    - A-B-C-D 역이 있는 노선에서 C역이 제거되는 경우 A-B-D 순으로 재배치됩니다.

노선 조회 API 수정

- [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.

Section

- Source Station
- Target station
- 거리

Station

- 이름

Line

- 이름
- Section 리스트

## API 설계

### Line 등록

요청 `POST("/lines")`

```json
{
  "lineName": "2호선",
  "sourceStation": "강남역",
  "destinationStation": "역삼역",
  "distance": 10
}
```

응답

```json
{
  "lineId": 1
}
```

### 전체 Line 조회

요청 `GET("/lines")`

응답

```json
[
  {
    "name": "1호선",
    "stations": [
      {
        "name": "서울역"
      },
      {
        "name": "시청역"
      }
    ]
  },
  {
    "name": "2호선",
    "stations": [
      {
        "name": "강남역"
      },
      {
        "name": "역삼역"
      }
    ]
  }
]
```

### Line 단건 조회

요청 ` GET("/lines/{id}")`

응답

```json
{
  "name": "2호선",
  "stations": [
    {
      "name": "강남역"
    },
    {
      "name": "역삼역"
    }
  ]
}
```

### Line 삭제

요청 `DELETE("/lines/{id}")`

응답 X

---

### Station 등록

요청 `POST("/lines/{lineId}/stations")`

```json
{
  "lineId": 1,
  "station": "강남역",
  "downStation": "역삼역",
  "distance": 10
}
```

응답 X

### Station 삭제

요청 `DELETE("/lines/{lineId}/stations")`

```json
{
  "stationName": "역삼역"
}
```

응답 X
