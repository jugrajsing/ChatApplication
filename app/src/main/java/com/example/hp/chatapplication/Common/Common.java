package com.example.hp.chatapplication.Common;

import com.example.hp.chatapplication.Holder.QBUsersHolder;
import com.quickblox.users.model.QBUser;

import java.util.List;

public class Common {

    public static final String DIALOG_EXTRA = "Dialog";

    public static String createChatDialogName(List<Integer> qbUsers) {

        List<QBUser> qbUsers1 = QBUsersHolder.getInstance().getUsersByIds(qbUsers);
        StringBuilder name = new StringBuilder();

        for (QBUser user : qbUsers1)
            name.append(user.getFullName()).append(" ");

        if (name.length() > 30)
            name = name.replace(30, name.length() - 1, "...");
        return name.toString();
    }
}
