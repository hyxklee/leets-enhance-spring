## 미션 요구사항
- 회원가입, 로그인
- 아이템 강화
- 아이템 조회

## 기능 요구사항
### BE

POST /users/register - 회원가입

POST /users/login - 로그인

GET /users/check-duplicate-id - id 중복 확인

POST /items- 아이템 추가

POST /enhance - 강화하기

GET /enhance - 강화 결과 조회

GET /items - 내 아이템 조회

GET /items/top10 - 상위 10개 아이템 조회

위 API 명세서는 가이드이며, 원한다면 수정하거나, 추가해도 됩니다.

## 요구 페이지

---
- 회원가입 [이메일, 비밀번호 입력]
  - 이메일 중복 확인, 비밀번호 2차 확인
  - 이름 입력받기
- 로그인
  - 이메일, 비밀번호로 로그인 (이메일, 비밀번호 틀리면 각각 불일치 문구 표시)
  - 로그인 하면 강화 가능
  - 로그인 안하면 명예의 전당 조회만 가능
