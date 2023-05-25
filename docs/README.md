# 1단계 리팩토링 목록

## 리뷰 내용
- [ ] 기존에 Line 객체가 가지고 있던 color 필드는 없어져도 되는가? 
- [x] `Line.addSection()` 메서드 분리
- [x] `LineRequest`의 불필요한 기본 생성자 제거
- [x] Response 객체에서의 `to`, `from` 사용 기준이 있는가?
- [x] 일관성 있게 커스텀 예외 사용하도록 수정
- [x] ControllerAdvice의 크기가 너무 커지지 않도록 부모 예외 설정
- [x] LineController의 API URL 설계 시, PathVariable 변수명을 Id -> lineId로 수정
- [x] station, section 테이블 참조 관계에 대한 제약 추가
- [x] 서비스에서는 도메인만 다룰 수 있도록 수정
- [x] 실패하는 테스트 복구
- [x] LineService 생성자 public?? 
- [x] `created_at`, `updated_at` 데이터 생성

## 미완성 기능 구현
- [x] 조회 기능: API 레벨부터 천천히 살펴보기
  - [x] 조회 응답 Dto 다시 만들기
    - String lineName
    - List<StationResponse> stations
- [x] 노선에서의 역 제거 API 구현
- [x] 테스트 작성
- [x] 도메인 객체에 id 추가 -> equals,hashcode 다시 정의하기
  - [x] Line
  - [x] Station

## 궁금한 내용
- 프론트에서는 조회/삽입/수정/삭제 등의 요청을 날릴 때 서버가 넘겨준 정보(id)를 기반으로 요청할 것이다. 그렇다면, 조회 요청에 대한 응답으로는 항상 pk를 넘겨줘야 되는 것일까?
- 도메인 객체가 id를 갖도록 했다. 그럴지 않으면 조회 작업이 너무 까다로워진다는 것을 확인함. 

## 리팩토링
- [x] 조건에 따라 조회하는 DAO 메서드는 Optional을 반환하도록 수정
- [x] 테스트 Fixture 생성

# 2단계

## 1단계 리뷰 내용
- [ ] 이번 미션에서 엔티티를 사용한 이유는?
- [ ] id라는 값이 꼭 DB에 한정된 값일까? DB 연동을 하지 않더라도 id 값을 가지게 만들 수 있을텐데, 이때는 왜 id를 사용하는 것일까?
- [ ] 현재와 같이 정보를 가져간다면, 역 이름이 달라지면 다른 역이 되는 걸까?
- [x] DuplicatedNameException 핸들러 제거 + SubwayException 추상화 계층 추가
- [x] Section의 isAnySame() 메서드가 Station의 equals를 통해 비교하도록 수정

## 2단계 요구사항

### 데이터베이스 설정
- [x] 데이터베이스 설정을 프로덕션과 테스트를 다르게 합니다.
  - 프로덕션의 데이터베이스는 로컬에 저장될 수 있도록 설정
  - 테스트용 데이터베이스는 인메모리로 동작할 수 있도록 설정

### 기능
- [x] 경로 조회 API 구현
  - 출발역과 도착역 사이의 최단 거리 경로를 구하는 API를 구현합니다.
  - 최단 거리 경로와 함께 총 거리 정보를 함께 응답합니다.
  - 한 노선에서 경로 찾기 뿐만 아니라 여러 노선의 환승도 고려합니다.
- [x] 요금 조회 기능 추가
  - 경로 조회 시 요금 정보를 포함하여 응답합니다.

### 요금 계산 방법
- 기본운임(10㎞ 이내): 기본운임 1,250원
- 이용 거리 초과 시 추가운임 부과
  - 10km~50km: 5km 까지 마다 100원 추가
  - 50km 초과: 8km 까지 마다 100원 추가

9km = 1250원
12km = 10km + 2km = 1350원
16km = 10km + 6km = 1450원
58km = 10km + 40km + 8km = 2150원

### 궁금한 내용
- Docker에서 포트포워딩을 하는 것으로 알고 있는데, 커넥션을 생성할 때의 url에는 localhost:13306으로 설정해야 하는 이유?
- application.yml에 상용 DB의 정보만 설정했음에도 테스트에서는 알아서 인메모리 DB를 띄우는 원리
- DataSource.setDriverClassName를 할 때, environment.getProperty("spring.datasource.driver-class-name")이 안 되는 이유
- ResponseDto의 필드에 null이 들어가는 경우에도 Jackson이 역직렬화를 할 수 없다는 에러 메시지를 띄워줬다.

### 리팩토링 해야 되는 내용
- distance 자료형 double로 변경
- NegativeException -> NotPositiveException으로 변경

### 2단계 리뷰내용
- [x] 수동 빈 등록의 이점은?
  1. 객체간의 의존관계를 명확하게 확인할 수 있다.
  2. 빈을 자동으로 등록할 수 없는 외부 라이브러리를 등록할 때 사용할 수 있다.
  3. @Conditional을 통해 조건부 빈 등록이 가능하다.
  - 하지만 이번 미션에서는 딱히 수동으로 빈을 등록할 이유가 없었던 것 같다.
- [x] DataSoruce는 application.yml을 통해 자동으로 설정될 텐데, 추가적으로 구현한 이유는?
  - 상용 DB와 테스트용 DB 환경을 분리하는 과정에서 직접 구현했는데, 사실 왜 그랬는지 기억이 안 난다... 디버깅 과정에서 만들었던 것 같다.
- [ ] SubwayGraph를 컴포넌트로 등록하는 것은 어떨까?
- [ ] StationRequest의 name으로 빈 갑싱 들어온다면?
- [ ] Line/Section/StationException을 따로 핸들링한 이유는?
- 
