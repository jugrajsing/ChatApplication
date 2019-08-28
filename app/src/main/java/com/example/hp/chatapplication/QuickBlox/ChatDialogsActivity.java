package com.example.hp.chatapplication.QuickBlox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hp.chatapplication.Adapter.ChatDialogsAdapter;
import com.example.hp.chatapplication.Common.Common;
import com.example.hp.chatapplication.Holder.QBUsersHolder;
import com.example.hp.chatapplication.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ChatDialogsActivity extends AppCompatActivity {

    ListView list_chat_dialogs;
    FloatingActionButton chatdialog_adduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialogs);

        list_chat_dialogs = (ListView) findViewById(R.id.list_chat_dialogs);
        list_chat_dialogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog qbChatDialog = (QBChatDialog) list_chat_dialogs.getAdapter().getItem(position);
                Intent intent = new Intent(ChatDialogsActivity.this, ChatMesssageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA, qbChatDialog);
                startActivity(intent);

            }
        });
        chatdialog_adduser = (FloatingActionButton) findViewById(R.id.chatdialog_adduser);
        chatdialog_adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDialogsActivity.this, ListUsersActivity.class);
                startActivity(intent);
            }
        });

        createSessionForChat();
        loadChatDialogs();

    }

    @Override
    protected void onResume() {
        loadChatDialogs();
        super.onResume();
    }

    private void loadChatDialogs() {
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(100);

        QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(
                new QBEntityCallback<ArrayList<QBChatDialog>>() {
                    @Override
                    public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle params) {
                        ChatDialogsAdapter adapter = new ChatDialogsAdapter(getBaseContext(), qbChatDialogs);
                        list_chat_dialogs.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(QBResponseException e) {

                        Log.e("Error", e.getMessage());
                    }
                });
    }

    private void createSessionForChat() {
        final ProgressDialog progressDialog = new ProgressDialog(ChatDialogsActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        String user_name, password;
        user_name = getIntent().getStringExtra("user");
        password = getIntent().getStringExtra("password");


        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUsersHolder.getInstance().putUsers(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });


        final QBUser qbUser = new QBUser(user_name, password);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ERROR", e.getMessage());
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });

    }
}
