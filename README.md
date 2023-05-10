# jwp-shopping-cart

# 📚 도메인 모델 네이밍 사전

| 한글명 | 영문명      | 설명     | 분류    |
|-----|----------|--------|-------|
| 역   | Station  | 지하철 역  | class |
| 노선  | Line     | 구간의 모음 | class |
| 거리  | Distance | 구간의 거리 | class |

<br>

## DB(DAO)

- H2 데이터베이스를 사용한다.
- DB 테이블 설계
- 테이블 이름 - STATION

  | id | name |
        | --- | --- |
  | 1L | 노포역 |
  | 2L | 화정역 |

- 테이블 이름 - LINE

  | id | name | color |
        | --- | --- | --- |
  | 1L | 1호선 | 파란색 |
  | 2L | 2호선 | 초록색 |

# 👨‍🍳 기능 목록

- [ ]  노선 추가
    - [x] 처음엔 역 2개가 등록되어야 한다.(처음 추가되는 역은 다음 역을 가지고 있어야 한다)
    - [x] 노선에 처음 추가되는 역은 비어있으면 안된다
    - [x] 연결된 두 역이 같은 이름이면 예외처리

  ```jsx
  POST /lines
  RequestBody = {
      name:"1호선",
      color:"주황색",
      upStation:"강남역",
      downStation:"역삼역",
  }
  ```

  ```jsx
  추가된 노선의 아이디 반환
  ```

- [ ]  노선에 역 등록
    - [x] 노선은 종점의 정보를 가진다
    - [x]  A-B-C가 있을 때 A의 앞, C의 뒤, A와 B 사이에도 추가할 수 있다.
    - [x]  노선은 역 간 거리 정보를 포함한다.
    - [x]  역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록할 수 없다.
    - [x] 입력되는 상행역과 하행역이 노선에 모두 존재하지 않을 경우 예외처리
    - [ ] 입력되는 상행역과 하행역 사이에 다른 역이 존재할 경우 예외처리
    - [ ] 입력되는 상행역과 하행역 모두 역이 노선에 이미 존재할 경우 예외처리
    - [x]  거리는 양의 정수이다.

  ```jsx
  POST /stations/{line_id}
  
  RequestBody = [{
      name : "노포역",
  },{
      name : "범어사역",
  }]
  +)TODO 어디에 추가할 건지 
  ```

  ```jsx
  추가된 역의 아이디 리스트
  ```

- [ ]  노선의 역 제거
    - [ ]  A-B-C에서 B를 제거할 경우 A-C의 연결이 남는다
    - [ ]  역이 제거될 때 역과 역 사이의 거리도 재배정되어야 한다.
    - [ ]  노선에 역이 2개인 경우 하나를 제거하면 둘 다 제거되어야 한다.

  ```jsx
  DELETE /stations/{line_id}
  
  RequestBody = {
      stationId : 2,
  }
  ```

  ```jsx
  삭제된 아이디 리스트
  ```

- [ ]  노선 조회

  ```jsx
  GET /lines/{line_id}
  ```

  ```jsx
  [
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

- [ ]  노선 목록 조회

  ```jsx
  GET /lines
  ```

  ```jsx
  [
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

repository => domain의 저장소

    - 도메인 : 비즈니스 로직을 수행
    - 한 개 이상의 dao를 호출
    Station findBy~~()
    void save(Station station)
    dao로부터 받은 엔티티 객체를 도메인 객체로 변환시키는 것은 repository의 역할

dao => entity

    - 엔티티 : DB의 데이터 하나에 대응되는 객체(테이블의 컬럼과 1:1로 매핑된다)
    StationEntity findBy~~()
    - DB에 직접 접근을 해서 데이터를 받아오거나 업데이트하는 역할
    dao 1개는 DB table 1개에 매핑된다





