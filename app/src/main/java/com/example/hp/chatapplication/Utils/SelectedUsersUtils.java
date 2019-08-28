package com.example.hp.chatapplication.Utils;

import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.Collection;

public class SelectedUsersUtils {
    public static ArrayList<Integer> getIdsSelectedOpponents(Collection<User> selectedUsers) {
        ArrayList<Integer> opponentsIds = new ArrayList<>();
        if (!selectedUsers.isEmpty()) {
            for (User qbUser : selectedUsers) {
                opponentsIds.add(Integer.valueOf(qbUser.getUserId()));
            }
        }

        return opponentsIds;
    }

}
