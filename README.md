# Traffic Calendar App
GPS를 사용해 해당 지역의 날씨정보를 제공하고,
캘린더에서 해당날짜에 출발지와 도착지를 정하면 외부 API를 사용해 통근 시간을 자동으로 계산해주는 앱입니다.

일정 관리를 위해 고안된 해당 어플리케이션은 GPS를 통한 날씨 정보와 기온, 현재 위치, 시간 별 일기 예보를 제공하고, 
일정 등록 시 날짜와 시간, 출발지와 도착지 등록 및 일정 삭제 기능과 장소 또는 주소 검색, 소모 시간, 경로 검색을 보여주는 기능으로 구성되어 있습니다.
기존의 캘린더 어플리케이션이 갖는 기능에서 날씨와 교통 정보를 추가하여 사용자가 보다 쉽고 편하게 일정을 관리하고 날씨와 교통 정보를 얻을 수 있는 플랫폼을 제공합니다.
## 기술스택
- Android Studio
- SQLite
- Java

## Installing

SQLitle는 안드로이드에 기본적으로 내장된 lightweight 및 파일 기반 데이터베이스이므로 별도의 설치가 필요하지 않습니다.
해당 프로젝트는 배포되지 않았습니다.
별도로 app\build\outputs\apk\debug의 app-debug.apk를 다운받아 안드로이드 운영체제의 기기에서 설치해주세요

## 기능 구현
**Weather Layout**</br>
![크기변환 KakaoTalk_20240806_034810044](https://github.com/user-attachments/assets/b0429707-2291-4153-8504-ddaeab2d3c2f)

 최초로 동작하게 될 때 보여지는 레이아웃이다. GPS를 활용하여 현재 위치의 날씨와 기온, 일기 예보를 제공한다.
 하단의 recylcerview를 통해 오늘의 시간대별 날씨와 온도, 풍속등을 한눈에 볼 수 있다.</br>
 
**Custom Calendar Layout**</br>
![크기변환 KakaoTalk_20240806_035640965_05](https://github.com/user-attachments/assets/c55bc1a1-eaf1-41cb-9d90-c3737708fdf4)

캘린더를 열람하는 기능을 수행하는 레이아웃이다. 달력을 이동하는 버튼과 모든 날짜를 보여주는 그리드뷰로 이루어져있다. </br>

</br>
주소와 장소 API가 활용되어 검색 란에 텍스트를 입력하여 정확한 위치 정보를 등록할 수 있다. </br>

주로 상용되는 지도 앱에서 사용되는 검색 알고리즘을 보면 대게 주소 서비스로만 이루어져 있지 않다. 

사용자는 이를 인식하지 않고 해당 장소의 이름을 알고 있으면 해당 장소의 이름을 검색 키워드로 사용하고 해당 장소의 주소를 알고 있다면 해당 장소의 주소를 키워드로 사용하지만,

주로 사용되는 앱에서의 알고리즘은 이를 자동으로 인식하고 사용자가 입력하는 형식에 따라 자동으로 주소 API를 사용해서 정보를 보여줄지, 장소 API를 사용해서 정보를 보여줄지 결정한다.

이와 같은 알고리즘을 원시적으로나마 구현하기 위해 사용자가 알고 있는 정보가 해당 장소의 이름인지, 주소인지 선택하게 함으로써 복잡한 해당 기능을 간단하게 구현해 보았다.</br>
![크기변환 KakaoTalk_20240806_035640965_04](https://github.com/user-attachments/assets/c8be3217-4fad-4d75-b8e5-c85061fc7698)
![크기변환 KakaoTalk_20240806_035640965_02](https://github.com/user-attachments/assets/2212fa49-e5f1-4bcd-9805-a155a7f3d29a)
![크기변환 KakaoTalk_20240806_035640965_03](https://github.com/user-attachments/assets/a7c4bc5e-dbdf-444f-9494-a2e6e5bdd8b8)

사용자는 해당 날짜를 눌러 일정 내용과 시간, 출발지와 도착지를 주소 또는 장소로 검색 후 등록하여 새로운 일정을 생성할 수 있다. </br>

</br>

일정 생성 시 해당 날짜에 일정이 표시된다. 생성된 일정 목록은 해당 날짜를 롱 클릭 시 확인할 수 있다.</br>
</br>
![크기변환 KakaoTalk_20240806_035640965](https://github.com/user-attachments/assets/a30333cd-994d-4bb3-8ff5-fa2fa4ea99a9)

등록된 일정은 입력한 출발지와 도착지, 날짜 정보를 바탕으로 해당 날짜를 longclick 해서 보여지는 Recylerview에서 소모시간이 보여진다. 
하지만 사용자 입장에서는 해당 일정의 출발지와 도착지에서 걸리는 소모시간에 대해서 의문을 가질 수 있다. </br>
</br>

![크기변환 KakaoTalk_20240806_035640965_01](https://github.com/user-attachments/assets/4998e76d-9065-4e5d-b4ec-6ee1191d736f)


이에 대한 사용자의 의문을 해결하기 위해 각 일정의 돋보기 버튼을 클릭할시 사용자에게 출발지와 도착지에서 걸리는 다양한 경로를 보여주고 사용자가 선택하거나, 해당 경로를 자세히 볼 수 있게 하였다.

## 사용 설명 영상


https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/37ad76f0-e1ca-483b-a01d-c9f9a8d1adf9





## Authors

* **hwlee9905** - [github](https://github.com/hwlee9905)

## License

This project is licensed under hwlee9905
