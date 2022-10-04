package com.talkmoaserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * txt 파일을 열어서 단어를 가공해주는 서비스
 * 대화자 : 단어(토큰) 리스트로 매핑해주고, DB에 저장해줌
 */

@Slf4j
@Service
public class ParseService {
    public static final String TEMP_SAVED_PATH =
            System.getProperty("user.dir") + "/temp/target.txt";

    // txt 파일을 읽기 위해서 일단 임시로 저장함
    private void saveFile(MultipartFile multipartFile) throws IOException {
        File file = new File(TEMP_SAVED_PATH);
        multipartFile.transferTo(file);
    }

    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(TEMP_SAVED_PATH));
    }

    // 대화내역(txt) 파일을 라인 별로 자르는 함수
    private List<String> slicePerLine() throws IOException {
        ArrayList<String> result = new ArrayList<>();
        BufferedReader reader = openFile();
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            // 한 사람이 길게 쓴 문자까지 탐지해서 라인 나누기
            if (!line.startsWith("[") && !result.isEmpty() && !line.startsWith("------")) {
                String lastLine = result.get(result.size() - 1);
                result.remove(lastLine);
                String newLine = lastLine + line;
                result.add(newLine);
                continue;
            }

            result.add(line);
        }
        reader.close();
        return result;
    }

    // 특정 라인을 토큰화 하는 함수
    private List<String> tokenizePerLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, " \n,[]");
        List<String> result = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            result.add(token);
        }
        return result;
    }

    // 대화내역(txt) 전체를 토큰화 하는 함수
    // TODO : 현재 단어 추출만 하지 못함 -> 성능 개선 필요
    public List<String> tokenizeTotal() throws IOException {
        List<String> result = new ArrayList<>();
        BufferedReader reader = openFile();
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            result.addAll(tokenizePerLine(line));
        }
        reader.close();
        return result;
    }

    // 대화자 추출
    // TODO : 정확한 대화자만 추출해야하는데 잘 안됨 -> 개선 필요
    public List<String> getTalkers() throws IOException {
        HashSet<String> result = new HashSet<>();
        BufferedReader reader = openFile();
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            StringTokenizer tokenizer = new StringTokenizer(line, "[]");
            while (tokenizer.hasMoreTokens()) {
                String t = tokenizer.nextToken();
                if (!t.startsWith(" ") && !t.startsWith("오전") && !t.startsWith("오후")) {
                    result.add(t);
                }
            }
        }
        reader.close();

        log.info("셋 = {}", result);
        return result.stream().toList();
    }

    // 방 이름 추출
    public String getRoomName() throws IOException {
        return openFile().readLine().split(" ")[0];
    }

    //  대화자 (1) : 대화 한 단어들 (리스트) 로 매핑 지어서 돌려주는 함수
    private Map<String, List<String>> mapTalkerToLine(List<String> talkers, List<String> slicedPerLine) {
        Map<String, List<String>> result = new HashMap<>();
        for (String line : slicedPerLine) {
            for (String talker : talkers) {
                if (line.contains(talker)) {
                    List<String> tokens = tokenizePerLine(line);
                    if (!result.containsKey(talker)) {
                        result.put(talker, tokens);
                    } else {
                        result.get(talker).addAll(tokens);
                    }
                }
            } // talker loop end
        } // line loop end
        return result;
    }

    /**
     * 해당 서비스 핵심 함수
     * 다른 객체들은 ParsingService.parse(MultipartFile file) 을 호출해서 얻은 결과물에만 사용하고
     * 그 내부 로직은 몰라야 한다 (캡슐화)
     */
    public Map<String, List<String>> parse(MultipartFile multipartFile) throws IOException {
        // multipartFile -> File 로 저장한다
        saveFile(multipartFile);

        // txt 파일을 열어서 라인별로 자른다
        List<String> slicedResult = slicePerLine();

        // txt 파일에서 대화자를 추출한다
        List<String> talkers = getTalkers();

        // 대화자(1) : 말한 단어(토큰화된 리스트) 를 매핑한다
        return mapTalkerToLine(talkers, slicedResult);
    }
}
