# jwp-subway-path

## API

| Method | URI                    | Description        |
|--------|------------------------|--------------------|
| POST   | /stations              | 역을 신규 등록           |
| GET    | /stations              | 등록된 역을 전체 조회       |
 | GET    | /stations/{id}         | 특정 역을 조회           |
| POST   | /lines                 | 기존 역을 통해 신규 노선을 등록 |
| GET    | /lines                 | 등록된 노선을 전체 조회      |
| GET    | /lines/{id}            | 특정 노선을 조회          |
| DELETE | /lines/{id}            | 특정 노선을 삭제          |
 | PATCH  | /lines/{id}/register   | 특정 노선에 역 연결        |
 | PATCH  | /lines/{id}/unregister | 특정 노선에 역 제거        |
 | POST   | /path                  | 두 역 사이의 최단 경로 조회   |

[여기](./subway.http)에서 API 요청 형식을 확인할 수 있습니다.


## 구현 기능 목록
### 노선에 역 등록
- [x] 노선을 최초 등록할 때 두 역을 동시에 등록한다.
- [x] 노선에 이미 등록된 두 역 사이에 역을 추가하는 경우 연결이 재배치된다.
- [x] 노선에 이미 등록된 두 역 사이에 역을 추가하는 경우 역 간 거리가 조정된다.


- [x] 존재하지 않는 역을 노선에 등록할 수 없다.
- [x] 존재하지 않는 노선에 역을 등록할 수 없다.
- [x] 노선에 갈래길이 존재할 수 없다.
- [x] 역 간 거리는 언제나 앙의 정수여야 한다.


### 노선에 역 제거
- [x] 노선에서 역을 제거하는 경우 나머지 역의 연결이 재배치된다.
- [x] 노선의 마지막 두 역 중 하나를 제거하는 경우 노선도 삭제된다.


- [x] 노선에 등록되지 않은 역을 삭제할 수 없다.


### 경로 조회
- [x] 경로 조회 시 요금 정보를 포함하여 응답한다.
  - [x] 기본 운임은 1250원이며 10~50km에서는 5km마다 100원 추가, 50km 초과 시는 8km마다 100원이 추가된다.


- [x] 노선에 아직 연결되지 않은 역과의 경로를 조회할 수 없다.
- [x] 노선끼리 연결되지 않은 상황에서 다른 노선의 두 역 간의 경로를 조회할 수 없다.


## 프로그래밍 요구 사항
- 프로덕션 환경은 [로컬 h2](/src/test/resources/application.properties)에서 동작한다. 별도의 h2가 설치 및 실행된 상태에서 프로그램이 동작한다.
- 테스트 환경은 [인메모리 h2](/src/test/resources/application.properties)에서 동작한다.
