package sejong.libraryinmind.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.dto.FileDto;
import sejong.libraryinmind.entity.CustomerEntity;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.service.DiaryService;
import sejong.libraryinmind.service.FileService;
import sejong.libraryinmind.util.MD5Generator;

import java.io.File;
import java.util.List;

@Controller
public class DiaryController {
    private DiaryService diaryService;
    private FileService fileService;

    public DiaryController(DiaryService diaryService, FileService fileService){
        this.diaryService = diaryService;
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String diarylist(Model model){

        List<DiaryDto> diaryDtoList = this.diaryService.getList();
        model.addAttribute("diaryDtoList",diaryDtoList);

        return "Diarylist.html";
    }

    @GetMapping("/post")
    public String diary(){
        return "Posting.html";
    }


    @PostMapping ("/post")
    public String write(@RequestParam("file")MultipartFile files, DiaryDto diaryDto){
        try {
            String origFilename = files.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePath = System.getProperty("user.dir") + "/files";

            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "/" + filename;
            files.transferTo(new File(filePath));

            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilepath(filePath);

            Long fileId = fileService.saveFile(fileDto);
            diaryDto.setFileId(fileId);
            diaryService.savePost(diaryDto);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/";
    }
    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        DiaryDto diaryDto = diaryService.getPost(id);
        model.addAttribute("post", diaryDto);
        return "Detail.html";
    }
    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id) {
        diaryService.deletePost(id);
        return "redirect:/";
    }

}
