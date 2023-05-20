# jwp-subway-path

## 기능 목록

### API 기능 요구사항

- [x] 노선에 역 등록 API 신규 구현
- [x] 노선에 역 제거 API 신규 구현
- [x] 노선 조회 API 수정
    - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.
- [x] 노선 목록 조회 API 수정
    - [x] 노선에 포함된 역을 순서대로 보여주도록 응답을 개선합니다.

### 비즈니스 규칙

- [x] 노선에 역을 등록할 수 있다
    - [x] 노선에 등록되는 역의 위치는 자유롭게 지정할 수 있어야 합니다.
    - [x] 노선에 역이 등록될 때 거리 정보도 함께 포함되어야 합니다.
        - [x] 거리 정보는 양의 정수로 제한합니다.
    - [x] 노선에 역이 하나도 등록되지 않은 상황에서 최초 등록 시 두 역을 동시에 등록해야 합니다.
    - [x] 하나의 역은 여러 노선에 등록이 될 수 있습니다.
    - [x] 노선은 갈래길을 가질 수 없습니다.
    - [x] 노선 가운데 역이 등록 될 경우 거리 정보를 고려해야 합니다.
        - [x] 노선 가운데 역이 등록 될 경우 거리는 양의 정수라는 비즈니스 규칙을 지켜야 합니다.
- [x] 노선에 역을 제거할 수 있다
    - [x] 노선에서 역을 제거할 경우 정상 동작을 위해 재배치 되어야 합니다.
    - [x] 노선에서 역이 제거될 경우 역과 역 사이의 거리도 재배정되어야 합니다.
    - [x] 노선에 등록된 역이 2개 인 경우 하나의 역을 제거할 때 두 역이 모두 제거되어야 합니다.

- [x] 데이터베이스 설정을 프로덕션과 테스트를 다르게 합니다.
    - [x] 프로덕션의 데이터베이스는 로컬에 저장될 수 있도록 설정
    - [x] 테스트용 데이터베이스는 인메모리로 동작할 수 있도록 설정

- [x] 경로 조회 API 구현
    - [x] 출발역과 도착역 사이의 최단 거리 경로를 구하는 API를 구현합니다.
    - [x] 최단 거리 경로와 함께 총 거리 정보를 함께 응답합니다.
    - [x] 한 노선에서 경로 찾기 뿐만 아니라 여러 노선의 환승도 고려합니다.

- [x] 요금 조회 기능 추가
    - [x] 경로 조회 시 요금 정보를 포함하여 응답합니다.
    - [x] 기본운임(10㎞ 이내): 기본운임 1,250원
    - [x] 이용 거리 초과 시 추가운임 부과
    - [x] 10km~50km: 5km 까지 마다 100원 추가
    - [x] 50km 초과: 8km 까지 마다 100원 추가

## 데이터베이스 환경 설정

### Docker 사용 방법

1. docker directory를 생성한다.

2. 생성한 directory 하위에 docker-compose.yml 파일 생성

```
version: "3.9"
services:
  db:
    image: mysql:8.0.33
    platform: linux/amd64
    restart: always
    ports:
      - "13306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: subway
      MYSQL_USER: user
      MYSQL_PASSWORD: password
      TZ: Asia/Seoul
```

3. docker-compose.yml 파일이 있는 경로에서, docker 명령어로 Server를 실행

```
# Docker 실행하기
docker-compose -p chess up -d
```

```
# Docker 정지하기
docker-compose -p chess down
```

### Local MYSQL 사용 방법

1. MYSQL WorkBench를 설치하고 실행한다.

2. 다음과 같이 연결 정보를 입력한다.

```
Hostname : localhost
Port : 13306
Username : root
```

3. 새로운 유저를 생성한다.

```
create user 'username'@'localhost' identified by 'password';
```

4. 생성한 유저에게 모든 db 및 테이블에 접근권한 부여

```
grant all privileges on *.* to 'username'@'localhost';
```

5. 설정한 권한 적용

```
flush privileges;
```

## 데이터베이스 생성 쿼리

1. 데이터베이스 `subway`를 만듭니다.

```
CREATE DATABASE subway DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
```

2. 테이블은 애플리케이션을 실행시키면 자동으로 생성됩니다.

## 👏👏👏 모든 설정을 완료했습니다!! 👏👏👏
