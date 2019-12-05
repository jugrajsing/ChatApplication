package com.example.hp.chatapplication.Fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hp.chatapplication.Adapter.PostListAdapter;
import com.example.hp.chatapplication.Adapter.Social_userlistAdapter;
import com.example.hp.chatapplication.ImageCroperActivty;
import com.example.hp.chatapplication.Intefaces.LikeInterface;
import com.example.hp.chatapplication.Intefaces.OnBackPressedListener;
import com.example.hp.chatapplication.ModelClasses.PostListModel;
import com.example.hp.chatapplication.ModelClasses.Social_user_name;
import com.example.hp.chatapplication.MySingleTon;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.SharedPrefManager;
import com.example.hp.chatapplication.UserNavgation;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;
import com.example.hp.chatapplication.openchannel.OpenChannelListFragment;
import com.example.hp.chatapplication.openchannel.OpenChatFragment;
import com.kevalpatel2106.emoticongifkeyboard.EmoticonGIFKeyboardFragment;
import com.kevalpatel2106.emoticongifkeyboard.emoticons.Emoticon;
import com.kevalpatel2106.emoticongifkeyboard.emoticons.EmoticonSelectListener;
import com.kevalpatel2106.emoticongifkeyboard.gifs.Gif;
import com.kevalpatel2106.emoticongifkeyboard.gifs.GifSelectListener;
import com.kevalpatel2106.emoticonpack.android8.Android8EmoticonProvider;
import com.kevalpatel2106.gifpack.giphy.GiphyGifProvider;
import com.sendbird.android.SendBird;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SocialFragment extends Fragment {

    public static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;
    private static final int CAMERA_PIC_REQUEST = 100;
    String userId;
    ArrayList<PostListModel> postListModelArrayList;
    ArrayList<Social_user_name> social_user_names = new ArrayList<>();
    PostListAdapter postListAdapter;
    String content;
    boolean flag = true;
    ImageView imageview;
    CircleImageView profile_img;
    TextView user_name;
    String name;
    FrameLayout socialfragment;
    ProgressBar progress_social;
    ImageView iv_gallery, iv_camera, iv_sticker, iv_import;
    PopupWindow popupWindow;
    LinearLayout poplinear;
    ImageView dialog_imageview;
    FrameLayout post_keyboardcontainer;
    EmoticonGIFKeyboardFragment emoticonGIFKeyboardFragment;
    FrameLayout attachment_container;
    private EditText statusPost, statusTitle;
    private Button statusSend;
    private RecyclerView postRecycler, userlist_recycler;
    private RelativeLayout mRootLayout;
    private ImageView post_emoji_open_close_btn, noData;
    private ImageButton mUploadFileButton;
    /*ImageView iv_import;
     */

    public SocialFragment() {
        // Required empty public constructor
    }


    /**
     * To create an instance of this fragment, a Channel URL should be passed.
     */
    public static OpenChatFragment newInstance(@NonNull String channelUrl) {
        OpenChatFragment fragment = new OpenChatFragment();

        Bundle args = new Bundle();
        args.putString(OpenChannelListFragment.EXTRA_OPEN_CHANNEL_URL, channelUrl);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  mIMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ((UserNavgation) getActivity()).setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public void doBack() {
                    try {
                        if (attachment_container.getVisibility() == View.VISIBLE) {
                            attachment_container.setVisibility(View.GONE);
                        } else if (emoticonGIFKeyboardFragment.isAdded()) {
                            if (emoticonGIFKeyboardFragment.isOpen()) {
                                emoticonGIFKeyboardFragment.close();
                            } else {
                                //    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                //getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                // getActivity().finish();
                                //  getActivity().onBackPressed();
                                //  getFragmentManager().popBackStack(n,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                // getActivity().

                                //((UserNavgation) getActivity()).setOnBackPressedListener(null);
                            }
                        } else {

                            //  getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            //getActivity().finish();
                            // getActivity().finish();
                            //getActivity().onBackPressed();
                            //getActivity().onBackPressed();
                            // getActivity().moveTaskToBack(true);
                            // getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            ((UserNavgation) getActivity()).setOnBackPressedListener(null);
                        }
                    } catch (Exception e) {

                    }
                }
            });
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userId = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id().toString();
        name = SharedPrefManager.getInstance(getActivity()).getUser().getUser_name().toString();


        View view = inflater.inflate(R.layout.fragment_social, container, false);

        loadPost();
        loadImage();
        loadUserlist();
        post_keyboardcontainer = (FrameLayout) view.findViewById(R.id.postkeyboard_container);
