import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TourApiExample {
    public static void main(String[] args) {
        try {
            // OpenAPI 요청 URL 설정
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService1/locationBasedList1");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=%2FYlxk9%2F2%2F4C1Wqj%2FelEWXBkPoVHK6lYc%2F5opKHIU8zlwNjkGmZHXX%2BW1m7fKGu57nb2T7eM%2FGzmjmNpiq1pyqg%3D%3D"); // 서비스키
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=50"); // 한 페이지 결과 수
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1"); // 페이지 번호
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=ETC"); // OS 구분 (ETC로 설정)
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=AppTest"); // 어플리케이션 이름
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=A"); // 정렬 방식 (제목순)
            urlBuilder.append("&" + URLEncoder.encode("mapX", "UTF-8") + "=126.988251"); // 남산타워 경도 (X좌표)
            urlBuilder.append("&" + URLEncoder.encode("mapY", "UTF-8") + "=37.551242"); // 남산타워 위도 (Y좌표)
            urlBuilder.append("&" + URLEncoder.encode("radius", "UTF-8") + "=3500"); // 반경 3500m
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=xml"); // 응답 형식 XML

            // URL 객체 생성
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 결과 읽기
            BufferedReader rd;
            if (responseCode == 200) { // 정상 응답
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else { // 에러 발생
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            // XML 데이터를 StringBuilder에 저장
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            // 파일 객체 생성 및 경로 확인
            File file = new File("src/main/java/com/locationbase/tour_api_response.xml");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(sb.toString());
                System.out.println("XML 응답이 '" + file.getAbsolutePath() + "' 경로에 저장되었습니다.");
            } catch (Exception e) {
                e.printStackTrace();
            }

//            // 5초 지연 추가
//            Thread.sleep(5000);
//
//            // 콘솔 명령어 실행
//            String command = "cmd.exe /c start cmd.exe /k \"cd C:\\Spring_Study\\Team_project\\src\\main\\java && live-server --port=5501 --open=/geocoder_example.html\"";
//            ProcessBuilder pb = new ProcessBuilder(command.split(" "));
//            pb.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
