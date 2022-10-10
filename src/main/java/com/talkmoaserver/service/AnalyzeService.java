package com.talkmoaserver.service;

import com.talkmoaserver.dto.FrequencyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyzeService {

    /**
     * 1. 대화자 추출 -> 대화자 목록과 각기 대화한 횟수 분석
     */
    public List<FrequencyResult> calcTotal(Map<String, List<String>> talkerToWords) {
        List<FrequencyResult> result = new ArrayList<>();
        for (String talker : talkerToWords.keySet())
            result.add(new FrequencyResult(talker, countTalkingInList(talkerToWords.get(talker))));
        return result;
    }

    // 대화횟수를 세주는 함수
    private int countTalkingInList(List<String> talkingList){
        int talkCounter = 0;
        for (String talk : talkingList) {
            if (!talk.equals("사진") && !talk.equals("이모티콘") && !talk.equals("동영상") && !talk.contains("파일:")) {
                if ((talk.contains("오전") || talk.contains("오후")) && talk.contains(":") && talk.length() == 8) continue;
                talkCounter++;
            }
        }
        return talkCounter;
    }

    /**
     * 2. 미디어(사진, 동영상, 첨부파일)를 주고받은 횟수 추출 -> 대화자 : 횟수
     */
    public List<FrequencyResult> calcMedia(Map<String, List<String>> talkerToWords) {
        List<FrequencyResult> result = new ArrayList<>();
        for (String talker : talkerToWords.keySet()) {
            List<String> talkingList = talkerToWords.get(talker);
            result.add(new FrequencyResult(talker, countMediaInList(talkingList)));
        }
        return result;
    }

    //미디어 개수를 세주는 함수
    private int countMediaInList(List<String> talkingList){
        int mediaCounter = 0;
        for (String talk : talkingList) {
            if (talk.equals("사진") || talk.equals("동영상") || talk.contains("파일:"))
                mediaCounter++;
        }
        return mediaCounter;
    }

    /**
     * 3. 이모티콘 주고받은 횟수 -> 대화자 : 횟수
     */
    public List<FrequencyResult> calcEmoji(Map<String, List<String>> talkerToWords) {
        List<FrequencyResult> result = new ArrayList<>();
        for (String talker : talkerToWords.keySet()) {
            List<String> talkingList = talkerToWords.get(talker);
            int emojiCounter = 0;
            for (String talk : talkingList) if (talk.equals("이모티콘")) emojiCounter++;
            result.add(new FrequencyResult(talker, emojiCounter));
        }
        return result;
    }

    /**
     * 4. 가장 조용한 시간 찾기 -> 말 횟수가 가장 적은 시간대 1시간 간격으로 순위 매겨서 분석
     * 5. 가장 활발한 시간 찾기 -> 말 횟수가 가장 많은 시간대 1시간 간격으로 순위 매겨서 분석
     * 둘 이 똑같은 로직인데 정렬 순서만 변경되는 것 이므로 type 으로 4, 5 인지 구분해서 정렬해서 리턴하도록 구현
     */
    public List<FrequencyResult> calcTime(Map<String, List<String>> talkerToLine, String type) {
        Map<Integer, Integer> talkingTimeMap = new HashMap<>();
        for (String talker : talkerToLine.keySet()) {
            List<String> timeList = talkerToLine.get(talker);
            countTalkingTime(talkingTimeMap, timeList);
        }
        return switch (type) {
            case "high" -> makeAscendingOrderMap(talkingTimeMap);
            case "low" -> makeDescendingOrderMap(talkingTimeMap);
            default -> null;
        };
    }

    // 대화한 시간을 세서 map 에 넣어주는 함수
    private void countTalkingTime(Map<Integer, Integer> talkingTimeMap, List<String> talkingList) {
        for (String talk : talkingList) {
            if ((talk.contains("오전") || talk.contains("오후")) && talk.contains(":")) {
                int time = Integer.parseInt(talk.substring(3, 5));
                if (talk.contains("오전")) setTime(talkingTimeMap, time);
                else if (talk.contains("오후")) setTime(talkingTimeMap, time + 12);
            }
        }
    }

    //주어진 시간이 map 에 있으면 1올리고 없으면 새로 넣는 함수
    private void setTime(Map<Integer, Integer> talkingTimeMap, int time) {
        if (talkingTimeMap.containsKey(time))
            talkingTimeMap.put(time, talkingTimeMap.get(time) + 1);
        else
            talkingTimeMap.put(time, 1);
    }

    // 오름차순으로 분석한 맵을 정렬해주는 함수
    private List<FrequencyResult> makeAscendingOrderMap(Map<Integer, Integer> talkingTimeMap){
        List<FrequencyResult> result = new ArrayList<>();
        List<Integer> keySetList = new ArrayList<>(talkingTimeMap.keySet());

        keySetList.sort(Comparator.comparing(talkingTimeMap::get));

        for (Integer time : keySetList)
            result.add(new FrequencyResult(Integer.toString(time), talkingTimeMap.get(time)));

        return result;
    }

    // 내림차순으로 분석한 맵을 정렬해주는 함수
    private List<FrequencyResult> makeDescendingOrderMap(Map<Integer, Integer> talkingTimeMap){
        List<FrequencyResult> result = new ArrayList<>();
        List<Integer> keySetList = new ArrayList<>(talkingTimeMap.keySet());

        keySetList.sort((value1, value2) -> (talkingTimeMap.get(value2).compareTo(talkingTimeMap.get(value1))));

        for (Integer time : keySetList)
            result.add(new FrequencyResult(Integer.toString(time), talkingTimeMap.get(time)));

        return result;
    }
}
