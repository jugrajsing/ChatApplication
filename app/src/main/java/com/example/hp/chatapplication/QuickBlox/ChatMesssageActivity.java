package com.example.hp.chatapplication.QuickBlox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.hp.chatapplication.Adapter.ChatMessageAdapter;
import com.example.hp.chatapplication.Common.Common;
import com.example.hp.chatapplication.Holder.QBChatMessageHolder;
import com.example.hp.chatapplication.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import org.jivesoftware.smack.SmackException;
import java.util.ArrayList;


public class ChatMesssageActivity extends AppCompatActivity {

    QBChatDialog qbChatDialog;
    ListView lstChatMessages;
    ImageButton submitt_button;
    EditText edt_content;
    ChatMessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messsage);
        initViews();
        initchatDialigs();
        retriveAllMessages();


        submitt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QBChatMessage chatMessage= new QBChatMessage();
                chatMessage.setBody(edt_content.getText().toString());
                chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                chatMessage.setSaveToHistory(true);

                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }

                //put messsages to the cache

                QBChatMessageHolder.getInstance().putMessage( qbChatDialog.getDialogId(),chatMessage);
                ArrayList<QBChatMessage>messages=QBChatMessageHolder.getInstance().getChatMessageByDialogId(qbChatDialog.getDialogId());
                adapter=new ChatMessageAdapter(getBaseContext(),messages);
                lstChatMessages.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                //remove text from edit text

                edt_content.setText("");
                edt_content.setFocusable(true);

            }
        });
    }

    private void initViews() {
        lstChatMessages=(ListView)findViewById(R.id.list_of_message);
        submitt_button=(ImageButton)findViewById(R.id.send_button);
        edt_content=(EditText)findViewById(R.id.edt_content);

    }

    private void initchatDialigs(){

        qbChatDialog=(QBChatDialog)getIntent().getSerializableExtra(Common.DIALOG_EXTRA);
        qbChatDialog.initForChat(QBChatService.getInstance());
        //registre lister in coming messages
        QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(
                new QBChatDialogMessageListener() {
                    @Override
                    public void processMessage(String dialogId, QBChatMessage message, Integer senderId) {

                    }

                    @Override
                    public void processError(String dialogId, QBChatException exception, QBChatMessage message, Integer senderId) {

                    }
                });

        qbChatDialog.addMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                QBChatMessageHolder.getInstance().putMessage(qbChatMessage.getDialogId(),qbChatMessage);

                ArrayList<QBChatMessage>messages=QBChatMessageHolder.getInstance().getChatMessageByDialogId(qbChatMessage.getDialogId());
                adapter=new ChatMessageAdapter(getBaseContext(),messages);
                lstChatMessages.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Log.e("Error",e.getMessage());
            }
        });


    }

    private void retriveAllMessages() {

        QBMessageGetBuilder messageGetBuilder=new QBMessageGetBuilder();
        messageGetBuilder.setLimit(500);

        if (qbChatDialog!=null){
            QBRestChatService.getDialogMessages(qbChatDialog,messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    //put messages to cache
                    QBChatMessageHolder.getInstance().putMessages( qbChatDialog.getDialogId(),qbChatMessages);

                    adapter=new ChatMessageAdapter(getBaseContext(),qbChatMessages);
                    lstChatMessages.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });

        }
    }

}
