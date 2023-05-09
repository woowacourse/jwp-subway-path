# 📄요구사항

## 💾테이블 설계

### ✔ 역 (STATION)

| 컬럼명 | 데이터 타입 | 속성 |
| --- | --- | --- |
| ID | BIGINT | PK, AI |
| NAME | VARCHAR(16) | NN |
| SRM_PK | BIGINT | FK |

### ✔ 역-노선 매핑(STATION_ROUTE_MAPPING)

| 컬럼명 | 데이터 타입 | 속성 |
| --- | --- | --- |
| ID | BIGINT | PK,AI |
| STATION_ID | BIGINT | FK |
| ROUTE_ID | BIGINT | FK |

### ✔ 노선(ROUTE)

| 컬럼명 | 데이터 타입 | 속성 |
| --- | --- | --- |
| ID | BIGINT | PK, AI |
| NAME | VARCHAR(16) | NN |
| COLOR | VARCHAR(32) | NN |

## 🗣️API

### ✔ **역 API**

| 기능 | Method | URL | Body |
| --- | --- | --- | --- |
| 생성 | POST | /stations | name, position |
| 단일 - 조회 | GET | /stations/{id} |  |
| 전체 - 조회 | GET | /stations |  |
| 수정 | PATCH | /stations/{id} | name |
| 삭제 | DELETE | /stations/{id} |  |

### ✔ 노선 API

| 기능 | Method | URL | Body |
| --- | --- | --- | --- |
| 생성 | POST | /routes | name, color |
| 단일 - 조회 | GET | /routes/{id} |  |
| 전체 - 조회 | GET | /routes |  |
| 수정 | PATCH | /routes/{id} | name, color |
| 삭제 | DELETE | /routes/{id} |  |

## 🚇도메인

### ✔ 역

- 역은 이름과 여러 노선 정보를 가진다.
- 새로운 역을 추가할 수 있다.
    - 노선 정보도 같이 받는다.
    - 역을 노선에 추가할 때는 기준이 있어야 한다.
        - 예시) B-C 사이, B 뒤, C 앞
    - 역과 역 사이의 거리는 양의 정수
- 기존 역을 삭제할 수 있다.
    - 이때, 앞 뒤 역을 연결해야한다.
- 역을 최초로 노선에 등록할 때는 2개를 입력받아야한다.
    - 거리 정보도 같이 받는다.
- 역을 제거할 때 만약 남은 역이 2개이면 제거할 수 없다.
    - 만약에 제거해야한다면 다 제거해야한다.

### ✔ 노선

- 이름과 색상을 가진다.
