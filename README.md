# jwp-subway-path

## API 설계
### 1. [x] 역 등록 API (post - "/stations")

request
```JSON
  {
    "stationName" : "잠실"
  }
```
response
```JSON
  {
  "stationId" : 1,
  "stationName" : "잠실"
  }
```

### 2. [x] 최초 노선 등록 API (post - "/lines")

request
```JSON
  {
    "lineName" : "2호선",
    "upStationId" : 1,
    "downStationId" : 2,
    "distance" : 10
   }
```
response
```JSON
  {
  "lineId" : 1,
  "lineName" : "2호선",
  "stationIds" : [1, 2, 3]
  }
```

### 3. [x] 노선에 역 등록 API (post - "/lines/{lineId}/stations")
request
```JSON
  {
    "upStationId" : 3,
    "downStationId" : 1,
    "distance" : 5
  }
```
response
```JSON
  {
  "lineId" : 1,
  "lineName" : "2호선",
  "stationIds" : [1, 2, 3]
  }
```

### 4. [x] 노선의 모든 역 조회 API (get - "/lines/{lineId}")
response
```JSON
{
  "lineId" : 1,
  "lineName": "2호선",
  "stations" : [
    {
      "stationId" : 1,
      "stationName" : "경복궁"
    },
    {
      "stationId" : 2,
      "stationName" : "안국"
    }
  ]
}
```
### 5. [x] 모든 노선의 모든 역 조회 API (get - "/lines")
response
```JSON
[
  {
    "lineId" : 1,
    "lineName": "2호선",
    "stations" : [
      {
        "stationId" : 1,
        "stationName" : "잠실"
      },
      {
        "stationId" : 2,
        "stationName" : "잠실새내"
      }
    ]
  },
  {
    "lineId" : 2,
    "lineName": "3호선",
    "stations" : [
      {
        "stationId" : 3,
        "stationName" : "경복궁"
      },
      {
        "stationId" : 4,
        "stationName" : "안국"
      }
    ]
  }
]
```

### 6. [x] 노선에 역 제거 API (delete - "/lines/{lineId}/stations/{stationId}")
response
```JSON
  {
  "lineId" : 1,
  "lineName" : "2호선",
  "stationIds" : [1, 2, 3]
  }
```

### 7. [x] 경로 조회 API
request
```JSON
{
  "passengerAge": 15,
  "startStationId" : 1,
  "endStationId" : 4
}
```
response
```JSON
{
  "totalDistance" : 10,
  "totalCharge" : 1400,
  "path": [
    {
      "lineId" : 1,
      "lineName": "2호선",
      "stations" : [
        {
          "stationId" : 1,
          "stationName" : "a"
        },
        {
          "stationId" : 2,
          "stationName" : "b"
        },
        {
          "stationId" : 3,
          "stationName" : "c"
        }
      ]
    },
    {
      "lineId" : 2,
      "lineName": "3호선",
      "stations" : [
        {
          "stationId" : 3,
          "stationName" : "c"
        },
        {
          "stationId" : 4,
          "stationName" : "d"
        }
      ]
    }
  ]
}
```
