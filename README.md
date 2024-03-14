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

app\build\outputs\apk\debug의 app-debug.apk을 설치해주세요

## Deployment

1. 데이터베이스 생성 및 ERD작성
2. 개발 제안서 제출 및 교수 컨설팅 2회 실시 
3. 설계문서, UI보고서 설계
4. 데이터베이스 및 UI수정
5. JDBC, HTML, CSS 작성
6. 실행 시 오류 및 충돌 개선
## 기능 구현
**Weather Layout**</br>
![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/638010ab-97b0-4c0c-abe3-41ddc598e6e1)

 최초로 동작하게 될 때 보여지는 레이아웃이다. GPS를 활용하여 현재 위치의 날씨와 기온, 일기 예보를 제공한다.
 하단의 recylcerview를 통해 오늘의 시간대별 날씨와 온도, 풍속등을 한눈에 볼 수 있다.</br>
 
**Custom Calendar Layout**</br>
![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/c0d10fb6-08b5-4c9c-b1a5-58872cb86f68)


캘린더를 열람하는 기능을 수행하는 레이아웃이다. 달력을 이동하는 버튼과 모든 날짜를 보여주는 그리드뷰로 이루어져있다. </br>
![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/536c3deb-0792-426d-87fb-0529ab0c4f7a)
![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/61153f9a-4d45-4741-97d3-2c762f3997c2)
![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/9f18dac3-affc-4855-9b11-b071f2acd91d)
![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/0bb9ca75-e6f2-4bc5-9c73-412fef1ca61c)</br>
주소와 장소 API가 활용되어 검색 란에 텍스트를 입력하여 정확한 위치 정보를 등록할 수 있다. </br>

주로 상용되는 지도 앱에서 사용되는 검색 알고리즘을 보면 대게 주소 서비스로만 이루어져 있지 않다. 

사용자는 이를 인식하지 않고 해당 장소의 이름을 알고 있으면 해당 장소의 이름을 검색 키워드로 사용하고 해당 장소의 주소를 알고 있다면 해당 장소의 주소를 키워드로 사용하지만,

주로 사용되는 앱에서의 알고리즘은 이를 자동으로 인식하고 사용자가 입력하는 형식에 따라 자동으로 주소 API를 사용해서 정보를 보여줄지, 장소 API를 사용해서 정보를 보여줄지 결정한다.

이와 같은 알고리즘을 원시적으로나마 구현하기 위해 사용자가 알고 있는 정보가 해당 장소의 이름인지, 주소인지 선택하게 함으로써 복잡한 해당 기능을 간단하게 구현해 보았다.

사용자는 해당 날짜를 눌러 일정 내용과 시간, 출발지와 도착지를 주소 또는 장소로 검색 후 등록하여 새로운 일정을 생성할 수 있다. </br>

![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/d8c86a3c-5e8b-41c1-8408-bb51b16cbea5)</br>

일정 생성 시 해당 날짜에 일정 개수가 표시된다. 생성된 일정 목록은 해당 날짜를 롱 클릭 시 확인할 수 있다.</br>
![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/017e375d-50b7-4733-a066-160ea7faa02f)</br>

등록된 일정은 입력한 출발지와 도착지, 날짜 정보를 바탕으로 해당 날짜를 longclick 해서 보여지는 Recylerview에서 소모시간이 보여진다. 
하지만 사용자 입장에서는 해당 일정의 출발지와 도착지에서 걸리는 소모시간에 대해서 의문을 가질 수 있다. </br>

![image](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/5e1698bc-52b6-4483-98c7-04a318a76d2f)</br>

이에 대한 사용자의 의문을 해결하기 위해 각 일정의 돋보기 버튼을 클릭할시 사용자에게 출발지와 도착지에서 걸리는 다양한 경로를 보여주고 사용자가 선택하거나, 해당 경로를 자세히 볼 수 있게 하였다.

## 사용 설명 영상
![동영상](https://github.com/hwlee9905/Traffic_Calendar_App/assets/127581593/53c99549-a4e7-4471-bdbb-ad0dd292f8b3)


## Authors

* **hwlee9905** - [github](https://github.com/hwlee9905)

## License

This project is licensed under hwlee9905
