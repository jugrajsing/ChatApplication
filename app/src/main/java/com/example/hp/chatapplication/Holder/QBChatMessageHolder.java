package com.example.hp.chatapplication.Holder;

import android.support.v4.util.Pools;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QBChatMessageHolder {
    private static QBChatMessageHolder instance;
    private HashMap<String , ArrayList<QBChatMessage>> qbChatMessageArray;

    public static synchronized QBChatMessageHolder getInstance(){

        QBChatMessageHolder qbChatMessageHolder;
        synchronized (QBChatMessageHolder.class){
            if (instance==null)
                instance=new QBChatMessageHolder();
            qbChatMessageHolder=instance;
        }
        return qbChatMessageHolder;
    }

    private QBChatMessageHolder(){
        this.qbChatMessageArray=new HashMap<>();
    }
    public void putMessages(String dialogId,ArrayList<QBChatMessage>qbChatMessages){
        this.qbChatMessageArray.put(dialogId,qbChatMessages);

    }
    public void putMessage(String dialogId,QBChatMessage qbChatMessage){

        List<QBChatMessage>listResult=(List)this.qbChatMessageArray.get(dialogId);
        listResult.add(qbChatMessage);
        ArrayList<QBChatMessage>lstAdded=new ArrayList<>(listResult.size());
        lstAdded.addAll(listResult);
        putMessages(dialogId,lstAdded);

    }
    public ArrayList<QBChatMessage>getChatMessageByDialogId(String dialogId){

        return (ArrayList<QBChatMessage>)this.qbChatMessageArray.get(dialogId);
    }
}

