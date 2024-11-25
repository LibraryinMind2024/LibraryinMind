package sejong.libraryinmind.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
public class MyController {

    @GetMapping("/uploads")
    public String executePythonScript(Model model) {
        try {
            // Python 스크립트 경로 지정
            String pythonScriptPath = "src/main/resources/app.py";

            // Python 프로세스를 실행
            ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScriptPath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 결과를 읽음
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            System.out.println(output.toString());  // 출력 확인

            // Python 출력 결과를 모델에 추가
            model.addAttribute("pythonOutput", output.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "urlTest"; // HTML 템플릿 이름
    }
}
