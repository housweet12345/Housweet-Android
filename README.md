<img width="300" height="300" alt="Image" src="https://github.com/user-attachments/assets/49cf8534-e784-47b7-a1eb-05ab7b9aefba" /> 

# 하우스잇

[![Kotlin](https://img.shields.io/badge/kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-28%2B-brightgreen.svg)](https://android-arsenal.com/api?level=28)
[![Google Play](https://img.shields.io/badge/Google%20Play-Download-blue)](https://play.google.com/store/apps/details?id=com.housweet.app)

## 프로젝트 소개
하우스잇(Housweet)은 쉐어하우스 인원 모집을 도와주고 쉐어 하우스 생활을 보조해주는 앱입니다!
<br><br/>

## ✨ 주요 기능

### 🙍 자신의 프로필 관리

- **프로필 관리**: 개인 정보 및 키워드 설정

### 👥 커뮤니티 및 검색

- **지역별 공고 검색**: 네이버 지도를 활용한 지역별 공고 검색
- **공고 등록**: 4단계의 공고 등록 프로세스
- **북마크**: 관심 있는 공고 저장

### 💬 채팅 및 소통

- **실시간 채팅**: Socket.IO를 통한 실시간 채팅

### 🏠 방 관리

- **방 생성**: 새로운 방 만들기
- **초대 코드**: 방 가입를 위한 초대 코드 시스템
- **기분 공유**: 룸메이트 기분 공유
  <br><br/>

## 🛠 기술 스택

### 개발 환경

- **언어**: Kotlin
- **SDK**: Android API 28+ (Target: API 35)
- **Gradle**: 8.5.2
- **IDE**: Android Studio

### 아키텍처

- **Clean Architecture**: App, Domain, Data, Presentation Layer
- **MVVM 패턴**

### 주요 라이브러리

| 카테고리        | 라이브러리         | 용도                       |
| --------------- | ------------------ | -------------------------- |
| **UI**          | Jetpack Compose    | 선언적 UI 개발             |
|                 | Lottie             | 스플래시 스크린 애니메이션 |
|                 | Coil               | 이미지 로딩                |
| **네트워킹**    | Ktor               | 네트워크 통신              |
|                 | Socket.IO          | 실시간 채팅                |
| **데이터**      | DataStore          | JWT 토큰 저장              |
| **DI**          | Hilt               | 의존성 주입                |
| **비동기**      | Coroutines         | 비동기 처리                |
| **네비게이션**  | Navigation Compose | 화면 네비게이션            |
| **지도**        | Naver Map SDK      | 지도 서비스                |
| **소셜 로그인** | Kakao SDK          | 카카오 로그인              |

<br><br/>

## 🚀 시작하기

### 요구사항

- **Android 9.0 (API 28)** 이상
- **JDK** 17

### API 키 설정

API 키 설정(local.properties 파일에 다음 키들을 추가)

- kakaoLogin_api_key=your_kakao_api_key_here
- kakaoLogin_Redirect_Uri=your_kakao_redirect_uri_here
- naver_client_id=your_naver_client_id_here
- base_url= 자체 백엔드 서버 주소
- user_base_url= 자체 백엔드 서버 주소(프로필 API, 채팅 API)

### API 키 발급 방법

1. **카카오 개발자 콘솔** (https://developers.kakao.com/)

   - 애플리케이션 등록
   - 네이티브 앱 키 발급
   - 리다이렉트 URI 설정

2. **네이버 개발자 센터** (https://developers.naver.com/)

   - 애플리케이션 등록 (지도 서비스)
   - Client ID 발급

3. **백엔드 API 서버**
   - 자체 백엔드 서버 주소 설정

### Firebase 설정

1. **Firebase 프로젝트 생성**

   - Firebase Console에서 새 프로젝트 생성
   - Android 앱 추가 (패키지명: `com.housweet.app`)

2. **google-services.json 파일 다운로드**

   - Firebase Console에서 설정 파일 다운로드
   - `app/` 디렉토리에 파일 배치

3. **Firebase 서비스 활성화**

   - **Analytics**: 앱 사용 분석
   - **Crashlytics**: 크래시 리포팅

<br><br/>

## 🏗 프로젝트 구조

```
Housweet/
├── app/                    # 메인 애플리케이션 모듈
├── data/                   # 데이터 레이어
│   ├── api/               # API 서비스
│   ├── datasource/        # 데이터 소스
│   ├── repository/        # 레포지토리 구현
│   └── request/response/  # API 모델
├── domain/                 # 도메인 레이어
│   ├── model/             # 도메인 모델
│   ├── repository/        # 레포지토리 인터페이스
│   └── usecase/           # 유스케이스
├── presentation/           # 프레젠테이션 레이어
│   ├── ui/                # Compose UI 화면
│   ├── viewmodel/         # ViewModel
│   └── navigation/        # 네비게이션
└── fastlane/              # CI/CD 자동화
```

<br><br/>

## 📊 프로젝트 정보

- 개발 기간: 2025년 4월 ~ 2025년 9월
- 개발 인원:
    - 안드로이드 개발자 3명
    - 백엔드 개발자 2명
    - 디자인 1명
  <br><br/>

## 📱 플레이 스토어 링크

- https://play.google.com/store/apps/details?id=com.housweet.app
<br><br/>

## 📺 구현 화면
### - 방구하기 화면 및 홈 화면
<img width="262" height="514" alt="Image" src="https://github.com/user-attachments/assets/c3084e83-f2fd-46b3-8b40-5133aa43afd6" />
<img width="262" height="513" alt="Image" src="https://github.com/user-attachments/assets/1e0a41b1-7733-4872-aefa-0f147b232769" />

### - 지도 화면 및 지역 검색 화면, 방 공고 작성 및 조회 화면
<img width="262" height="514" alt="Image" src="https://github.com/user-attachments/assets/77f24154-1bcb-44eb-856a-f4c0cedff771" />
<img width="262" height="514" alt="Image" src="https://github.com/user-attachments/assets/171ed494-9af3-4434-add4-00ab8ef1096b" />
<img width="262" height="513" alt="Image" src="https://github.com/user-attachments/assets/261a1ed0-900d-4e81-9d28-674fea4a493b" />

### - 채팅 화면 및 프로필 조회 화면
<img width="262" height="513" alt="image" src="https://github.com/user-attachments/assets/6363352e-fcc3-428f-828e-a296d09159ad" />
<img width="262" height="513" alt="image" src="https://github.com/user-attachments/assets/9bc6b4f0-dde7-4147-89b6-23d56ef0a679" />
