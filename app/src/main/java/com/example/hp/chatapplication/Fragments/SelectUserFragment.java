package com.example.hp.chatapplication.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.hp.chatapplication.Adapter.SelectableUserListAdapter;
import com.example.hp.chatapplication.R;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectUserFragment extends Fragment{

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    public SelectableUserListAdapter mListAdapter;

    public UserListQuery mUserListQuery;
    public UsersSelectedListener mListener;
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 :- for private mode
    SharedPreferences.Editor editor = pref.edit();
   // ArrayList<Integer> selectedIds;


    // To pass selected user IDs to the parent Activity.
    public interface UsersSelectedListener {
        void onUserSelected(boolean selected, String userId);
    }

   public static SelectUserFragment newInstance() {
        SelectUserFragment fragment = new SelectUserFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_select_user, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_select_user);
        mListAdapter = new SelectableUserListAdapter(getActivity(), false, true);


        mListAdapter.setItemCheckedChangeListener(new SelectableUserListAdapter.OnItemCheckedChangeListener() {
            @Override
            public void OnItemChecked(User user, boolean checked) {
                if (checked) {
                    mListener.onUserSelected(true, user.getUserId());
                 //   Toast.makeText(getContext(), ""+user.getUserId(), Toast.LENGTH_SHORT).show();
                   // selectedIds=new ArrayList<>();
                   // for (int i=0;i<)
                  //  selectedIds.add(user.getUserId())


                 //   editor.putString("opponents_key",user.getUserId());

                } else {
                    mListener.onUserSelected(false, user.getUserId());

                    //Toast.makeText(getContext(), ""+user.getUserId(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mListener = (UsersSelectedListener) getActivity();

        setUpRecyclerView();

        loadInitialUserList(15);

       // ((CreateGroupChannelActivity) getActivity()).setState(CreateGroupChannelActivity.STATE_SELECT_USERS);

        return rootView;
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mListAdapter.getItemCount() - 1) {
                    loadNextUserList(10);
                }
            }
        });
    }

    /**
     * Replaces current user list with new list.
     * Should be used only on initial load.
     */
    private void loadInitialUserList(int size) {
        mUserListQuery = SendBird.createUserListQuery();

        mUserListQuery.setLimit(size);
        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                mListAdapter.setUserList(list);
            }
        });
    }

    private void loadNextUserList(int size) {
        mUserListQuery.setLimit(size);

        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                for (User user : list) {
                    mListAdapter.addLast(user);
                }
            }
        });
    }
}