//        noData = view.findViewById(R.id.noData);
        profile_img = (CircleImageView) view.findViewById(R.id.profile_img);
        mUploadFileButton = (ImageButton) view.findViewById(R.id.button_group_chat_upload);

        // iv_more=(ImageView)view.findViewById(R.id.iv_more);

        //user_name=(TextView)view.findViewById(R.id.user_name);
//        user_name.setText(name);
        progress_social = (ProgressBar) view.findViewById(R.id.progress_social);

        postRecycler = (RecyclerView) view.findViewById(R.id.postRecycler);
        userlist_recycler = (RecyclerView) view.findViewById(R.id.userlist_recycler);
        statusPost = (EditText) view.findViewById(R.id.statusPost);
        // statusTitle=(EditText) view.findViewById(R.id.statusTitle);
        statusSend = (Button) view.findViewById(R.id.statusSend);
        //   imageview=(ImageView)view.findViewById(R.id.iv_like);
        socialfragment = (FrameLayout) view.findViewById(R.id.socialfragment);
        iv_camera = (ImageView) view.findViewById(R.id.iv_camera);
        iv_gallery = (ImageView) view.findViewById(R.id.iv_gallery);
        attachment_container = (FrameLayout) view.findViewById(R.id.attachment_container);
        // iv_more=(ImageView)view.findViewById(R.id.iv_more);

        // iv_gallery=(ImageView)view.findViewById(R.id.iv_gallery);
        // iv_camera=(ImageView)view.findViewById(R.id.iv_image);
        //iv_sticker=(ImageView)view.findViewById(R.id.iv_sticker);

        iv_import = (ImageView) view.findViewById(R.id.iv_import);
        dialog_imageview = (ImageView) view.findViewById(R.id.dialog_imageview);


        iv_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupShow();
            }
        });
        statusPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emoticonGIFKeyboardFragment.isAdded()) {
                    emoticonGIFKeyboardFragment.close();
                } else if (attachment_container.getVisibility() == View.VISIBLE) {
                    attachment_container.setVisibility(View.GONE);
                }
            }
        });

        /*   social_user_namw=new ArrayList<>();
        social_user_namw.add(new Social_user_name(R.drawable.jack_ma,"Jack Ma"));
        social_user_namw.add(new Social_user_name(R.drawable.mukesh_ambani,"Mukesh Ambani"));
        social_user_namw.add(new Social_user_name(R.drawable.steve_jobs,"Steve Jobs"));
        social_user_namw.add(new Social_user_name(R.drawable.bill_gates,"Bill Gates"));*/
        // social_user_namw.add(new Social_user_name(R.drawable.mark_zuckerberg,"Mark Zuckerberg"));
        // userlist_recycler.setHasFixedSize(true);

        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        //  userlist_recycler.setLayoutManager(layoutManager1);
        //  Social_userlistAdapter social_userlistAdapter=new Social_userlistAdapter(social_user_namw,getActivity());
        //  userlist_recycler.setAdapter(social_userlistAdapter);

