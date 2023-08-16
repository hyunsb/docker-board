# 원티드 프리 온보딩 (Java / Spring Boot)
로그인/회원가입, 간단한 게시판 기능을 가지는 RESTful API 서버를 구현한다.

> **개발 인원** : 정현수 <br />
> **개발 기간** : 2023. 08. 10. 목 ~ 2023. 08. 14. 월 <br />
> **개발 환경** : Java11 + Spring Boot 2.7.14 + MySQL 8.0


## API 명세

### 🔷 회원가입
이메일과 패스워드를 입력받고 회원 테이블에 데이터를 추가합니다.
- `URI`: POST localhost:8080/user
- `REQUEST`: 
  - type: JSON
  - body:
    ```
    {
        "email": "1@123",
        "password": "12345678"
    }
    ```
- `RESPONSE`: 201 Created
<hr>

### 🔷 로그인
이메일과 패스워드를 입력받고 회원 데이터가 일치하는 지 확인 후 토큰을 발급합니다.
- `URI`: POST localhostL8080/signin
- `REQUEST`:
  - type: JSON
  - body:
    ```
    {
        "email": "1@123",
        "password": "12345678"
    }
    ```
- `RESPONSE`: 
  - status: 200 OK
  - header: Authorization: JWT
<hr>

### 🔷 게시글 등록
로그인 시 발급받은 토큰을 통해 인증을 수행하고 제목, 내용을 입력받아 게시글 테이블에 데이터를 저장합니다.
- `URI`: POST localhostL8080/board
- `REQUEST`:
  - type: JSON
  - header: Authorization: JWT
  - body:
    ```
    {
        "title": "title",
        "content": "content"
    }
    ```
- `RESPONSE`: 
  - status: 201 Created
<hr>

### 🔷 게시글 목록 조회
전체 게시글 목록을 페이징 정보와 함께 조회 합니다. 
- `URI`: GET localhostL8080/board
- `RESPONSE`: 
  - status: 200 OK
  - body
    ```json
    {
        "content": [
    				// 게시글 목록
    		],
        "pageable": {
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "offset": 0,
            "pageSize": 5,
            "pageNumber": 0,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalPages": 0,
        "totalElements": 0,
        "size": 5,
        "number": 0,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "first": true,
        "numberOfElements": 0,
        "empty": true
    }
    ```
<hr>

### 🔷 특정 게시글 조회
게시글 아이디에 해당하는 게시글 정보를 조회합니다.
- `URI`: POST localhostL8080/board/`{게시글ID}`
- `RESPONSE`: 
  - status: 200 OK
<hr>

### 🔷 특정 게시글 수정
게시글 아이디에 해당하는 게시글 정보를 수정합니다. 게시글 작성자만 수정할 수 있습니다.
- `URI`: POST localhostL8080/board/`{게시글ID}`
- `REQUEST`:
  - header: Authorization: JWT
- `RESPONSE`: 
  - status: 204 No Content
<hr>

### 🔷 특정 게시글 삭제
게시글 아이디에 해당하는 게시글 정보를 삭제합니다. 게시글 작성자만 삭제할 수 있습니다.
- `URI`: POST localhostL8080/board/`{게시글ID}`
- `REQUEST`:
  - header: Authorization: JWT
- `RESPONSE`: 
  - status: 204 No Content
<hr>
