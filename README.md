# jwp-subway-path

# API 설계

## 노선 조회

```http request
GET "/lines/{lineId}/stations"

응답
HTTP Status : 200 OK
[
    {
        "id": 1,
        "name": "강남"
    },
    {
        "id": 2,
        "name": "선릉"
    }
    ...
]
```

## 노선 목록 조회

```http request
GET "/lines"

응답
HTTP Status : 200 OK
[
    {
        "id": 1,
        "name": "1호선",
        "color": "red"
    },
    {
        "id": 2,
        "name": "2호선",
        "color": "blue"
    }
    ...
]
```

## 노선에 역 추가

```http request
POST "/lines/{lineId}"

요청
{
    "upStationId" : 1,
    "downStationId" : 2,
    "distance" : 11
}

응답
HTTP Status: 201 
location: "/lines/{lineId}"
body:
{
    "id" : 1,
    "lineId" : 1,
    "upStationId" : 1,
    "downStationId" : 2,
    "distance" : 11
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
CREATE TABLE IF NOT EXISTS STATION
(
  id   BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS LINE
(
  id    BIGINT AUTO_INCREMENT NOT NULL,
  name  VARCHAR(255)          NOT NULL UNIQUE,
  color VARCHAR(255)          NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS SECTION
(
  id         BIGINT AUTO_INCREMENT NOT NULL,
  line_id    BIGINT,
  up_station_id BIGINT,
  down_station_id BIGINT,
  distance INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (line_id) REFERENCES LINE (id) ON DELETE CASCADE,
  FOREIGN KEY (up_station_id) REFERENCES STATION (id) ON DELETE CASCADE,
  FOREIGN KEY (down_station_id) REFERENCES STATION (id) ON DELETE CASCADE
);
```