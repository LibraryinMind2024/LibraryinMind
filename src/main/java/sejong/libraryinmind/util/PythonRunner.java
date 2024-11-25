package sejong.libraryinmind.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class PythonRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // resources 폴더 내 app.py 경로 설정
        File pythonFile = new File("src/main/resources/app.py"); // 경로를 절대 경로로 설정

        if (!pythonFile.exists()) {
            throw new FileNotFoundException("Python 파일이 존재하지 않습니다: " + pythonFile.getAbsolutePath());
        }

        ProcessBuilder processBuilder = new ProcessBuilder(
                "newvenv/bin/python3", pythonFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);  // 에러 스트림도 동일한 출력 스트림으로 합침
        Process process = processBuilder.start();

        // Python 출력 읽기 (옵션)
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[Python]: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        outputThread.start();

        // 종료 코드 확인
        int exitCode = process.waitFor();
        System.out.println("Python script finished with exit code: " + exitCode);

        // outputThread가 종료될 때까지 기다림
        outputThread.join();
    }
}
