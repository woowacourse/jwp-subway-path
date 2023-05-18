# jwp-subway-path

### 1단계 리팩터링 목록

- [x] README 문서 업데이트
- [x] [`IllegalArgumentException`에 대한 처리 400대 VS 500대](https://github.com/woowacourse/jwp-subway-path/pull/35#discussion_r1193146265)
- [ ] domain Line이 Station과의 관계를 드러낼 수 있도록 수정하기 => 계층적 구조 드러내기
- [x] 도메인 내 주석 없애기 => 최대한 코드만으로 표현하자!
- [x] Sections 중복 코드 없애기
- [ ] MockMVC 사용한 테스트 코드 작성

### 2단계 요구사항

#### 데이터베이스 설정 변경

- [x] 프로덕션의 데이터베이스는 로컬에 저장될 수 있도록 설정
- [x] 테스트용 데이터베이스는 인메모리로 동작할 수 있도록 설정

#### 경로 조회 API 구현

- [ ] 출발역과 도착역 사이의 최단 거리 경로를 탐색
- [ ] 최단 거리 경로와 함께 총 거리 정보를 함께 응답
    - 여러 노선의 환승도 고려
- [ ] 경로 조회 시 요금 정보를 포함하여 응답

### 요금 조회 기능 추가

- [ ] 기본운임(10㎞ 이내): 기본운임 1,250원
    - [ ] 이용 거리 초과 시 추가운임 부과
    - [ ] 10km~50km: 5km 까지 마다 100원 추가
    - [ ] 50km 초과: 8km 까지 마다 100원 추가

# API 설계

| HttpMethod | URL                                  | HttpStatus | Description         |
|------------|--------------------------------------|------------|---------------------|
| GET        | /lines                               | 200        | 전체 노선 목록을 조회한다.     |
| GET        | /lines/{lineId}                      | 200        | 특정 노선을 조회한다.        |
| POST       | /lines                               | 201        | 노선을 생성한다.           |
| POST       | /lines/{lineId}/stations             | 201        | 특정 노선에 역을 추가한다.     |
| POST       | /lines/{lineId}/station-init         | 201        | 노선에 최초로 역 2개를 추가한다. |
| DELETE     | /lines/{lineId}/stations/{stationId} | 204        | 특정 노선에서 특정 역을 삭제한다. |
| POST       | /stations                            | 201        | 역을 생성한다.            |

## POST /lines

#### Request

BODY

```json
{
  "name": "2호선",
  "color": "GREEN"
}
```

#### Response

BODY

```json
{
  "id": 1,
  "name": "2호선",
  "color": "GREEN"
}
```

### POST /lines/{lineId}/stations

#### Request

Body

```json
{
  "upStation": 1,
  "downStation": 2,
  "distance": 15
}
```

#### Response

Body

```json
{
  "id": 1,
  "name": "2호선",
  "color": "GREEN",
  "stations": [
    {
      "id": 1,
      "name": "역삼역"
    },
    {
      "id": 2,
      "name": "삼성역"
    },
    {
      "id": 3,
      "name": "잠실역"
    }
  ]
}
```
