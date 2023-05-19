

### 역 생성 API

```http request
POST http://localhost:8080/stations
```

request body
```json
{
  "name": "잠실"
}
```

```http request
HHTP/1.1 201 Created
Location: /stations/{id}
```

### 노선의 역 제거 API

```http request
DELETE http://localhost:8080/lines/{lineId}/{stationId}
```

```http request
HHTP/1.1 204 No-content
```

### 노선 등록 API
```http request
POST http://localhost:8080/lines
```

```json
{
  "name": "2호선",
  "color": "#00A84D",
  "upStationId": 1,
  "downStationId": 2,
  "distance": 4
}
```

```http request
HHTP/1.1 201 Created
Location: /lines/{id}

```
### 노선에 역 등록 API

```http request
POST http://localhost:8080/lines/stations
```

request body

```json
{
  "stationId": 2,
  "lineId": 2,
  "adjacentStationId": 1,
  "direction": "DOWN",
  "distance": 3
}
```

### 노선 조회 API

```http request
GET http://localhost:8080/lines/{id}
```

response body
```json
{
  "id": 2,
  "name": "2호선",
  "color": "#00A84D",
  "stations": [
    {
      "id": 1,
      "name": "잠실"
    },
    ...
  ]
}
```

### 노선 목록 조회 API

```http request
GET http://localhost:8080/lines
```

```json
{
  "lines": [
    {
      "id": 2,
      "name": "2호선",
      "color": "#00A84D",
      "stations": [
        {
          "id": 1,
          "name": "잠실"
        },
        ...
      ]
    },
    ...
  ]
}
```

### 경로 조회 API

```http request
GET http://localhost:8080/path?departureStationId={DEPARTURE_STATION_ID}&arrivalStationId={ARRIVAL_STATION_ID}
```

#### 요청 예시

GET `http://localhost:8080/path?departureStationId=1&arrivalStationId=4`

#### 응답 예시

- 경로가 있는 경우
```json
{
  "departureStation": {
    "id": 1,
    "name": "잠실"
  },
  "arrivalStation": {
    "id": 4,
    "name": "혜화"
  },
  "totalDistance": 10, // 단위: km
  "fare": 1350, // 단위: 원(KRW)
  "doesPathExists": true,
  "path": [
    {
      "line": {
        "id": 1,
        "name": "2호선",
        "color": "#00A84D"
      },
      "stations": [
        {
          "id": 1,
          "name": "잠실"
        },
        {
          "id": 2,
          "name": "성수"
        },
        {
          "id": 3,
          "name": "동대문역사문화공원"
        }
      ]
    },
    {
      "line": {
        "id": 2,
        "name": "4호선",
        "color": "#00A5DE"
      },
      "stations": [
        {
          "id": 3,
          "name": "동대문역사문화공원"
        },
        {
          "id": 4,
          "name": "혜화"
        }
      ]
    }
  ]
}
```

- 경로가 없는 경우

```json
{
  "doesPathExists": false
}
```