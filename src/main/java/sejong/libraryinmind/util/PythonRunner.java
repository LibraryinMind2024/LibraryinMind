package sejong.libraryinmind.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class PythonRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // resources 폴더 내 app.py 경로 설정
        File pythonFile = new File("src/main/resources/app.py"); // 절대 경로 사용

        if (!pythonFile.exists()) {
            throw new FileNotFoundException("Python 파일이 존재하지 않습니다: " + pythonFile.getAbsolutePath());
        }

        // Flask 서버 실행
        ProcessBuilder processBuilder = new ProcessBuilder(
                "newvenv/bin/python3", pythonFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);  // 에러 스트림도 동일한 출력 스트림으로 합침
        processBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);  // 출력을 무시

        Process process = processBuilder.start();

        // 종료 코드 확인
        int exitCode = process.waitFor();
        System.out.println("Python script finished with exit code: " + exitCode);
    }
}