//hideSoftKeyboard();
        postRecycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        postRecycler.setLayoutManager(layoutManager);

        mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachment_container.setVisibility(View.VISIBLE);
                post_keyboardcontainer.setVisibility(View.GONE);
                //   keyboard_container.setVisibility(View.GONE);
                /* requestMedia();*/
            }
        });
    /*    // camera click
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachment_container.setVisibility(View.GONE);
                Intent intent=new Intent(getContext(),ImageCroperActivty.class);
                intent.putExtra("DATA", "One");
                startActivity(intent);
                popupWindow.dismiss();


               *//* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //   startActivity(cameraIntent);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);*//*

            }
        });*/

        // gallery click

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMedia();
                attachment_container.setVisibility(View.GONE);
            }
        });
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);*/
                Intent intent = new Intent(getContext(), ImageCroperActivty.class);
                intent.putExtra("DATA", "One");
                startActivity(intent);
                //   popupWindow.dismiss();


            }
        });


        statusSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = statusPost.getText().toString();
                if (!content.isEmpty()) {
                    sendPost();
                    // if (emoticonGIFKeyboardFragment.isOpen())
                    // emoticonGIFKeyboardFragment.close();
                /*    if(!statusTitle.getText().toString().isEmpty()) {
                        sendPost();
                    }
                    else {
                        Toast.makeText(getActivity(), "Please Enter Title", Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    Toast.makeText(getActivity(), "Please Enter status", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /// adding emoji and gif
        EmoticonGIFKeyboardFragment.EmoticonConfig emoticonConfig = new EmoticonGIFKeyboardFragment.EmoticonConfig()
                .setEmoticonProvider(Android8EmoticonProvider.create())
                /*  NOTE: The process of removing last character when user preses back space will handle
                  by library if your edit text is in focus.
                 */
                .setEmoticonSelectListener(new EmoticonSelectListener() {

                    @Override
                    public void emoticonSelected(Emoticon emoticon) {
                        //Do something with new emoticon.
                        //     Log.d(TAG, "emoticonSelected: " + emoticon.getUnicode());
                        //   editText.append(emoticon.getUnicode(),
                        //         editText.getSelectionStart(),
                        //       editText.getSelectionEnd());
                        statusPost.append(emoticon.getUnicode());

                    }

                    @Override
                    public void onBackSpace() {
                        emoticonGIFKeyboardFragment.close();
                        //Do something here to handle backspace event.
                        //The process of removing last character when user preses back space will handle
                        //by library if your edit text is in focus.
                    }
                });


        EmoticonGIFKeyboardFragment.GIFConfig gifConfig = new EmoticonGIFKeyboardFragment

        /*
          Here we are using GIPHY to provide GIFs. Create Giphy GIF provider by passing your key.
          It is required to set GIF provider before adding fragment into container.
         */
                .GIFConfig(GiphyGifProvider.create(getActivity(), "564ce7370bf347f2b7c0e4746593c179"))
                .setGifSelectListener(new GifSelectListener() {
                    @Override
                    public void onGifSelected(@NonNull Gif gif) {
                        //Do something with the selected GIF.
                        Log.d("GUF", "onGifSelected: " + gif.getGifUrl());
                        // sendPost(gif.getGifUrl());

                    }
                });


        emoticonGIFKeyboardFragment = EmoticonGIFKeyboardFragment
                .getNewInstance(post_keyboardcontainer, emoticonConfig, gifConfig);
        post_emoji_open_close_btn = (ImageView) view.findViewById(R.id.post_emoji_open_close_btn);
        //    ke.setVisibility(View.VISIBLE);
        post_keyboardcontainer.setVisibility(View.VISIBLE);
        attachment_container.setVisibility(View.GONE);
        //  attachment_container.setVisibility(View.GONE);
        post_emoji_open_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachment_container.setVisibility(View.GONE);
                post_keyboardcontainer.setVisibility(View.VISIBLE);
              /*  getActivity().getWindow().setSoftInputMode(
                        SOFT_INPUT_ADJUST_PAN);*/


                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

//Adding the keyboard fragment to keyboard_container.
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.postkeyboard_container, emoticonGIFKeyboardFragment)
                        .commit();
                emoticonGIFKeyboardFragment.open();
            }
        });

        return view;
    }

    private void requestMedia() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            Intent intent = new Intent();

            // Pick images or videos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType("*/*");
                String[] mimeTypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            } else {
                intent.setType("image/* video/*");
            }

            intent.setAction(Intent.ACTION_GET_CONTENT);

            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Media"), INTENT_REQUEST_CHOOSE_MEDIA);

            // Set this as false to maintain connection
            // even when an external Activity is started.
            SendBird.setAutoBackgroundDetection(false);
        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mRootLayout, "Storage access permissions are required to upload/download files.",
                    Snackbar.LENGTH_LONG)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            // Permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void loadUserlist() {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        // searchedUsersModelArrayList=new ArrayList<>();
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;

                        progressDialog.dismiss();
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("search_result");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject user_details = jsonArray.getJSONObject(i);
                                String name = user_details.optString("name");
                                // Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                                String user_img = user_details.optString("user_img");

                                // String mobileno = user_details.optString("mobileno");
                                // String f_id=user_details.optString("frnid");
                                // String gender = user_details.optString("gender");
                                // String secret_id = user_details.optString("secrate_id");
                                //   Toast.makeText(getContext(), ""+secret_id, Toast.LENGTH_SHORT).show();
                                //   String friend_status=user_details.optString("frnstatus");

                                //  social_user_names=new Social_user_name("","");
                                // social_user_names=new Social_user_name()
                                Social_user_name socialmodel = new Social_user_name(user_img, name);
                                social_user_names.add(socialmodel);
                                Social_userlistAdapter social_userlistAdapter = new Social_userlistAdapter(social_user_names, getContext());
                                final LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                userlist_recycler.setLayoutManager(layoutManager1);
                                //Social_userlistAdapter social_userlistAdapter=new Social_userlistAdapter(social_user_namw,getActivity());
                                userlist_recycler.setAdapter(social_userlistAdapter);
                                //adding the hero to searchedlIst

                                //rv_user_list.setAdapter(searchingDetailsAdapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                            noData.setVisibility(View.VISIBLE);
                            noData.setImageResource(R.drawable.no_internet_);
                            noData.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "searchfriends");
                logParams.put("searchkey", "");
                logParams.put("userid", userId);
                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

  /*  public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_like: {
             if (flag) {
                 loadPost();
                    imageview.setImageResource(R.mipmap.ic_heart_red);
                    flag = false;
             } else {
                     imageview.setImageResource(R.mipmap.ic_heart_white);
                    flag = true;
                    }
                return;
            }
        }
    }*/


    private void sendPost() {
        final String SENDPOST_URL = BaseUrl_ConstantClass.BASE_URL + "?";
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, SENDPOST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");

                            if (status.equals("true")) {
//                                statusTitle.setText("Enter Title");
                                statusPost.setText("Enter Post");
                                loadPost();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "addpost");
                logParams.put("userid", userId);
                logParams.put("post_title", "title");
                logParams.put("content", content);

                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

    private void loadPost() {

        final String POST_URL = BaseUrl_ConstantClass.BASE_URL + "?action=getpost";
        postListModelArrayList = new ArrayList<>();
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("post_result");
                            for (int i = 0; i <= jsonArray.length(); i++) {

                                JSONObject user_details = jsonArray.getJSONObject(i);
                                final String post_id = user_details.optString("post_id");
                                String post_title = user_details.optString("post_title");
                                String content = user_details.optString("content");
                                String postedby = user_details.optString("postedby");
                                String postedby_name = user_details.optString("postedby_name");
                                String post_likes = user_details.optString("post_likes");
                                String posted_date = user_details.optString("posted_date");
                                String post_profileimg = user_details.optString("user_img");
                                String you_liked = user_details.optString("you_liked");
                                String you_unliked = user_details.optString("you_unliked");
                                // getting user list
                                //  List<Social_user_name> userlist=new ArrayList<>();
                                //  Social_user_name social_user_name = new Social_user_name(postedby, post_profileimg);
                                //  userlist.add(social_user_name);

                                // Glide.with(getActivity()).load(post_profileimg).into(profile_img);

                             /*   if (you_liked.contains("Yes")){
                                    imageview.setBackgroundResource(R.mipmap.ic_heart_red);
                                }
                                else {
                                    imageview.setBackgroundResource(R.mipmap.ic_heart_white);
                                }*/
                                // Toast.makeText(getContext(), post_likes.toString(), Toast.LENGTH_SHORT).show();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = null;
                                try {
                                    date = sdf.parse(posted_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long millis = date.getTime();
                                String result = DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 0).toString();

                                PostListModel postListModel = new PostListModel(post_id, post_title, content, postedby, postedby_name, post_likes,you_unliked, result, post_profileimg, you_liked);
                                //adding the hero to searchedlIst
                                postListModelArrayList.add(postListModel);

                                postListAdapter = new PostListAdapter(postListModelArrayList, getActivity(), new LikeInterface() {
                                    @Override
                                    public void likePost(View view, int position) {
                                        PostListModel postListModel = postListModelArrayList.get(position);
                                        likeFragments(postListModel.getPost_id());
                                    } @Override
                                    public void unlikePost(View view, int position) {
                                        PostListModel postListModel = postListModelArrayList.get(position);
                                        dislikeFragments(postListModel.getPost_id());
                                    }
                                });
                                postRecycler.setAdapter(postListAdapter);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("userid", userId);
                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

    private void likeFragments(final String postId) {
        final String POST_URL = BaseUrl_ConstantClass.BASE_URL + "?";

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            loadPost();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "likepost");
                logParams.put("post_id", postId);
                logParams.put("userid", userId);


                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }
    private void dislikeFragments(final String postId) {
        final String POST_URL = BaseUrl_ConstantClass.BASE_URL + "?";

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            loadPost();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "dislikepost");
                logParams.put("post_id", postId);
                logParams.put("userid", userId);


                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }


    private void loadImage() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        final String user_id = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id().toString();


        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.optString("success");
                            String message = jsonObject.optString("message");
                            String user_image = jsonObject.optString("user_img");
                            if (status.equals("true")) {
                                // Toast.makeText(UserNavgation.this, ""+message, Toast.LENGTH_SHORT).show();
                                if (user_image != null) {
//                                    Glide.with(getActivity()).load(user_image).into(profile_img);
                                    Picasso.get().load(user_image).placeholder(R.drawable.profile_img).into(profile_img);
                                } else {
                                    profile_img.setBackgroundResource(R.drawable.profile_img);

                                }

                            } else {
                                Toast.makeText(getActivity(), "Please enter your valid Secret ID or Passkey", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "getimg");
                logParams.put("userid", user_id);
                return logParams;
            }
        };

        MySingleTon.getInstance(getActivity()).addToRequestQue(stringRequestLogIn);

    }

    private void popupShow() {
        if (checkPermission1()) {
            //instantiate the popup.xml layout file
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.social_pop_attached, null);

            //    pop_up_close = (ImageView) customView.findViewById(R.id.pop_up_close);
            //instantiate popup window
            popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //display the popup window
            poplinear = (LinearLayout) customView.findViewById(R.id.poplinear);
            popupWindow.showAtLocation(poplinear, Gravity.CENTER, 0, 0);
           /* popupWindow.setOutsideTouchable(true);


// Set focus true to make prevent touch event to below view (main layout), which works like a dialog with 'cancel' property => Try it! And you will know what I mean.
            popupWindow.setFocusable(true);

// Removes default background.
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) customView.getLayoutParams();
            p.setMargins(30, 0, 30, 0);

            //close the popup window on button click
           /* pop_up_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });*/

            //close the popup window on button click
            ImageView cancel_img = customView.findViewById(R.id.cancel_img);
            cancel_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                }
            });


            //  iv_camera=(ImageView)customView.findViewById(R.id.iv_image);
            iv_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);*/
                    Intent intent = new Intent(getContext(), ImageCroperActivty.class);
                    intent.putExtra("DATA", "One");
                    startActivity(intent);
                    //   popupWindow.dismiss();


                }
            });
            iv_gallery = (ImageView) customView.findViewById(R.id.iv_gallery);
            iv_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);*/
                    Intent intent = new Intent(getContext(), ImageCroperActivty.class);
                    intent.putExtra("DATA", "Two");
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });


        } else {
            requestPermission1();
        }


    }

    private void requestPermission1() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA}, 5);


    }


    public boolean checkPermission1() {

        int result8 = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        int result9 = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getActivity(), CAMERA);
        return result8 == PackageManager.PERMISSION_GRANTED &&
                result9 == PackageManager.PERMISSION_GRANTED &&
                result == PackageManager.PERMISSION_GRANTED;
