# jwp-shopping-cart

# 📚 도메인 모델 네이밍 사전

| 한글명 | 영문명      | 설명                 | 분류    |
|-----|----------|--------------------|-------|
| 역   | Station  | 지하철 역              | class |
| 노선  | Line     | 구간의 모음             | class |
| 거리  | Distance | 역 간 거리 또는 경로의 총 거리 | class |
| 요금  | Charge   | 특정 경로에 대한 운임 요금    | class |

<br>

## DB(DAO)

- H2 데이터베이스를 사용한다.
- DB 테이블 설계
- 테이블 이름 - STATION

  | id | name | next | distance | line_id |
      | --- | --- |------|----------| -------- |
  | 1L | 노포역 | 2L | 10 | 1L |
  | 2L | 화정역 | 3L | 5 | 1L |
  | 3L | 잠실역 | 0L | null | 1L |
  | 4L | 화정역 | 5L | 8 | 2L |

name next가 중복 허용

- 테이블 이름 - LINE

  | id | name | color | head_station |
    | --- | --- |-------| ------------ |
  | 1L | 1호선 | 파란색 | 1L |
  | 2L | 2호선 | 초록색 | 4L |

# 👨‍🍳 기능 목록

### 역(Station) 관련 서비스

- [x]  노선에 역 등록
    - [x] 노선은 종점의 정보를 가진다
    - [x]  A-B-C가 있을 때 A의 앞, C의 뒤, A와 B 사이에도 추가할 수 있다.
    - [x]  노선은 역 간 거리 정보를 포함한다.
    - [x]  역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.
    - [x] 입력되는 상행역과 하행역이 노선에 모두 존재하지 않을 경우 예외처리
    - [x] 입력되는 상행역과 하행역 모두 역이 노선에 이미 존재할 경우 예외처리
    - [x]  거리는 양의 정수이다.
    - [x] 테스트코드 작성

  ```jsx
  POST /lines/{line_id}/stations
  
  RequestBody = {
      upStation : "노포역",
      downStation : "강남역",
      distance : 10,
  }
   
  ```

  ```jsx
  ResponseBody = {
    upStationId : 1,
    downStationId : 2,
  }
  ```

- [x] 역 목록 조회
    - [x] 테스트코드 작성

```jsx
GET /lines/{line_id}/stations
```

```jsx
ResponseBody = [
        {
                id : 1,
                name : "노포역",
                
        },{
                id : 2,
                name : "범어사역",
                
        },{
                id : 3,
                name:"남산역",
        }
]
```

- [x] 노선의 역 제거
    - [x]  A-B-C에서 B를 제거할 경우 A-C의 연결이 남는다
    - [x]  역이 제거될 때 역과 역 사이의 거리도 재배정되어야 한다.
    - [x]  노선에 역이 2개인 경우 하나를 제거하면 둘 다 제거되어야 한다.
    - [x] 테스트코드 작성

```jsx
DELETE /lines/{line_id}/stations

RequestBody = {
    name : "역삼역"
}
```

```jsx
ResponseBody = {
    id : 2,
} 
```

### 노선(Line) 관련 서비스

- [x]  노선 목록 조회
    - [x] 테스트코드 작성

```jsx
GET /lines
```

```jsx
ResponseBody = [
        {
                id : 1,
                name : "동해선",
                color : "파란색",
        },{
                id : 2,
                name : "수인분당선",
                color : "노란색",
                
        },{
                id : 3,
                name : "우이신설선",
                color : "연두색",
        }
]
```

- [x]  노선 조회
    - [x] 테스트코드 작성

```jsx
GET /lines/{lineId}
```

```jsx
ResponseBody = {
    id : 1,
    name : "동해선",
    color : "파란색",
}
```

- [x]  노선 추가
    - [x] 처음엔 역 2개가 등록되어야 한다.(처음 추가되는 역은 다음 역을 가지고 있어야 한다)
    - [x] 연결된 두 역이 같은 이름이면 예외처리
    - [x] 같은 이름의 노선이 2개 이상 존재하면 예외 처리.
    - [x] 테스트코드 작성

  ```jsx
  POST /lines
  RequestBody = {
      name:"1호선",
      color:"주황색",
      upStation:"강남역",
      downStation:"역삼역",
      distance:10,
  }
  ```

  ```jsx
  ResponseBody = {
    id : 1,
  }
  ```

- [x] 노선 제거
    - [x] 이름으로 노선을 삭제
    - [x] 테스트코드 작성

```jsx
DELETE /lines/{lineId}
```

```jsx
noContent
```

## 2단계 구현

- [x] 경로 조회 API 구현
    - [x] 출발 역과 도착 역 사이의 최단 거리 경로와 그 거리를 구한다.
    - [x] 존재하지 않는 역의 정보가 입력된다면 예외처리한다.
    - [x] 출발역과 도착역의 이름이 같을 경우 예외처리 한다.
        - 한 노선에서 경로찾기 뿐만 아니라 여러 노선의 환승도 고려해야 한다.
    - [x] 요금 계산 규칙에 따라 요금을 계산하고 경로 조회 API에서 함께 반환한다.
    - [x] (3단계) 연령에 따른 할인 제도 적용
    - [x] (3단계) 노선에 따른 할증 제도 적용
    - [x] 테스트코드 작성
  ```jsx
  GET /path
  
  RequestBody = {
    startStation: "성수역",
    endStation: "건대입구역"
  }
  ```
  ```jsx
  ResponseBody = {
    path: ["성수역", "뚝섬역", "잠실역", "건대입구역"],
    distance: 26,
    charge: 1650,
  }
  ```
- [x] 데이터베이스 설정을 프로덕션과 테스트를 다르게 한다.

# 📌 Commit Convention

커밋 메시지는 다음과 같은 형태로 작성합니다.

```Bash
> "커밋의 타입: 커밋 메세지 내용"
ex) "docs: 기능 목록 추가"
``` 

커밋의 타입은 아래 10가지 중 가장 적절한 것을 선택해 작성합니다.

|  커밋의 타입  |              설명               |
|:--------:|:-----------------------------:|
|   feat   |           새로운 기능 추가           |
|   fix    |             버그 수정             |
|   test   |           테스트 코드 추가           |
|   docs   | 문서를 추가 혹은 수정 (ex. README 수정)  |
|  chore   |   빌드 태스크 업데이트, 패키지 매니저를 설정    |
| refactor |            코드 리팩토링            |
|  style   | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |

- 상세한 컨벤션
  내용은 [Angular JS Git Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)
  를 참고

# 📌 Code Convention

- [우아한 테크코스 Java 코딩 컨벤션](https://github.com/woowacourse/woowacourse-docs/tree/main/styleguide/java)을
  준수합니다.
- IntelliJ의 Formatter를 적용합니다.





