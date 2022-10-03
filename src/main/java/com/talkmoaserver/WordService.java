package com.talkmoaserver;

import com.talkmoaserver.entity.ChatRoom;
import com.talkmoaserver.repository.ChatRoomRepository;
import com.talkmoaserver.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 단어 분석 등 핵심적인 비즈니스 로직을 담당하는 레이어
 */
@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    private final ChatRoomRepository chatRoomRepository;
    private static long sequence = 0L;



    // 1. txt파일을 읽어서 대화내역을 받으면 ChatRoom 객체로 만들어서 저장하는 함수
    public ChatRoom storeFileToChat(@RequestParam("file")MultipartFile mutifile) throws IOException {
        File file = new File(mutifile.getOriginalFilename());
        mutifile.transferTo(file);

        StringBuffer strbuffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        //방 이름 가져오기
        String roomname = reader.readLine();

        // 처음에 날짜가 있어서 넘김
        String empty = reader.readLine();
        empty = reader.readLine();


        String[] list = new String[]{};
        int coun = 0;
        while (strbuffer.append(reader.readLine())!=null){
            //파일을 읽으며 문장 배열을 만듬
            String str = strbuffer.toString();
            list[coun++]=str;
        }
        reader.close();

        ChatRoom chatRoom = new ChatRoom(sequence++,roomname, storeTalkTolist(list,coun));
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }


    // 2. 받은 대화 내역을 일단 배열에 저장해서 돌려주는 함수
    int charcount = 0; //단어를 세는 횟수를 저장
    public List storeTalkTolist(String[] line, int num){
        HashMap<String, Integer>talkerMap = new HashMap<>();
        HashMap<String, Integer>talkingtimeMap = new HashMap<>();
        List<String> talk = new ArrayList<>();

        for(int b=0; b<num; b++){
            char[] arr = line[b].toCharArray();
            if(arr[0]=='-')continue;
            if(arr[0]!='['){
                talk.add(line[b]);
                continue;
            }
            charcount = 0;
            talkerMap = saveTalkerToMap(arr);

            charcount+=2;
            talkingtimeMap = saveTalkerToMap(arr);

            //대화 내역 저장
            charcount+=2;
            char[] talking = new char[arr.length+1];
            for(int a=0; charcount < arr.length; a++){
                talking[a] = arr[charcount++];
            }
            talk.add(String.valueOf(talking));

        }

        return talk;
    }

    private HashMap saveTalkerToMap(char[] arr){
        HashMap<String, Integer>talkerMap = new HashMap<>();
        //대화자 저장
        char[] name = new char[arr.length+1];
        for(int a=0; arr[charcount]!=']'; a++){
            name[a] = arr[charcount++];
        }
        if(!talkerMap.containsKey(String.valueOf(name))){
            talkerMap.put(String.valueOf(name),1);
        }else{
            talkerMap.put(String.valueOf(name), talkerMap.get(String.valueOf(name))+1);
        }
        return talkerMap;
    }

    private HashMap saveTalkingTimeToMap(char[] arr){
        HashMap<String, Integer>talkingtimeMap = new HashMap<>();
        //대화 시간 저장
        char[] talkingtime = new char[arr.length+1];
        for(int a=0; arr[charcount]!=']'; a++){
            talkingtime[a] = arr[charcount++];
        }
        if(!talkingtimeMap.containsKey(String.valueOf(talkingtime))){
            talkingtimeMap.put(String.valueOf(talkingtime),1);
        }else{
            talkingtimeMap.put(String.valueOf(talkingtime), talkingtimeMap.get(String.valueOf(talkingtime))+1);
        }
        return talkingtimeMap;
    }



    // 3. 그 배열을 돌면서 해시맵에 단어 : 빈도수로 매핑시켜서 돌려주는 함수
    public HashMap mappingWordInMap(ArrayList<String> wordlist){
        HashMap<String, Integer>analyzedResultMap = new HashMap<>();

        while (!wordlist.isEmpty()){
            String str = (String) wordlist.remove(0);

            if(!analyzedResultMap.containsKey(str)){
                analyzedResultMap.put(str,1);
            }else{
                analyzedResultMap.put(str, analyzedResultMap.get(str)+1);
            }
        }
        return analyzedResultMap;
    }


}
