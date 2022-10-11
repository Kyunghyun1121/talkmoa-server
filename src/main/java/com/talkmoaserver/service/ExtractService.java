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
public class ExtractService {
    public static final String TEMP_SAVED_PATH =
            System.getProperty("user.dir") + "/temp/target.txt";

    // txt 파일을 읽기 위해서 일단 임시로 저장함
    public void saveFile(MultipartFile multipartFile) throws IOException {
        File file = new File(TEMP_SAVED_PATH);
        multipartFile.transferTo(file);
    }

    private BufferedReader openFile() throws FileNotFoundException {
        return new BufferedReader(new FileReader(TEMP_SAVED_PATH));
    }

    // 대화내역(txt) 파일을 라인 별로 자르는 함수
    private List<String> slicePerLine() throws IOException {
//        log.info("slicePerLine 호출");
//        long start = System.currentTimeMillis();
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
//        long end = System.currentTimeMillis();
//        log.info("slicedPerLine 수행 시간 = {}s", (end - start)/1000.0);
        return result;
    }

    // 특정 라인을 토큰화 하는 함수
    private List<String> tokenizePerLine(String talker, String line) {
        String exclude = " \n,[]오전오후"+talker;
        StringTokenizer tokenizer = new StringTokenizer(line, exclude);
        List<String> result = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.contains(":") || token.startsWith("--------")) continue;
            result.add(token);
        }
        return result;
    }

    // 대화자 추출
    public List<String> getTalkers() throws IOException {
        HashSet<String> result = new HashSet<>();
        BufferedReader reader = openFile();
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            if (line.startsWith("[")) {
                int lastIndex = line.indexOf("]");
                if (lastIndex == -1) continue;
                String talkerCandidate = line.substring(1, lastIndex);
                if (talkerCandidate.length() >= 10) continue;
                result.add(talkerCandidate);
            }
        }
        reader.close();
        return result.stream().toList();
    }

    // 방 이름 추출
    public String getRoomName() throws IOException {
        String[] firstLine = openFile().readLine().split(" ");
        StringBuilder chatRoomName = new StringBuilder();
        for (String word : firstLine) {
            if (word.startsWith("님과")) break;
            chatRoomName.append(" ").append(word);
        }
        return chatRoomName.substring(1); // 맨 앞 공백 제거 후 리턴
    }

    //  대화자 (1) : 대화 한 단어들 (리스트) 로 매핑 지어서 돌려주는 함수
    private Map<String, List<String>> mapTalkerToToken(List<String> talkers, List<String> slicedPerLine) {
//        long start = System.currentTimeMillis();
        Map<String, List<String>> result = new HashMap<>();
        for (String line : slicedPerLine) {
            for (String talker : talkers) {
                if (line.contains(talker)) {
                    List<String> tokens = tokenizePerLine(talker, line);
                    if (!result.containsKey(talker)) {
                        result.put(talker, tokens);
                    } else {
                        result.get(talker).addAll(tokens);
                    }
                }
            } // talker loop end
        } // line loop end
//        long end = System.currentTimeMillis();
//        log.info("mapTalkerToToken 수행 시간 = {}s", (end - start)/1000.0);
        return result;
    }

    private Map<String, List<String>> mapTalkerToLine(List<String> talkers, List<String> slicedPerLine) {
//        long start = System.currentTimeMillis();
        Map<String, List<String>> result = new HashMap<>();
        for (String line : slicedPerLine) {
            for (String talker : talkers) {
                if (line.contains(talker)) {
                    if (!result.containsKey(talker)) {
                        List<String> lineList = new ArrayList<>();
                        lineList.add(line);
                        result.put(talker, lineList);
                    } else {
                        result.get(talker).add(line);
                    }
                }
            } // talkers loop end
        } // line loop end
//        long end = System.currentTimeMillis();
//        log.info("mapTalkerToLine 수행 시간 = {}s", (end - start)/1000.0);
        return result;
    }

    /**
     해당 서비스 핵심 함수
     */
    public Map<String, List<String>> getTalkerToToken() throws IOException {
        // txt 파일을 열어서 라인별로 자른다
        List<String> slicedResult = slicePerLine();

        // txt 파일에서 대화자를 추출한다
        List<String> talkers = getTalkers();

        // 대화자(1) : 말한 단어(토큰화된 리스트) 를 매핑한다
        return mapTalkerToToken(talkers, slicedResult);
    }

    public Map<String, List<String>> getTalkerToLine() throws IOException {
        // txt 파일을 열어서 라인별로 자른다
        List<String> slicedResult = slicePerLine();

        // txt 파일에서 대화자를 추출한다
        List<String> talkers = getTalkers();

        return mapTalkerToLine(talkers, slicedResult);
    }
}
