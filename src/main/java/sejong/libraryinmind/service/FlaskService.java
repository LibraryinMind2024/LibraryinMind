package sejong.libraryinmind.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FlaskService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://localhost:5000"; // Flask 서버 URL

    public Map<String, Object> uploadAndProcessImage(MultipartFile file,String searchOption) throws IOException {
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
        body.add("search-option", searchOption);


        // HttpEntity 생성 (MultiValueMap과 헤더)
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        System.out.println(requestEntity);

        try {
            // Flask API에서 JSON 응답을 Map으로 받기
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity, Map.class);

            return response.getBody();
        }
        catch (HttpClientErrorException e) {
            // Flask 서버에서 4xx 에러가 발생한 경우
            System.err.println("Client Error: " + e.getResponseBodyAsString());
            return Map.of("error", "Client error occurred: " + e.getMessage());
        }
        catch (HttpServerErrorException e) {
            // Flask 서버에서 5xx 에러가 발생한 경우
            System.err.println("Server Error: " + e.getResponseBodyAsString());
            return Map.of("error", "Server error occurred: " + e.getMessage());
        }
        catch (Exception e) {
            // 그 외 다른 예외가 발생한 경우
            e.printStackTrace();
            return Map.of("error", "Unexpected error occurred: " + e.getMessage());
        }
    }
}
