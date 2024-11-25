package sejong.libraryinmind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.libraryinmind.service.FlaskService;

import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/flask")
public class FlaskController {

    @Autowired
    private FlaskService flaskService;

    // 업로드 결과를 HTML로 표시
    @GetMapping("/upload/view")
    public String uploadImageToFlask(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        Map<String, Object> response = flaskService.uploadAndProcessImage(file);

        String image_url = response.get("image_url").toString();
        String content = response.get("ocr_text").toString();

        if (content.length() > 300) {
            content = content.substring(0, 300); // 5000자 이상은 잘라서 저장
        }

        model.addAttribute("imageUrl", image_url);
        model.addAttribute("ocrText", content);
        return "result"; // result.html 템플릿으로 이동
    }
}