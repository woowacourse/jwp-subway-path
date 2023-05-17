# jwp-subway-path

# docker 실행

도커 컨테이서 실행 후, 프로젝트 루트 디렉토리에서 다음 명령어 실행

```text
cd docker
docker-compose -p subway up -d
```

# Step1

## API 기능 요구사항

- [x] 역 생성 API 구현
- [x] 노선에 역 등록 API 구현
- [x] 노선의 역 제거 API 구현
- [x] 새 노선 등록 API 구현
- [x] 노선 조회 API 구현
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.
- [x] 노선 목록 조회 API 구현
    - 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.

## API 문서

[API 문서](https://documenter.getpostman.com/view/19074157/2s93ebSqu4)

## 비즈니스 요구사항

### 노선에 역 등록

- [x] 노선에 등록되는 역의 위치를 지정할 수 있다.
    - 필요한 정보
        - 노선 아이디
        - 인접한 역의 아이디
        - 인접한 역의 아이디
        - 인접 역과의 거리
    - [x] 두 역 중간에 새로운 역을 등록시 역간의 거리 정보를 고려한다.
        - 거릭가 7인 A - B 에 역C를 등록시 A와의 거리는 7미만 양의정수여야 한다.

- [x] 하나의 역은 여러 노선에 등록이 될 수 있다.

- [x] 노선은 갈래길을 가질 수 없다.

## 노선의 역 제거

- [x] 노선에서 역을 제거할 경우 정상 동작을 위해 재배치 되어야 한다.
    - A-(2)-B-(3)-C 상황에서 B를 제거시 A-(5)-C 상태가 되어야 한다.

- [x] 역이 2개인 노선에서 역을 제거하면 노선이 사라진다.

# Step2

## DB 요구사항

- [x] profile을 이용해 test DB와 product DB 구분
    - [x] test: 인메모리 DB(H2)
    - [x] product: local DB (mySql(docker))

## API 기능 요구사항

- [x] 경로 조회 API 구현

## 비즈니스 요구사항

- [x] source와 target 간의 최소 경로, 거리를 구한다.
    - [x] source 와 target이 일치하면 예외를 발생시킨다.
    - [x] source 또는 target이 line에 등록되지 않은 역이면 예외를 발생한다.
    - [x] source와 target 간의 경로를 구하지 못하는 경우는 예외를 발생한다.
- [x] source 에서 target 최소경로 이동시 운임을 구한다.
    - [x] 10km 까지는 기본운임 1,250원이 부과된다.
    - [x] 11km ~ 50km 의 추가 운임은 5km 당 100원이다.
    - [x] 51km ~ 의 추가 운임은 8km 당 100원이다.
        - 예시
            - 8km -> 1,250
            - 11km -> 1,350
            - 58km -> 2,150

# Step3

- [ ] 노선 별 추가요금 적용
    - [ ] LINE 테이블 수정
    - [ ] 운임 계산시, 추가 요금 적용
- [ ] 연령별 할인 금액 적용
  - [ ] 경로계산 API 수정
  - [ ] 운임 계산 시 연령에 따른 할인 금액 적용
  - [ ] 청소년(13세 이상~19세 미만) : 운임에서 350원을 공제한 금액의 20%할인
  - [ ] 어린이(6세 이상~13세 미만0 : 운임에서 350원을 공제한 금액의 50%할인
    - 예시
      - 운임: 1,350
      - 청소년 : 800
        - (1,350 - 350) * 0.8
      - 어린이 : 500
        - (1,350 - 350) * 0.5
```sql
create table if not exists LINE
(
    id             bigint auto_increment not null,
    name           varchar(255)          not null unique,
    color          varchar(20)           not null,
    additionalFare int(20)               not null,
    primary key (id)
);
```
