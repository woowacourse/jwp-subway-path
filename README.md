# jwp-subway-path

# 🤔다시 만난 페어

<table>
    <tr>
        <td align="center"><img src="https://avatars.githubusercontent.com/u/79090478?v=4" width=500></td>
        <td align="center"><img src="https://avatars.githubusercontent.com/u/82203978?v=4" width=500></td>
    </tr>
    <tr>
        <td align="center"><a href="">루카</a></td>
        <td align="center"><a href="">헤나</a></td>
    </tr>
</table>

# 시나리오

- 역 등록
    - 기존 노선이 없을 경우
        - A, B와 새로운 노선을 함께 등록한다.
    - 기존 노선이 있을 경우
        - 새로운 역 C를 등록할 경우
            - C가 기존 노선일 경우
                - 등록한다.
            - C가 기존 노선이 아닐 경우
                - C의 노선이 없을 경우
                    - 새로운 노선을 만들고, 등록한다.
                - C의 노선이 있을 경우
                    - 등록한다.


# 요구사항 분석
> `지하철`은 `노선`으로 이루어져 있다.
> 
> `지하철`이란, `역`과 `역`이 이어진 `구간`으로 되어있다.
> 
> `구간`이 하나 이상일 때 `노선`이라고 부른다.
> 
> `구간`은 `상행`과 `하행`이 존재한다.
> 
> `구간`은 `길이`를 갖는다.
> 
> 각 `노선`은 `상행 종점`과 `하행 종점`을 갖는다.


# API (적용 X, 확정 X)
## `station` 관련
### GET /stations/{stationId}
- 요청
```http request
GET /stations/1
```
- 응답
```http request
HTTP /1.1 200 OK
Content-Type: application/json

{
  "id" : "1",
  "name" : "잠실",
  "lines" : [
    {
      "lineName" : "2",
      "upStationName" : "잠실나루",
      "downStationName" : "잠실새내",
    },
    {
      "lineName" : "8",
      "upStationName" : "몽촌토성",
      "downStationName" : "석촌",
    }
  ]
}
```
### POST /stations
- 요청 
```http request 
POST /stations
Content-Type: application/json

{
  "name" : "잠실",
  "postion" : [
    {
      "lineId" : 1 (2호선),
      "upStationId" : 1 (잠실나루),
      "downStationId" : 2 (잠실새내),
      "distance" : 5
    },
    {
      "lineId" : 2 (8호선),
      "upStationId" : 3 (몽촌토성),
      "downStationId" : 4 (석촌),
      "distance" : 5
    }
  ]
}
```
- 응답
```http request
HTTP /1.1 201 CREATED
```
### PUT /stations/{stationId}
- 요청
```http request 
POST /stations
Content-Type: application/json

{
  "name" : "잠실",
  "postion" : [
    {
      "lineId" : 1 (2호선),
      "upStationId" : 1 (잠실나루),
      "downStationId" : 2 (잠실새내),
    },
    {
      "lineId" : 2 (8호선),
      "upStationId" : 3 (몽촌토성),
      "downStationId" : 4 (석촌),
    }
  ]
}
```
- 응답
```http request
HTTP /1.1 201 CREATED
```
### DELETE /stations/{stationId}
- 요청
```http request 
DELETE /stations/1
```
- 응답
```http request
HTTP /1.1 204 NO CONTENT
```

## `line` 관련
### GET /lines/{lineId}/stations
- 요청
```http request 
GET /lines/1
```
- 응답
```http request
HTTP /1.1 200 OK

{
  "lineId" : 1,
  "lineName" : "2호선",
  "lineColor" : "초록"
  "stations" : [
    {
      "stationId" : 1,
      "stationName" : "잠실새내"
    },
    {
      "stationId" : 5,
      "stationName" : "잠실"
    },
    {
      "stationId" : 2,
      "stationName" : "잠실나루"
    },
  ]
}
```
### GET /lines/stations
- 요청
```http request 
GET /lines
```
- 응답
```http request
HTTP /1.1 200 OK

{[
    {
        "lineId" : 1,
        "lineName" : "2호선",
        "lineColor" : "초록",
        "stations" : [
            {
                "stationId" : 1,
                "stationName" : "잠실새내"
            },
            {
                "stationId" : 5,
                "stationName" : "잠실"
            },
            {
                "stationId" : 2,
                "stationName" : "잠실나루"
            }
        ]
    },
    {
        "lineId" : 2,
        "lineName" : "8호선",
        "lineColor" : "고동",
        "stations" : [
            {
                "stationId" : 3,
                "stationName" : "몽촌토성"
            },
            {
                "stationId" : 5,
                "stationName" : "잠실"
            },
            {
                "stationId" : 4,
                "stationName" : "석촌"
            }
        ]
    }
]}
```
### POST /lines

- 요청
```http request 
POST /lines
Content-Type: application/json

{
  "lineName" : "4호선",
  "lineColor" : "파랑"
}
```
- 응답
```http request
HTTP /1.1 201 CREATED
```
### PUT /lines/{lineId}

- 요청
```http request 
PUT /lines/3
Content-Type: application/json

{
  "lineName" : "4호선",
  "lineColor" : "하늘"
}
```
- 응답
```http request
HTTP /1.1 201 CREATED
```
### DELETE /lines/{lineId}

- 요청
```http request 
DELETE /lines
```
- 응답
```http request
HTTP /1.1 204 NO CONTENT
```

