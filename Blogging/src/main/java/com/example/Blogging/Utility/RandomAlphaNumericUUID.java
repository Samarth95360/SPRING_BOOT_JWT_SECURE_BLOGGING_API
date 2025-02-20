package com.example.Blogging.Utility;

import java.util.UUID;

public class RandomAlphaNumericUUID {

    public static String addSalt(String userId){
        int counter = 0;
        for(char x : userId.toCharArray()){
            if(x == '-'){
                counter++;
            }
        }
        if(counter == 0){
            return userId;
        }
        return addData(userId,counter);
    }

    private static String addData(String userId, int counter) {
        String[] randomData = new String[counter];
        for(int i=0;i<counter;i++){
            randomData[i] = UUID.randomUUID().toString().replaceAll("-","").substring(0,5);
        }
        String[] uuidSplit = userId.split("-");
        StringBuilder enhanceId = new StringBuilder();
        for(int i=0;i< uuidSplit.length-1;i++){
            enhanceId.append(uuidSplit[i]).append("-").append(randomData[i]);
        }
        enhanceId.append(uuidSplit[uuidSplit.length-1]);
        return new String(enhanceId);
    }

    public static UUID removeData(String userId){
        String[] data = userId.split("-");
        StringBuilder builder = new StringBuilder();
        builder.append(data[0]).append("-");
        for(int i=1;i<data.length-1;i++){
            builder.append(data[i].substring(5)).append("-");
        }
        builder.append(data[data.length-1].substring(5));
        return UUID.fromString(new String(builder));
    }

}
