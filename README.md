# API 설계

## 노선 조회

```http request
GET "/lines/{lineId}"

// List<StationResponse>
HTTP Status : 200 OK
[
    {
        "id": ?,
        "name": ?
    },
    {
        ...
    }
]
```

## 노선 목록 조회

```http request
GET "/lines"

// List<LineResponse>
HTTP Status : 200 OK
[
    {
        "id": ?,
        "name": ?,
        "color": ?
    },
    {
        ...
    }
]
```

## 노선에 초기 역 추가

```http request
POST "/lines/{lineId}"

// StationAddRequest (A - B)
{
    "previousStationId" : A,
    "nextStationId" : B,
    "distance" : 11
}
```

## 노선에 역 1개 추가

- 기존 노선에 추가
  - 중간
    lineId, 추가할 stationId, 이전 stationId, 다음 stationId, 이전 station 간의 거리, 다음 station 간의 거리
  - 끝
    lineId, 추가할 stationId, 이전 stationId, 이전 station 간의 거리

```http request
POST "/lines/{lineId}/stations"

// List<StationAddRequest> (A - C - B)
[
   {
        "previousStationId" : A,
        "nextStationId" : C,
        "distance" : 4
    },
    {
        "previousStationId" : C,
        "nextStationId" : B,
        "distance" : 7
    }
]

// response
{
HTTP Status : 201 CREATED
Location: "/lines/{lineId}"
}
```

## 노선에 역 제거 (모든 역 제거 포함)

```http request
DELETE "/lines/{lineId}/stations/{stationId}"

//response
HTTP Status : 204 NO Contentent
```

---

# DB TABLE 설계

```mysql
CREATE TABLE STATION
(
    id   BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE LINE
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    name  VARCHAR(255)          NOT NULL UNIQUE,
    color VARCHAR(255)          NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE SECTION
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    line_id    BIGINT,
    previous_station_id BIGINT,
    next_station_id BIGINT,
    distance INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE,
    FOREIGN KEY (previous_station_id) REFERENCES STATION (id) ON DELETE CASCADE,
    FOREIGN KEY (next_station_id) REFERENCES STATION (id) ON DELETE CASCADE
);
```