//
    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
   /* private void openImageOnPopUp() {

        final android.app.AlertDialog.Builder alertadd = new android.app.AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());

        final View view = factory.inflate(R.layout.image_on_popup, null);
        dialog_imageview= (ImageView) view.findViewById(R.id.dialog_imageview);
       ImageView post_image=(ImageView)view.findViewById(R.id.post_image);
        //   Glide.with(UserDetailsActivity.this).load(profile_pic).into(dialog_imageview);

            Glide.with(getContext()).load(post_image).into(dialog_imageview);
        alertadd.setView(view);
        alertadd.show();

    }
*/

// alertdialog for more options















  /*  private void  popupShow(){
        if (checkPermission1())
        {
            //instantiate the popup.xml layout file
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.popup_setting_layout,null);

            pop_up_close = (ImageView) customView.findViewById(R.id.pop_up_close);


            //instantiate popup window
            popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            //display the popup window
            popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) customView.getLayoutParams();
            p.setMargins(30, 0, 30, 0);

            //close the popup window on button click
            pop_up_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            choose_image_iv=(ImageView)customView.findViewById(R.id.choose_image_iv);
            choose_image_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               *//* Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);*//*
                    Intent intent=new Intent(UserDetailsActivity.this,ImageCroperActivty.class);
                    intent.putExtra("DATA", "One");
                    startActivity(intent);
                    popupWindow.dismiss();

                }
            });
            iv_gallery_choose=(ImageView)customView.findViewById(R.id.iv_gallery_choose);
            iv_gallery_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               *//* Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);*//*
                    Intent intent=new Intent(UserDetailsActivity.this,ImageCroperActivty.class);
                    intent.putExtra("DATA", "Two");
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });



        }
        else
        {
            requestPermission1();
        }


    }

    public boolean checkPermission1() {

        int result8 = ContextCompat.checkSelfPermission(UserDetailsActivity.this,READ_EXTERNAL_STORAGE);
        int result9 = ContextCompat.checkSelfPermission(UserDetailsActivity.this,WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(UserDetailsActivity.this,CAMERA);
        return result8 == PackageManager.PERMISSION_GRANTED &&
                result9 == PackageManager.PERMISSION_GRANTED&&
                result == PackageManager.PERMISSION_GRANTED ;
//
    }

    private void openImageOnPopUp() {

        final android.app.AlertDialog.Builder alertadd = new android.app.AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);

        final View view = factory.inflate(R.layout.image_on_popup, null);
        dialog_imageview= (ImageView) view.findViewById(R.id.dialog_imageview);
        //   Glide.with(UserDetailsActivity.this).load(profile_pic).into(dialog_imageview);
        if (profile_pic!=null)
        {
            Glide.with(UserDetailsActivity.this).load(profile_pic).into(dialog_imageview);

        }
        else
        {
            user_profile_image_view.setBackgroundResource(R.drawable.app_buzz_logo);


        }


        alertadd.setView(view);
        alertadd.show();

    }
*/

}
