package com.talkmoaserver.service;

import com.talkmoaserver.dto.FrequencyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private List<FrequencyResult> convertDto(List<FrequencyResult> result) {
        return result;
    }

    /**
     * 1. 대화자 추출 -> 대화자 목록과 각기 대화한 횟수 분석
     */
    public List<FrequencyResult> calcTotal(Map<String, List<String>> talkerToWords) {
        List<FrequencyResult> result = new ArrayList<>();

        for(String talker : talkerToWords.keySet()){
            result.add(new FrequencyResult(talker, countTalkingInList(talkerToWords.get(talker))));
        }

        return convertDto(result);
    }
//    public Map<String, Integer> countTalkingToMap(Map<String, List<String>> talkerToWords){
//        Map<String, Integer> talkermap = new HashMap<>();
//
//        for(String talker : talkerToWords.keySet()){
//            int talkingcount = talkermap.get(talker);
//            if(talkingcount == 0){
//                talkermap.put(talker,1);
//            }else{
//                talkermap.put(talker, talkingcount);
//            }
//        }
//        return talkermap;
//    }
    //대화횟수를 세주는 함수
    private int countTalkingInList(List<String> talkinglist){
        int talkcounter = 0;
        for(String talk : talkinglist){
            if(!talk.equals("사진") && !talk.equals("이모티콘") && !talk.equals("동영상") && !talk.contains("파일:") ){
                if((talk.contains("오전") || talk.contains("오후")) && talk.contains(":") && talk.length() == 8)continue;
                talkcounter++;
            }
        }

        return talkcounter;
    }


    /**
     * 2. 미디어(사진, 동영상, 첨부파일)를 주고받은 횟수 추출 -> 대화자 : 횟수
     */
    public List<FrequencyResult> calcMedia(Map<String, List<String>> talkerToWords) {
        // 노경현 : 3430 // (사진, 동영상, 첨부파일)
        List<FrequencyResult> result = new ArrayList<>();
        for(String talker : talkerToWords.keySet()){
            List<String> talkinglist = talkerToWords.get(talker);

            result.add(new FrequencyResult(talker, countMediaInList(talkinglist)));
        }

        return convertDto(result);
    }
    //미디어 개수를 세주는 함수
    private int countMediaInList(List<String> talkinglist){
        int mediacounter = 0;
        for(String talk : talkinglist){
            if(talk.equals("사진") || talk.equals("동영상") || talk.contains("파일:") ){
                mediacounter++;
            }
        }
        return mediacounter;
    }

    /**
     * 3. 이모티콘 주고받은 횟수 -> 대화자 : 횟수
     */
    public List<FrequencyResult> calcEmoji(Map<String, List<String>> talkerToWords) {
        List<FrequencyResult> result = new ArrayList<>();
        for(String talker : talkerToWords.keySet()){
            List<String> talkinglist = talkerToWords.get(talker);

            result.add(new FrequencyResult(talker, countEmojiInList(talkinglist)));
        }

        return convertDto(result);
    }
    //이모티콘 개수를 세주는 함수
    private int countEmojiInList(List<String> talkinglist){
        int imoticoncounter = 0;
        for(String talk : talkinglist){
            if(talk.equals("이모티콘")){
                imoticoncounter++;
            }
        }
        return imoticoncounter;
    }

    /**
     * 4. 가장 조용한 시간 찾기 -> 말 횟수가 가장 적은 시간대 1시간 간격으로 순위 매겨서 분석
     * 5. 가장 활발한 시간 찾기 -> 말 횟수가 가장 많은 시간대 1시간 간격으로 순위 매겨서 분석
     * 둘 이 똑같은 로직인데 정렬 순서만 변경되는 것 이므로 type 으로 4, 5 인지 구분해서 정렬해서 리턴하도록 구현
     */
    public List<FrequencyResult> calcTime(Map<String, List<String>> talkerToWords, String type) {
        List<FrequencyResult> result = new ArrayList<>();
        Map<Integer, Integer> talkingtimemap = new HashMap<>();

        for (String talker : talkerToWords.keySet()) {
            List<String> timelist = talkerToWords.get(talker);
            talkingtimemap = countTalkingTime(talkingtimemap, timelist);
        }

        if (type.equals("활발한")) {
            result = makeAscendingOrderTimeMap(talkingtimemap);
        } else if (type.equals("조용한")) {
            result = makeDescendingOrderTimeMap(talkingtimemap);
        }

        return convertDto(result);

    }

    //대화한 시간을 세서 map에 넣어주는 함수
    private Map<Integer, Integer> countTalkingTime (Map<Integer, Integer> talkingtimemap, List<String> talkinglist){

        for(String talk : talkinglist){
            if(talk.length() == 8 && talk.contains(":")){
                int time = Integer.parseInt(talk.substring(3,5));
                if(talk.contains("오전")){
                    talkingtimemap = setTime(talkingtimemap, time);
                }else if(talk.contains("오후")){
                    talkingtimemap = setTime(talkingtimemap, time + 12);
                }
            }else {
                continue;
            }
        }

        return talkingtimemap;
    }
    //주어진 시간이 map에 잇으면 1올리고 없으면 새로 넣는 함수
    private Map<Integer, Integer>  setTime (Map<Integer, Integer> talkingtimemap, int time ){
        if(talkingtimemap.containsKey(time)){
            talkingtimemap.put(time, talkingtimemap.get(time) + 1);
        }else {
            talkingtimemap.put(time, 1);
        }
        return talkingtimemap;
    }
    //오름차순으로 분석한 시간 맵을 정렬해주는 함수
    private List<FrequencyResult> makeAscendingOrderTimeMap(Map<Integer, Integer> talkingtimemap){
        List<FrequencyResult> result = new ArrayList<>();
        List<Integer> keysetlist = new ArrayList<>(talkingtimemap.keySet());


        Collections.sort(keysetlist, (value1, value2) -> (talkingtimemap.get(value1).compareTo(talkingtimemap.get(value2))));

        Iterator<Integer> talkingkeysiterator = keysetlist.iterator();
        while(talkingkeysiterator.hasNext()){
            Integer time = talkingkeysiterator.next();
            result.add(new FrequencyResult(Integer.toString(time), talkingtimemap.get(time)));
        }
        return result;
    }
    //내림차순으로 분석한 시간 맵을 정렬해주는 함수
    private List<FrequencyResult> makeDescendingOrderTimeMap(Map<Integer, Integer> talkingtimemap){
        List<FrequencyResult> result = new ArrayList<>();
        List<Integer> keysetlist = new ArrayList<>(talkingtimemap.keySet());

        Collections.sort(keysetlist, (value1, value2) -> (talkingtimemap.get(value2).compareTo(talkingtimemap.get(value1))));

        Iterator<Integer> talkingkeysiterator = keysetlist.iterator();
        while(talkingkeysiterator.hasNext()){
            Integer time = talkingkeysiterator.next();
            result.add(new FrequencyResult(Integer.toString(time), talkingtimemap.get(time)));
        }
        return result;
    }



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
