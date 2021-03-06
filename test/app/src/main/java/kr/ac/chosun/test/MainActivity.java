package kr.ac.chosun.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class MainActivity extends Activity {

    //cgtdt 혼잡일자 //조회일자YYYYMMDD
    //cgthm 혼잡일시 //업데이트시간 HHMM
    //gate1 2번 출국장 혼잡도
    //gate2 3번 출국장 혼잡도
    //gate3 4번 출국장 혼잡도
    //gate4 5번 출국장 혼잡도
    //gateinfo1 2번 출국장 대기인원수
    //gateinfo2 3번 출국장 대기인원수
    //gateinfo3 4번 출국장 대기인원수
    //gateinfo4 5번 출국장 대기인원수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button = (ImageButton)findViewById(R.id.add_info); //패스트 트랙 안내메세지 버튼 생성
        button.setOnClickListener(new View.OnClickListener() {  // 안내메세지 버튼 이벤트 추가
            @Override
            public void onClick(View view) {
                show();
            }
        });

        //버튼->새로운 화면으로 이동하는 함수
        ImageButton congestion = (ImageButton) findViewById(R.id.congestion_button); //혼잡도 버튼을 지정합니다.
        congestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //혼잡도 버튼이 눌렸을 때
                Intent intent = new Intent(MainActivity.this, congestion.class);
                startActivity(intent); //혼잡도 화면으로 이동
            }
        });

        ImageButton landing = (ImageButton) findViewById(R.id.flight_land_button); //이륙정보 버튼을 지정합니다.
        landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //이륙정보 버튼이 눌렸을 때
                Uri land = Uri.parse("https://www.airport.kr/ap/ko/arr/arrPasSchList.do");
                Intent landing = new Intent(Intent.ACTION_VIEW, land);
                startActivity(landing); //이륙정보 화면으로 이동
            }
        });

        ImageButton delay = (ImageButton) findViewById(R.id.delay_button); //지연/결항 버튼을 지정합니다.
        delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //지연/결항 버튼이 눌렸을 때
                Intent intent = new Intent(MainActivity.this, delay.class);
                startActivity(intent); //지연/결항 화면으로 이동
            }
        });

        ImageButton parking = (ImageButton) findViewById(R.id.parking_button); //주차장 버튼을 지정합니다.
        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //주차장 버튼이 눌렸을 때
                Intent intent = new Intent(MainActivity.this, parking.class);
                startActivity(intent); //주차장화면으로 이동
            }
        });

        ImageButton takeoff = (ImageButton) findViewById(R.id.flight_takeoff_button);
        takeoff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri takeoff = Uri.parse("https://www.airport.kr/ap/ko/dep/depPasSchList.do");
                Intent gotakeoff = new Intent(Intent.ACTION_VIEW, takeoff);
                startActivity(gotakeoff); //액티비티 이동
            }
        });

        ImageButton convenience = (ImageButton) findViewById(R.id.convienience_button); //편의시설 버튼을 지정합니다.
        convenience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //편의시설 버튼이 눌렸을 때
                Intent intent = new Intent(MainActivity.this, convenience.class);
                startActivity(intent); //편의시설 화면으로 이동
            }
        });

        StrictMode.enableDefaults();

        //파싱된 결과확인을 위한 textview변수선언
        TextView status1 = (TextView)findViewById(R.id.status1);
        TextView status2 = (TextView)findViewById(R.id.status2);
        TextView status3 = (TextView)findViewById(R.id.status3);
        TextView status4 = (TextView)findViewById(R.id.status4);

        TextView title = (TextView)findViewById(R.id.title);

        //필요한 item 변수명을 선언
        boolean initem = false, inAreadiv = false, inCgtdt = false, inCgthm =false, inGateinfo1 = false, inGateinfo2 = false, inGateinfo3 = false, inGateinfo4 = false;

        String areadiv = null, cgtdt = null, cgthm = null, gateinfo1= null, gateinfo2 = null, gateinfo3 = null, gateinfo4 = null;


        try{

            URL url = new URL("http://openapi.airport.kr/openapi/service/StatusOfDepartures/getDeparturesCongestion?ServiceKey=" +
                    "Nb%2BV2BcVQ%2BjSh4zZQkvreUtW0lbjoMq4kmUkR3Inc0OHZXmUTxvqXKaDhBoqvV0HGIx0%2BodRUS8K4Vyg7qiOwg%3D%3D"
            ); //openAPI 접근URL과 serviceKey를 이용하여 xml 데이터 접근

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance(); // 위에서 생성된 URL을 통하여 서버에 요청하면 결과가 XML Resource로 전달됨
            XmlPullParser parser = parserCreator.newPullParser(); //XML Resource 를 파싱할 parser를 parserCreator로 생성

            parser.setInput(url.openStream(), null); // 파서 통하여 각 요소들의 이벤트성 처리를 반복수행


            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){ //XML문이 끝날 때까지 정보를 읽는다.

                switch(parserEvent){
                    case XmlPullParser.START_TAG: //parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("areadiv")){ //areadiv 만나면 내용을 받을수 있게
                            inGateinfo1 = true;
                        }
                        if(parser.getName().equals("cgtdt")){ //dgtdt 만나면 내용을 받을수 있게
                            inCgtdt = true;
                        }
                        if(parser.getName().equals("cgthm")){ //cgthm 만나면 내용을 받을수 있게
                            inCgthm = true;
                        }
                        if(parser.getName().equals("gateinfo1")){ //gateinfo1 만나면 내용을 받을수 있게
                            inGateinfo1 = true;
                        }
                        if(parser.getName().equals("gateinfo2")){ //gateinfo2 만나면 내용을 받을수 있게
                            inGateinfo2 = true;
                        }
                        if(parser.getName().equals("gateinfo3")){ //gateinfo3 만나면 내용을 받을수 있게
                            inGateinfo3 = true;
                        }
                        if(parser.getName().equals("gateinfo4")){ //gateinfo4 만나면 내용을 받을수 있게
                            inGateinfo4 = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            status1.setText(status1.getText()+"에러");
                            //에러코드에 따라 다른 메세지를 출력
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inAreadiv){ //isAreadiv이 true일 때 태그의 내용을 저장.
                            areadiv = parser.getText();
                            inAreadiv = false;
                        }
                        if(inCgtdt){ //inCgtdt이 true일 때 태그의 내용을 저장.
                            cgtdt = parser.getText();
                            inCgtdt = false;
                        }
                        if(inCgthm){ //inCgthm이 true일 때 태그의 내용을 저장.
                            cgthm = parser.getText();
                            inCgthm = false;
                        }
                        if(inGateinfo1){ //inGateinfo1이 true일 때 태그의 내용을 저장.
                            gateinfo1 = parser.getText();
                            inGateinfo1 = false;
                        }
                        if(inGateinfo2){ //inGateinfo2이 true일 때 태그의 내용을 저장.
                            gateinfo2 = parser.getText();
                            inGateinfo2 = false;
                        }
                        if(inGateinfo3){ //inGateinfo3이 true일 때 태그의 내용을 저장.
                            gateinfo3 = parser.getText();
                            inGateinfo3 = false;
                        }
                        if(inGateinfo4){ //inGateinfo4이 true일 때 태그의 내용을 저장.
                            gateinfo4 = parser.getText();
                            inGateinfo4 = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){


                            //각 Gate에 몇 명이 있는지 text저장
                            if (gateinfo1.equals("0")){
                                status1.setText(("종료"));
                            }
                            else status1.setText(status1.getText()+gateinfo1 + "명");
                            if(gateinfo2.equals("0")){
                                status2.setText(("종료"));
                            }
                            else status2.setText(status2.getText()+gateinfo2 + "명");
                            if(gateinfo3.equals("0")){
                                status3.setText(("종료"));
                            }
                            else status3.setText(status3.getText()+gateinfo3 + "명");
                            if(gateinfo4.equals("0")){
                                status4.setText(("종료"));
                            }
                            else status4.setText(status4.getText()+gateinfo4 + "명");

                            int intgateinfo1 = Integer.parseInt(gateinfo1);
                            int intgateinfo2 = Integer.parseInt(gateinfo2);
                            int intgateinfo3 = Integer.parseInt(gateinfo3);
                            int intgateinfo4 = Integer.parseInt(gateinfo4);
                            int intgateinfo[]={intgateinfo1, intgateinfo2, intgateinfo3, intgateinfo4};
                            int max = intgateinfo[0];
                            int min = intgateinfo[0];



                            for(int i =1 ; i<intgateinfo.length; i++){
                                if(intgateinfo[i] > max){
                                    max = intgateinfo[i];
                                }
                                if(intgateinfo[i] < min) {
                                    min = intgateinfo[i];
                                }

                            }


                            ImageView imageview1 = (ImageView)findViewById(R.id.imageView1);
                            ImageView imageview2 = (ImageView)findViewById(R.id.imageView2);
                            ImageView imageview3 = (ImageView)findViewById(R.id.imageView3);
                            ImageView imageview4 = (ImageView)findViewById(R.id.imageView4);


                            if(intgateinfo[0] == max){
                                imageview1.setImageResource(R.drawable.bubble9);
                            }else {
                                imageview1.setImageResource(R.drawable.bubble1);
                            }
                            if(intgateinfo[1] == max){
                                imageview2.setImageResource(R.drawable.bubble10);
                            }else {
                                imageview2.setImageResource(R.drawable.bubble2);
                            }
                            if(intgateinfo[2] == max){
                                imageview3.setImageResource(R.drawable.bubble11);
                            }else {
                                imageview3.setImageResource(R.drawable.bubble3);
                            }
                            if(intgateinfo[3] == max){
                                imageview4.setImageResource(R.drawable.bubble12);
                            }else {
                                imageview4.setImageResource(R.drawable.bubble4);
                            }

                            if(intgateinfo[0] == min){
                                imageview1.setImageResource(R.drawable.bubble5);
                            }else {
                                imageview1.setImageResource(R.drawable.bubble1);
                            }
                            if(intgateinfo[1] == min){
                                imageview2.setImageResource(R.drawable.bubble6);
                            }else {
                                imageview2.setImageResource(R.drawable.bubble2);
                            }
                            if(intgateinfo[2] == min){
                                imageview3.setImageResource(R.drawable.bubble7);
                            }else {
                                imageview3.setImageResource(R.drawable.bubble3);
                            }
                            if(intgateinfo[3] == min){
                                imageview4.setImageResource(R.drawable.bubble8);
                            }else {
                                imageview4.setImageResource(R.drawable.bubble4);
                            }



                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e) {
            status1.setText("에러발생");

        }
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("패스트트랙(교통약자용)");
        builder.setMessage("이용 시설 : 1번 6번 출국장\n이용 시간 : 오전 7시~오후 7시\n이용 대상 : 교통약자 및 출입국 우대자");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}







