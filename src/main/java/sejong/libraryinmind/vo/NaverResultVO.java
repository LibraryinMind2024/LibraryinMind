package sejong.libraryinmind.vo;


import lombok.*;

import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NaverResultVO {
    private String lastBuildDate; //검색 결과를 생성한 시간
    private int total; //총 검색 결과 개수
    private int start; //검색 시작 위치
    private int display; //한 번에 표시할 검색 결과 개수
    private List<BookVO> items; //개별 검색 결과

}
