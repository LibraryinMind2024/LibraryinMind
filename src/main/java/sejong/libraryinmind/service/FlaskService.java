package sejong.libraryinmind.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FlaskService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://localhost:5000"; // Flask 서버 URL

    // 데이터 가져오기 (예: /api/data 호출)
    public String getFlaskData() {
        String url = flaskUrl + "/api/data";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public Map<String, Object> uploadAndProcessImage(MultipartFile file) throws IOException {
        // Flask 서버 URL 설정
        String url = flaskUrl + "/uploads";

        // 파일을 FormData로 변환
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // MultiValueMap 사용하여 파일을 FormData로 추가
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();  // 파일 이름 설정
            }
        });

        // HttpEntity 생성 (MultiValueMap과 헤더)
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // POST 요청 보내기
        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, Map.class);

        // Flask 서버에서 받은 응답 반환
        return response.getBody();
    }
}
