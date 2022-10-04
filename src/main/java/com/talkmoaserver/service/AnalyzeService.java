package com.talkmoaserver.service;

import com.talkmoaserver.dto.FrequencyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 가공된 단어 목록을 기능별로 분석해주는 서비스
 * 1. 대화자 추출 -> 대화자 목록과 각기 대화한 횟수 분석
 * 2. 미디어(사진, 동영상, 첨부파일)를 주고받은 횟수 추출 -> 대화자 : 횟수
 * 3. 이모티콘 주고받은 횟수 -> 대화자 : 횟수
 * 4. 가장 조용한 시간 찾기 -> 말 횟수가 가장 적은 시간대 1시간 간격으로 순위 매겨서 분석
 * 5. 가장 활발한 시간 찾기 -> 말 횟수가 가장 많은 시간대 1시간 간격으로 순위 매겨서 분석
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyzeService {

    // 컨트롤러로 전달할 DTO 를 만들어주는 메소드 파라미터로 무얼 받아야할 지는 구현하면서 고민해 볼 것
    private List<FrequencyResult> convertDto() {
        List<FrequencyResult> data = new ArrayList<>();
        return data;
    }

    /**
     * 1. 대화자 추출 -> 대화자 목록과 각기 대화한 횟수 분석
     */
    public List<FrequencyResult> calcTotal(Map<String, List<String>> talkerToWords) {
        return convertDto();
    }

    /**
     * 2. 미디어(사진, 동영상, 첨부파일)를 주고받은 횟수 추출 -> 대화자 : 횟수
     */
    public List<FrequencyResult> calcMedia(Map<String, List<String>> talkerToWords) {
        return convertDto();
    }

    /**
     * 3. 이모티콘 주고받은 횟수 -> 대화자 : 횟수
     */
    public List<FrequencyResult> calcEmoji(Map<String, List<String>> talkerToWords) {
        return convertDto();
    }

    /**
     * 4. 가장 조용한 시간 찾기 -> 말 횟수가 가장 적은 시간대 1시간 간격으로 순위 매겨서 분석
     * 5. 가장 활발한 시간 찾기 -> 말 횟수가 가장 많은 시간대 1시간 간격으로 순위 매겨서 분석
     * 둘 이 똑같은 로직인데 정렬 순서만 변경되는 것 이므로 type 으로 4, 5 인지 구분해서 정렬해서 리턴하도록 구현
     */
    public List<FrequencyResult> calcTime(Map<String, List<String>> talkerToWords, String type) {
        return convertDto();
    }




//
//    // 1. txt 파일을 읽어서 대화내역을 받으면 ChatRoom 객체로 만들어서 저장하는 함수
//    public ChatRoom storeFileToChat(MultipartFile multipartFile) throws IOException {
//        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        multipartFile.transferTo(file);
//
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//
//        // 방 이름 가져오기
//        String roomName = reader.readLine();
//
//        // 처음에 날짜가 있어서 넘김
//        reader.readLine();
//        reader.readLine();
//
//        String[] list = new String[]{};
//        int count = 0;
//        while (sb.append(reader.readLine()) != null){
//            //파일을 읽으며 문장 배열을 만듬
//            String str = sb.toString();
//            list[count++] = str;
//        }
//
//        reader.close();
//
//        ChatRoom chatRoom = new ChatRoom(roomName, storeTalkToList(list, coun));
//        chatRoomRepository.save(chatRoom);
//        return chatRoom;
//    }

    // 2. 받은 대화 내역을 일단 배열에 저장해서 돌려주는 함수
//    int charCount = 0; //단어를 세는 횟수를 저장
//    public List<String> storeTalkToList(String[] line, int num){
//        HashMap<String, Integer> talkerMap = new HashMap<>();
//        HashMap<String, Integer> talkingTimeMap = new HashMap<>();
//        List<String> talk = new ArrayList<>();
//
//        for (int b = 0 ; b < num ; b++) {
//            char[] arr = line[b].toCharArray();
//            if (arr[0] == '-') continue;
//            if (arr[0] != '[') {
//                talk.add(line[b]);
//                continue;
//            }
//            charCount = 0;
//            talkerMap = extractTalker(arr);
//
//            charCount +=2;
//            talkingTimeMap = extractTalker(arr);
//
//            //대화 내역 저장
//            charCount +=2;
//            char[] talking = new char[arr.length+1];
//            for(int a = 0 ; charCount < arr.length ; a++){
//                talking[a] = arr[charCount++];
//            }
//            talk.add(String.valueOf(talking));
//        }
//
//        return talk;
//    }

    //        HashMap<String, Integer> talkerMap = new HashMap<>();
//        //대화자 저장
//        char[] name = new char[arr.length+1];
//        for(int a = 0; arr[charCount]!=']'; a++){
//            name[a] = arr[charCount++];
//        }
//        if(!talkerMap.containsKey(String.valueOf(name))){
//            talkerMap.put(String.valueOf(name),1);
//        }else{
//            talkerMap.put(String.valueOf(name), talkerMap.get(String.valueOf(name))+1);
//        }
//        return talkerMap;

//    private HashMap saveTalkingTimeToMap(char[] arr){
//        HashMap<String, Integer>talkingtimeMap = new HashMap<>();
//        //대화 시간 저장
//        char[] talkingtime = new char[arr.length+1];
//        for(int a = 0; arr[charCount]!=']'; a++){
//            talkingtime[a] = arr[charCount++];
//        }
//        if(!talkingtimeMap.containsKey(String.valueOf(talkingtime))){
//            talkingtimeMap.put(String.valueOf(talkingtime),1);
//        }else{
//            talkingtimeMap.put(String.valueOf(talkingtime), talkingtimeMap.get(String.valueOf(talkingtime))+1);
//        }
//        return talkingtimeMap;
//    }

    // 3. 그 배열을 돌면서 해시맵에 단어 : 빈도수로 매핑시켜서 돌려주는 함수
    public HashMap<String, Integer> calculateWordFrequency(ArrayList<String> wordList) {
        HashMap<String, Integer> analyzedResult = new HashMap<>();
        wordList.forEach(word -> {
            if(!analyzedResult.containsKey(word)) {
                analyzedResult.put(word, 1);
            } else {
                analyzedResult.put(word, analyzedResult.get(word)+1);
            }
        });
        return analyzedResult;
    }




}
