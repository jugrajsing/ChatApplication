package com.example.hp.chatapplication.QuickBlox;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.chatapplication.Adapter.ListUsersAdapter;
import com.example.hp.chatapplication.Common.Common;
import com.example.hp.chatapplication.Holder.QBUsersHolder;
import com.example.hp.chatapplication.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ListUsersActivity extends AppCompatActivity {
    ListView list_users;
    Button btn_create_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        retriveAllUsers();

        list_users = (ListView) findViewById(R.id.list_users);

        list_users.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        btn_create_chat = (Button) findViewById(R.id.btn_create_chat);

        btn_create_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countChoice = list_users.getCount();
                for (int i = 0; 1 < countChoice; i++) {
                    if (list_users.getCheckedItemPositions().size() == 1)
                        createPrivateChat(list_users.getCheckedItemPositions());
                    else if (list_users.getCheckedItemPositions().size() > 1)
                        createGroupChat(list_users.getCheckedItemPositions());
                    else
                        Toast.makeText(ListUsersActivity.this, "Please select friend to chat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createGroupChat(SparseBooleanArray checkedItemPositions) {

        ProgressDialog progressDialog = new ProgressDialog(ListUsersActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        int countChoice = list_users.getCount();

        ArrayList<Integer> occupantIdoList = new ArrayList<>();
        for (int i = 0; 1 < countChoice; i++) {
            if (checkedItemPositions.get(i)) {

                QBUser user = (QBUser) list_users.getItemAtPosition(i);
                occupantIdoList.add(user.getId());

            }

        }

        //create chat dialogs

        QBChatDialog dialog = new QBChatDialog();
        dialog.setName(Common.createChatDialogName(occupantIdoList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdoList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                Toast.makeText(ListUsersActivity.this, "Create chat dialog success fully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Error", e.getMessage());
            }
        });

    }

    private void createPrivateChat(SparseBooleanArray checkedItemPositions) {

        final ProgressDialog progressDialog = new ProgressDialog(ListUsersActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        int countChoice = list_users.getCount();
        for (int i = 0; 1 < countChoice; i++) {
            if (checkedItemPositions.get(i)) {

                QBUser user = (QBUser) list_users.getItemAtPosition(i);
                QBChatDialog dialog = DialogUtils.buildPrivateDialog(user.getId());

                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        progressDialog.dismiss();
                        Toast.makeText(ListUsersActivity.this, "Create Private chat dialog success fully", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error", e.getMessage());
                    }
                });


            }

        }


    }

    private void retriveAllUsers() {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                QBUsersHolder.getInstance().putUsers(qbUsers);

                ArrayList<QBUser> qbuserWithoutCurrent = new ArrayList<QBUser>();
                for (QBUser user : qbUsers) {
                    if (!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin())) ;
                    qbuserWithoutCurrent.add(user);

                }
                ListUsersAdapter adapter = new ListUsersAdapter(ListUsersActivity.this, qbuserWithoutCurrent);
                list_users.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("Error", e.getMessage());
            }
        });

    }
}
