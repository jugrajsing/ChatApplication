package com.example.hp.chatapplication.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.Adapter.FileListAdapter;
import com.example.hp.chatapplication.Adapter.GroupChannelListFragment;
import com.example.hp.chatapplication.Adapter.GroupChatAdapter;
import com.example.hp.chatapplication.AllContacts;
import com.example.hp.chatapplication.Intefaces.OnBackPressedListener;
import com.example.hp.chatapplication.Main2Activity;
import com.example.hp.chatapplication.MapsActivity;
import com.example.hp.chatapplication.MediaPlayerActivity;
import com.example.hp.chatapplication.PhotoViewerActivity;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.SharedPrefManager;
import com.example.hp.chatapplication.Utils.FileUtils;
import com.example.hp.chatapplication.Utils.TextUtils;
import com.example.hp.chatapplication.Utils.UrlPreviewInfo;
import com.example.hp.chatapplication.Utils.WebUtils;
import com.example.hp.chatapplication.VideoCall.activities.CallActivity;
import com.example.hp.chatapplication.VideoCall.activities.PermissionsActivity;
import com.example.hp.chatapplication.VideoCall.adapters.OpponentsAdapter;
import com.example.hp.chatapplication.VideoCall.db.QbUsersDbManager;
import com.example.hp.chatapplication.VideoCall.services.CallService;
import com.example.hp.chatapplication.VideoCall.util.QBResRequestExecutor;
import com.example.hp.chatapplication.VideoCall.utils.Consts;
import com.example.hp.chatapplication.VideoCall.utils.PermissionsChecker;
import com.example.hp.chatapplication.VideoCall.utils.SharedPrefsHelper;
import com.example.hp.chatapplication.VideoCall.utils.WebRtcSessionManager;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.kevalpatel2106.emoticongifkeyboard.EmoticonGIFKeyboardFragment;
import com.kevalpatel2106.emoticongifkeyboard.emoticons.Emoticon;
import com.kevalpatel2106.emoticongifkeyboard.emoticons.EmoticonSelectListener;
import com.kevalpatel2106.emoticongifkeyboard.gifs.Gif;
import com.kevalpatel2106.emoticongifkeyboard.gifs.GifSelectListener;
import com.kevalpatel2106.emoticonpack.android8.Android8EmoticonProvider;
import com.kevalpatel2106.gifpack.giphy.GiphyGifProvider;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;
import com.stfalcon.multiimageview.MultiImageView;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import br.com.safety.audio_recorder.AudioListener;
import br.com.safety.audio_recorder.AudioRecordButton;
import br.com.safety.audio_recorder.AudioRecording;
import br.com.safety.audio_recorder.RecordingItem;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class GroupChatFragment extends Fragment {
    public static final String LOG_TAG = GroupChatFragment.class.getSimpleName();
    public static final int CHANNEL_LIST_LIMIT = 30;
    public static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHAT";
    public static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_CHAT";
    public static final int STATE_NORMAL = 0;
    public static final int STATE_EDIT = 1;
    public static final String STATE_CHANNEL_URL = "STATE_CHANNEL_URL";
    public static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;
    public static final String EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL";
    private static final int CAMERA_PIC_REQUEST = 100;
    private final static int FILE_REQUEST_CODE = 1;
    //calling
    private static final String TAG = "Call Fragment";
    protected QBResRequestExecutor requestExecutor;
    AudioRecordButton audio_record_button;
    FrameLayout recordcontainer;
    FrameLayout keyboard_container, attachment_container;
    EditText chatsearchbar;
    ArrayList<Integer> oppnentlist1;
    EmoticonGIFKeyboardFragment emoticonGIFKeyboardFragment;
    String[] text = {"Lots of Love", "Laugh out Louder"};
    ImageView chat_backarrow;
    List<Member> members;
    Button call, video;
    ImageView tv_onlinetime;
    private InputMethodManager mIMM;
    private HashMap<BaseChannel.SendFileMessageWithProgressHandler, FileMessage> mFileProgressHandlerMap;
    private RelativeLayout mRootLayout;
    private RecyclerView mRecyclerView;
    private GroupChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private AutoCompleteTextView mMessageEditText;
    private Button mMessageSendButton;
    private ImageButton mUploadFileButton;
    private View mCurrentEventLayout;
    private TextView mCurrentEventText, tv_chatperson_name;
    private SharedPrefsHelper sharedPrefsHelper;
    private GroupChannel mChannel;
    private String mChannelUrl;
    private PreviousMessageListQuery mPrevMessageListQuery;
    private boolean mIsTyping;
    private ImageView emoj, iv_document, iv_camera, iv_gallery, iv_record, iv_audio, iv_location, iv_contact, iv_schedule, iv_chatcall;
    private int mCurrentState = STATE_NORMAL;
    private BaseMessage mEditingMessage = null;
    private FileListAdapter fileListAdapter;
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    private AudioRecordButton mAudioRecordButton;
    private AudioRecording audioRecording;
    private FrameLayout searchframe;
    private ImageView iv_chatsearch;
    private OpponentsAdapter opponentsAdapter;
    private ListView opponentsListView;
    private QBUser currentUser;
    private ArrayList<QBUser> currentOpponentsList;
    private QbUsersDbManager dbManager;
    private boolean isRunForCall;
    private WebRtcSessionManager webRtcSessionManager;
    private PermissionsChecker checker;
    private BaseChannel groupChannel;
    private MultiImageView chat_personimg;


    //  private SharedPrefsHelper sharedPrefsHelper;


    public GroupChatFragment() {
    }

    @TargetApi(Build.VERSION_CODES.M)

    /**
     * To create an instance of this fragment, a Channel URL should be required.
     */

    public static GroupChatFragment newInstance(@NonNull String channelUrl) {
        GroupChatFragment fragment = new GroupChatFragment();
        Bundle args = new Bundle();
        args.putString(GroupChannelListFragment.EXTRA_GROUP_CHANNEL_URL, channelUrl);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        ((Main2Activity) getActivity()).setOnBackPressedListener(new OnBackPressedListener() {
            @Override
            public void doBack() {
                try {
                    if (attachment_container.getVisibility() == View.VISIBLE) {
                        attachment_container.setVisibility(View.GONE);
                    } else if (emoticonGIFKeyboardFragment.isAdded()) {
                        if (emoticonGIFKeyboardFragment.isOpen()) {
                            emoticonGIFKeyboardFragment.close();
                        } else {
                            getActivity().finish();
                        }
                    } else {
                        getActivity().finish();
                    }
                } catch (Exception e) {
                }

            }
        });

        ((Main2Activity) getActivity()).getSupportActionBar().hide();
        mFileProgressHandlerMap = new HashMap<>();


        if (savedInstanceState != null) {
            // Get channel URL from saved state.
            mChannelUrl = savedInstanceState.getString(STATE_CHANNEL_URL);
        } else {
            // Get channel URL from GroupChannelListFragment.
            mChannelUrl = getArguments().getString(GroupChannelListFragment.EXTRA_GROUP_CHANNEL_URL);
        }

        Log.d(LOG_TAG, mChannelUrl);

        mChatAdapter = new GroupChatAdapter(getActivity());
        setUpChatListAdapter();

        // Load messages from cache.
        mChatAdapter.load(mChannelUrl);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_chat, container, false);
// call
        initFields();


        setRetainInstance(true);
        sharedPrefsHelper = SharedPrefsHelper.getInstance();

        mRootLayout = (RelativeLayout) rootView.findViewById(R.id.layout_group_chat_root);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_group_chat);

        mCurrentEventLayout = rootView.findViewById(R.id.layout_group_chat_current_event);
        mCurrentEventText = (TextView) rootView.findViewById(R.id.text_group_chat_current_event);
        tv_chatperson_name = (TextView) rootView.findViewById(R.id.tv_chatperson_name);

        mMessageEditText = rootView.findViewById(R.id.edittext_group_chat_message);
        mMessageSendButton = (Button) rootView.findViewById(R.id.button_group_chat_send);
        mUploadFileButton = (ImageButton) rootView.findViewById(R.id.button_group_chat_upload);
        attachment_container = (FrameLayout) rootView.findViewById(R.id.attachment_container);
        keyboard_container = (FrameLayout) rootView.findViewById(R.id.keyboard_container);
        iv_document = (ImageView) rootView.findViewById(R.id.iv_document);
        iv_camera = (ImageView) rootView.findViewById(R.id.iv_camera);
        iv_gallery = (ImageView) rootView.findViewById(R.id.iv_gallery);
        iv_record = (ImageView) rootView.findViewById(R.id.iv_record);
        iv_audio = (ImageView) rootView.findViewById(R.id.iv_audio);
        iv_contact = (ImageView) rootView.findViewById(R.id.iv_contact);
        iv_location = (ImageView) rootView.findViewById(R.id.iv_location);
        iv_schedule = (ImageView) rootView.findViewById(R.id.iv_schedule);
        iv_chatsearch = (ImageView) rootView.findViewById(R.id.iv_chatsearch);
        iv_chatcall = (ImageView) rootView.findViewById(R.id.iv_chatcall);
        chat_backarrow = (ImageView) rootView.findViewById(R.id.chat_backarrow);
        tv_onlinetime = (ImageView) rootView.findViewById(R.id.tv_onlinetime);
        chat_personimg = (MultiImageView) rootView.findViewById(R.id.chat_personimg);
        recordcontainer = (FrameLayout) rootView.findViewById(R.id.recordcontainer);
        audio_record_button = (AudioRecordButton) rootView.findViewById(R.id.audio_record_button);
        searchframe = (FrameLayout) rootView.findViewById(R.id.searchframe);
        chatsearchbar = (EditText) rootView.findViewById(R.id.searchbar);
        chat_personimg.setShape(MultiImageView.Shape.CIRCLE);
        Glide.with(getContext()).load(SendBird.getCurrentUser().getProfileUrl()).into(chat_personimg);
        // Log.d("urlss",SendBird.getCurrentUser().getProfileUrl());
       /* if (SendBird.getCurrentUser().getConnectionStatus()==User.ConnectionStatus.ONLINE) {
            tv_onlinetime.setImageResource(R.drawable.graydot);
           // Glide.with(getContext()).load(SendBird.getCurrentUser().get).into(tv_onlinetime);
        }*/
        //
        // Glide.with(getContext()).load(currentOpponentsList.get(members).getProfileUrl()).into(chat_personimg);

        iv_chatcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isLoggedInChat()) {
                    startCall(false);
                }
//                if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                //          startPermissionsActivity(false);
                //      }


               /* if (isLoggedInChat()) {
               //     startCall(false  );
                }*/
               /* if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                    startPermissionsActivity(false);
                }*/
            }
        });

        iv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordcontainer.setVisibility(View.VISIBLE);
                attachment_container.setVisibility(View.GONE);
            }
        });
        iv_chatsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchframe.setVisibility(View.VISIBLE);
            }
        });
        chat_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getFragmentManager().popBackStack();
                // ft.replace(R.id.fragment_container, newFragment, tag);\

                // getFragmentManager().popBackStack(mRootLayout);
                // ((Main3Activity) getActivity()).setOnBackPressedListener(null);
                getActivity().finish();
               /* Fragment fragment=new OpenChannelListFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_group_channel,fragment).addToBackStack(null).commit();
*/
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, text);
        mMessageEditText.setThreshold(1);//will start working from first character
        mMessageEditText.setAdapter(adapter);//setting the adapter data into the
        audioRecording = new AudioRecording(getActivity());
        initView();
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, 0);
        this.mAudioRecordButton.setOnAudioListener(new AudioListener() {
            @Override
            public void onStop(RecordingItem recordingItem) {
                Toast.makeText(getContext(), "Audio..", Toast.LENGTH_SHORT).show();
                audioRecording.play(recordingItem);
            }

            @Override
            public void onCancel() {
                recordcontainer.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Log.d("MainActivity", "Error: " + e.getMessage());
            }
        });


        iv_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setSelectedMediaFiles(mediaFiles)
                        .setShowFiles(true)
                        .setShowImages(false)
                        .setShowVideos(false)
                        .setMaxSelection(10)
                        .setRootPath(Environment.getExternalStorageDirectory().getPath() + "/Download")
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        });
        iv_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                MediaFile file = null;
                for (int i = 0; i < mediaFiles.size(); i++) {
                    if (mediaFiles.get(i).getMediaType() == MediaFile.TYPE_AUDIO) {
                        file = mediaFiles.get(i);
                    }
                }
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(false)
                        .setShowVideos(false)
                        .setShowAudios(true)
                        .setSingleChoiceMode(true)
                        .setSelectedMediaFile(file)
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        });


        // camera click
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //   startActivity(cameraIntent);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

            }
        });
        iv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactintent = new Intent(getContext(), AllContacts.class);
                startActivity(contactintent);
            }
        });

        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationintent = new Intent(getContext(), MapsActivity.class);
                startActivity(locationintent);
            }
        });

        // gallery click

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMedia();
            }
        });


        //  mMessageEditText = (com.kevalpatel2106.emoticongifkeyboard.widget.EmoticonEditText) rootView.findViewById(R.id.edittext_chat_message);
        mMessageEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    emoticonGIFKeyboardFragment.close();
                } catch (Exception e) {
                }
            }
        });

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString().trim();
                if (s1.length() > 0 && !s1.equals("")) {
                    mMessageSendButton.setEnabled(true);
                } else {
                    mMessageSendButton.setEnabled(false);
                }
            }
        });

        mMessageSendButton.setEnabled(false);
        mMessageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == STATE_EDIT) {
                    String userInput = mMessageEditText.getText().toString();
                    if (userInput.length() > 0) {
                        if (mEditingMessage != null) {
                            editMessage(mEditingMessage, userInput);
                        }
                    }
                    setState(STATE_NORMAL, null, -1);
                } else {
                    String userInput = mMessageEditText.getText().toString().trim();

                    if (userInput.length() > 0) {
                        sendUserMessage(userInput);
                        mMessageEditText.setText("");
                    }
                }
            }
        });

        mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachment_container.setVisibility(View.VISIBLE);
                keyboard_container.setVisibility(View.GONE);
                /* requestMedia();*/
            }
        });

        mIsTyping = false;
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mIsTyping) {
                    setTypingStatus(true);
                }

                if (s.length() == 0) {
                    setTypingStatus(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        setUpRecyclerView();
        setHasOptionsMenu(true);
        EmoticonGIFKeyboardFragment.EmoticonConfig emoticonConfig = new EmoticonGIFKeyboardFragment.EmoticonConfig()
                .setEmoticonProvider(Android8EmoticonProvider.create())
                /*
                  NOTE: The process of removing last character when user preses back space will handle
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
                        //  sendUserMessage(emoticon.getUnicode());
                        mMessageEditText.append(emoticon.getUnicode());

                    }

                    @Override
                    public void onBackSpace() {
                        keyboard_container.setVisibility(View.GONE);
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
                        //    Log.d(TAG, "onGifSelected: " + gif.getGifUrl());
                        Log.d("GUF", "onGifSelected: " + gif.getGifUrl());
                        sendUserMessage(gif.getGifUrl());
                    }
                });
        keyboard_container.setVisibility(View.VISIBLE);
        attachment_container.setVisibility(View.GONE);
        emoticonGIFKeyboardFragment = EmoticonGIFKeyboardFragment
                .getNewInstance(rootView.findViewById(R.id.keyboard_container), emoticonConfig, gifConfig);
        emoj = (ImageView) rootView.findViewById(R.id.emoji_open_close_btn);
        emoj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboard_container.setVisibility(View.VISIBLE);
                attachment_container.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//Adding the keyboard fragment to keyboard_container.
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.keyboard_container, emoticonGIFKeyboardFragment)
                        .commit();
                emoticonGIFKeyboardFragment.open();
            }
        });

        return rootView;
    }

    private void initFields() {
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
        }


    }

    private void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(getActivity(), checkOnlyAudio, Consts.PERMISSIONS);
    }


    private boolean isLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            Toast.makeText(getActivity(), R.string.dlg_signal_error, Toast.LENGTH_SHORT).show();
            tryReLoginToChat();
            return false;
        }
        return true;
    }

    private void tryReLoginToChat() {
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            CallService.start(getActivity(), qbUser);
        }
    }

    private void startCall(boolean isVideoCall) {
      /*  if (opponentsAdapter.getSelectedItems().size() > Consts.MAX_OPPONENTS_COUNT) {
            Toast.makeText(getActivity(),getString(R.string.error_max_opponents_count)+Consts.MAX_OPPONENTS_COUNT,Toast.LENGTH_SHORT).show();
            return;
        }
*/
        Log.d(TAG, "startCall()");
        oppnentlist1 = new ArrayList<Integer>();

     /*   QBUser user=null;
        user.getId();*/
        // ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(opponentsAdapter.getSelectedItems());
        // ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(opponentsAdapter.getSelectedItems());

        List<Member> member = mChannel.getMembers();
        for (int i = 0; i < member.size(); i++) {
            if (!sharedPrefsHelper.getQbUser().getId().toString().equals(member.get(i).getUserId())) {
                member.get(i).getProfileUrl();
                Glide.with(getContext()).load(member.get(i).getProfileUrl()).into(chat_personimg);

                oppnentlist1.add(Integer.valueOf(member.get(i).getUserId()));

            }
           /*if (!member.get(i).getConnectionStatus().equals(User.ConnectionStatus.ONLINE))
           {
               tv_onlinetime.setImageResource(R.drawable.graydot);
           }*/
        }

        // User ms=null;
        // for (int i=0; i<member.size();i++){
        //    member.get(i);


        //  String arrayList= ms.getUserId();

        //  if (qs.getFullName()==ms.getUserId()) {

        //  }
        //   ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(opponentsAdapter.getSelectedItems());
        //    SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        //   int savedPref = sharedPreferences.getInt("opponents_key", 0);
        //  Toast.makeText(getContext(), ""+savedPref, Toast.LENGTH_SHORT).show();
        // List<Member> members=mChannel.getMembers();

        //  Toast.makeText(getActivity(), ""+members, Toast.LENGTH_SHORT).show();
        //  ArrayList<Integer> opponentsList = new ArrayList<Integer>();
        //opponentsList.add(savedPref);
        // opponentsList.add();


        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
        String ids = SharedPrefManager.getInstance(getActivity()).getUser().getUser_id();


        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getActivity());

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(oppnentlist1, conferenceType);


        WebRtcSessionManager.getInstance(getActivity()).setCurrentSession(newQbRtcSession);

        //   PushNotificationSender.sendPushMessage(opponentsList, currentUser.getFullName());

        CallActivity.start(getActivity(), false);
        Log.d(TAG, "conferenceType = " + conferenceType);
    }

   /* private void startCall(boolean isVideoCall) {


        Log.d("call", "startCall()");
      //  ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(opponentsAdapter.getSelectedItems());
        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType .QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.getInstance(getActivity()).setCurrentSession(newQbRtcSession);

        PushNotificationSender.sendPushMessage(opponentsList, currentUser.getFullName());

        CallActivity.start(getActivity(), false);
        Log.d("Conference", "conferenceType = " + conferenceType);
    }*/

    private void refresh() {
        if (mChannel == null) {
            GroupChannel.getChannel(mChannelUrl, new GroupChannel.GroupChannelGetHandler() {
                @Override
                public void onResult(GroupChannel groupChannel, SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }

                    mChannel = groupChannel;

                    mChatAdapter.setChannel(mChannel);
                    mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, new BaseChannel.GetMessagesHandler() {
                        @Override
                        public void onResult(List<BaseMessage> list, SendBirdException e) {
                            mChatAdapter.markAllMessagesAsRead();
                        }
                    });

                  /*  if (getActivity() != null) {
                        // Set action bar title to name of channel
                        // ((Main3Activity) getActivity()).setActionBarTitle(mChannel.getName());
                        ((Main2Activity) getActivity()).setActionBarTitle(mChannel.getName());
                        //Toast.makeText(getContext(), ""+mChannel.getName(), Toast.LENGTH_SHORT).show();
                        tv_chatperson_name.setText(mChannel.getName());
                        Toast.makeText(getContext(), ""+mChannel.getName(), Toast.LENGTH_SHORT).show();
                        
*/
                    // }
                    updateActionBarTitle();
                }
            });
        } else {
            mChannel.refresh(new GroupChannel.GroupChannelRefreshHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }

                    mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, new BaseChannel.GetMessagesHandler() {
                        @Override
                        public void onResult(List<BaseMessage> list, SendBirdException e) {
                            mChatAdapter.markAllMessagesAsRead();
                        }
                    });
                    if (getActivity() != null) {
                        // Set action bar title to name of channel
                        // ((Main3Activity) getActivity()).setActionBarTitle(mChannel.getName());
                        ((Main2Activity) getActivity()).setActionBarTitle(mChannel.getName());
                        // mChannel.getMyMemberState().


                        // ((Main2Activity) getActivity()).setActionBarTitle(mChannel.getName());
                        //Toast.makeText(getContext(), ""+mChannel.getName(), Toast.LENGTH_SHORT).show();
                        tv_chatperson_name.setText(mChannel.getName());

                    }
                    updateActionBarTitle();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        refresh();


        mChatAdapter.setContext(getActivity()); // Glide bug fix (java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity)

        // Gets channel from URL user requested

        Log.d(LOG_TAG, mChannelUrl);

        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.markAllMessagesAsRead();
                    // Add new message to view
                    mChatAdapter.addFirst(baseMessage);
                }
            }

            @Override
            public void onMessageDeleted(BaseChannel baseChannel, long msgId) {
                super.onMessageDeleted(baseChannel, msgId);
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.delete(msgId);
                }
            }

            @Override
            public void onMessageUpdated(BaseChannel channel, BaseMessage message) {
                super.onMessageUpdated(channel, message);
                if (channel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.update(message);
                }
            }

            @Override
            public void onReadReceiptUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTypingStatusUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    List<Member> typingUsers = channel.getTypingMembers();
                    displayTyping(typingUsers);
                }
            }

        });
    }

    /*  @Override
      public void onPause() {
          *//*setTypingStatus(false);


        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID);
        super.onPause();*//*
    }
*/
    @Override
    public void onDestroy() {
        // Save messages to cache.
        mChatAdapter.save();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_CHANNEL_URL, mChannelUrl);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //  inflater.inflate(R.menu.menu_group_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       /* if (id == R.id.action_group_channel_invite) {
            Intent intent = new Intent(getActivity(), InviteMemberActivity.class);
            intent.putExtra(EXTRA_CHANNEL_URL, mChannelUrl);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_group_channel_view_members) {
            Intent intent = new Intent(getActivity(), MemberListActivity.class);
            intent.putExtra(EXTRA_CHANNEL_URL, mChannelUrl);
            startActivity(intent);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Set this as true to restore background connection management.
        SendBird.setAutoBackgroundDetection(true);

        if (requestCode == INTENT_REQUEST_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            // If user has successfully chosen the image, show a dialog to confirm upload.
            if (data == null) {
                Log.d(LOG_TAG, "data is null!");
                return;
            }

            sendFileWithThumbnail(data.getData());
        } else if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final Bitmap bmpcamera = (Bitmap) bundle.get("data");


        }
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mChatAdapter.getItemCount() - 1) {
                    mChatAdapter.loadPreviousMessages(CHANNEL_LIST_LIMIT, null);
                }
            }
        });
    }

    private void setUpChatListAdapter() {
        mChatAdapter.setItemClickListener(new GroupChatAdapter.OnItemClickListener() {
            @Override
            public void onUserMessageItemClick(UserMessage message) {
                // Restore failed message and remove the failed message from list.
                if (mChatAdapter.isFailedMessage(message)) {
                    retryFailedMessage(message);
                    return;
                }

                // Message is sending. Do nothing on click event.
                if (mChatAdapter.isTempMessage(message)) {
                    return;
                }


                if (message.getCustomType().equals(GroupChatAdapter.URL_PREVIEW_CUSTOM_TYPE)) {
                    try {
                        UrlPreviewInfo info = new UrlPreviewInfo(message.getData());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.getUrl()));
                        startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFileMessageItemClick(FileMessage message) {
                // Load media chooser and remove the failed message from list.
                if (mChatAdapter.isFailedMessage(message)) {
                    retryFailedMessage(message);
                    return;
                }

                // Message is sending. Do nothing on click event.
                if (mChatAdapter.isTempMessage(message)) {
                    return;
                }


                onFileMessageClicked(message);
            }
        });

        mChatAdapter.setItemLongClickListener(new GroupChatAdapter.OnItemLongClickListener() {
            @Override
            public void onUserMessageItemLongClick(UserMessage message, int position) {
                showMessageOptionsDialog(message, position);

            }

            @Override
            public void onFileMessageItemLongClick(FileMessage message) {
            }

            @Override
            public void onAdminMessageItemLongClick(AdminMessage message) {
            }
        });
    }

    private void showMessageOptionsDialog(final BaseMessage message, final int position) {
        String[] options = new String[]{"Edit message", "Delete message"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setState(STATE_EDIT, message, position);
                } else if (which == 1) {
                    deleteMessage(message);
                }
            }
        });
        builder.create().show();
    }

    private void setState(int state, BaseMessage editingMessage, final int position) {
        switch (state) {
            case STATE_NORMAL:
                mCurrentState = STATE_NORMAL;
                mEditingMessage = null;

                mUploadFileButton.setVisibility(View.VISIBLE);
                mMessageSendButton.setText("SEND");
                mMessageEditText.setText("");
                break;

            case STATE_EDIT:
                mCurrentState = STATE_EDIT;
                mEditingMessage = editingMessage;

                mUploadFileButton.setVisibility(View.GONE);
                mMessageSendButton.setText("SAVE");
                String messageString = ((UserMessage) editingMessage).getMessage();
                if (messageString == null) {
                    messageString = "";
                }
                mMessageEditText.setText(messageString);
                if (messageString.length() > 0) {
                    mMessageEditText.setSelection(0, messageString.length());
                }

                mMessageEditText.requestFocus();
                mMessageEditText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIMM.showSoftInput(mMessageEditText, 0);

                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.scrollToPosition(position);
                            }
                        }, 500);
                    }
                }, 100);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* ((GroupChannelActivity)context).setOnBackPressedListener(new GroupChannelActivity.onBackPressedListener() {
            @Override
            public boolean onBack() {
                if (mCurrentState == STATE_EDIT) {
                    setState(STATE_NORMAL, null, -1);
                    return true;
                }

                mIMM.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
                return false;
            }
        });*/
    }

    private void retryFailedMessage(final BaseMessage message) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Retry?")
                .setPositiveButton(R.string.resend_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (message instanceof UserMessage) {
                                String userInput = ((UserMessage) message).getMessage();
                                sendUserMessage(userInput);
                            } else if (message instanceof FileMessage) {
                                Uri uri = mChatAdapter.getTempFileMessageUri(message);
                                sendFileWithThumbnail(uri);
                            }
                            mChatAdapter.removeFailedMessage(message);
                        }
                    }
                })
                .setNegativeButton(R.string.delete_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            mChatAdapter.removeFailedMessage(message);
                        }
                    }
                }).show();
    }

    /**
     * Display which users are typing.
     * If more than two users are currently typing, this will state that "multiple users" are typing.
     *
     * @param typingUsers The list of currently typing users.
     */
    private void displayTyping(List<Member> typingUsers) {

        if (typingUsers.size() > 0) {
            mCurrentEventLayout.setVisibility(View.VISIBLE);
            String string;

            if (typingUsers.size() == 1) {
                string = typingUsers.get(0).getNickname() + " is typing";
            } else if (typingUsers.size() == 2) {
                string = typingUsers.get(0).getNickname() + " " + typingUsers.get(1).getNickname() + " is typing";
            } else {
                string = "Multiple users are typing";
            }
            mCurrentEventText.setText(string);
        } else {
            mCurrentEventLayout.setVisibility(View.GONE);
        }
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

    private void onFileMessageClicked(FileMessage message) {
        String type = message.getType().toLowerCase();
        if (type.startsWith("image")) {
            Intent i = new Intent(getActivity(), PhotoViewerActivity.class);
            i.putExtra("url", message.getUrl());
            i.putExtra("type", message.getType());
            startActivity(i);
        } else if (type.startsWith("video")) {
            Intent intent = new Intent(getActivity(), MediaPlayerActivity.class);
            intent.putExtra("url", message.getUrl());
            startActivity(intent);
        } else {
            showDownloadConfirmDialog(message);
        }
    }

    private void showDownloadConfirmDialog(final FileMessage message) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Download file?")
                    .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                FileUtils.downloadFile(getActivity(), message.getUrl(), message.getName());
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).show();
        }

    }

  /*  private void updateActionBarTitle() {
        String title = "";

        if(mChannel != null) {
           //title = mChannel.getName();
            if (getActivity() != null) {
                // Set action bar title to name of channel
                // ((Main3Activity) getActivity()).setActionBarTitle(mChannel.getName());
                ((Main2Activity) getActivity()).setActionBarTitle(mChannel.getName());
                //Toast.makeText(getContext(), ""+mChannel.getName(), Toast.LENGTH_SHORT).show();
                tv_chatperson_name.setText(mChannel.getName());
                Toast.makeText(getContext(), ""+mChannel.getName(), Toast.LENGTH_SHORT).show();


            }

            //title = TextUtils.getGroupChannelTitle(mChannel);
        }

        // Set action bar title to name of channel
      //  tv_chatperson_name.setText(title);
    }*/

    private void updateActionBarTitle() {
        String title = "";
        try {

            if (mChannel != null) {
                title = TextUtils.getGroupChannelTitle(mChannel);

                List<Member> member = mChannel.getMembers();
                for (int i = 0; i < member.size(); i++) {

                    if (!sharedPrefsHelper.getQbUser().getId().toString().equals(member.get(i).getUserId())) {
                        member.get(i).getProfileUrl();
                        if (member.get(i).getConnectionStatus() == member.get(i).getConnectionStatus().ONLINE) {
                            tv_onlinetime.setVisibility(View.VISIBLE);
                        }
                        Glide.with(getContext()).load(member.get(i).getProfileUrl()).into(chat_personimg);


                    }
                }
            }

            // Set action bar title to name of channel
            if (getActivity() != null) {
                ((Main2Activity) getActivity()).setActionBarTitle(title);
                tv_chatperson_name.setText(title);

                // Toast.makeText(getContext(), ""+title, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendUserMessageWithUrl(final String text, String url) {
        new WebUtils.UrlPreviewAsyncTask() {
            @Override
            protected void onPostExecute(UrlPreviewInfo info) {
                UserMessage tempUserMessage = null;
                BaseChannel.SendUserMessageHandler handler = new BaseChannel.SendUserMessageHandler() {
                    @Override
                    public void onSent(UserMessage userMessage, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            Log.e(LOG_TAG, e.toString());
                            Toast.makeText(
                                    getActivity(),
                                    "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                            mChatAdapter.markMessageFailed(userMessage.getRequestId());
                            return;
                        }

                        // Update a sent message to RecyclerView
                        mChatAdapter.markMessageSent(userMessage);
                    }
                };

                try {
                    // Sending a message with URL preview information and custom type.
                    String jsonString = info.toJsonString();
                    tempUserMessage = mChannel.sendUserMessage(text, jsonString, GroupChatAdapter.URL_PREVIEW_CUSTOM_TYPE, handler);
                } catch (Exception e) {
                    // Sending a message without URL preview information.
                    tempUserMessage = mChannel.sendUserMessage(text, handler);
                }


                // Display a user message to RecyclerView
                mChatAdapter.addFirst(tempUserMessage);
            }
        }.execute(url);
    }

    private void sendUserMessage(String text) {
        List<String> urls = WebUtils.extractUrls(text);
        if (urls.size() > 0) {
            sendUserMessageWithUrl(text, urls.get(0));

            return;
        }

        UserMessage tempUserMessage = mChannel.sendUserMessage(text, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Log.e(LOG_TAG, e.toString());
                    Toast.makeText(
                            getActivity(),
                            "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    mChatAdapter.markMessageFailed(userMessage.getRequestId());
                    return;
                }

                // Update a sent message to RecyclerView
                mChatAdapter.markMessageSent(userMessage);
            }
        });

        // Display a user message to RecyclerView
        mChatAdapter.addFirst(tempUserMessage);
    }

    /**
     * Notify other users whether the current user is typing.
     *
     * @param typing Whether the user is currently typing.
     */
    private void setTypingStatus(boolean typing) {
        if (mChannel == null) {
            return;
        }

        if (typing) {
            mIsTyping = true;
            mChannel.startTyping();
        } else {
            mIsTyping = false;
            mChannel.endTyping();
        }
    }

    /**
     * Sends a File Message containing an image file.
     * Also requests thumbnails to be generated in specified sizes.
     *
     * @param uri The URI of the image, which in this case is received through an Intent request.
     */
    private void sendFileWithThumbnail(Uri uri) {
        // Specify two dimensions of thumbnails to generate
        List<FileMessage.ThumbnailSize> thumbnailSizes = new ArrayList<>();
        thumbnailSizes.add(new FileMessage.ThumbnailSize(240, 240));
        thumbnailSizes.add(new FileMessage.ThumbnailSize(320, 320));

        Hashtable<String, Object> info = FileUtils.getFileInfo(getActivity(), uri);

        if (info == null) {
            Toast.makeText(getActivity(), "Extracting file information failed.", Toast.LENGTH_LONG).show();
            return;
        }

        final String path = (String) info.get("path");
        final File file = new File(path);
        final String name = file.getName();
        final String mime = (String) info.get("mime");
        final int size = (Integer) info.get("size");

        if (path.equals("")) {
            Toast.makeText(getActivity(), "File must be located in local storage.", Toast.LENGTH_LONG).show();
        } else {
            BaseChannel.SendFileMessageWithProgressHandler progressHandler = new BaseChannel.SendFileMessageWithProgressHandler() {
                @Override
                public void onProgress(int bytesSent, int totalBytesSent, int totalBytesToSend) {
                    FileMessage fileMessage = mFileProgressHandlerMap.get(this);
                    if (fileMessage != null && totalBytesToSend > 0) {
                        int percent = (totalBytesSent * 100) / totalBytesToSend;
                        mChatAdapter.setFileProgressPercent(fileMessage, percent);
                    }
                }

                @Override
                public void onSent(FileMessage fileMessage, SendBirdException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        mChatAdapter.markMessageFailed(fileMessage.getRequestId());
                        return;
                    }

                    mChatAdapter.markMessageSent(fileMessage);
                }
            };

            // Send image with thumbnails in the specified dimensions
            FileMessage tempFileMessage = mChannel.sendFileMessage(file, name, mime, size, "", null, thumbnailSizes, progressHandler);

            mFileProgressHandlerMap.put(progressHandler, tempFileMessage);

            mChatAdapter.addTempFileMessageInfo(tempFileMessage, uri);
            mChatAdapter.addFirst(tempFileMessage);
        }
    }

    private void editMessage(final BaseMessage message, String editedMessage) {
        mChannel.updateUserMessage(message.getMessageId(), editedMessage, null, null, new BaseChannel.UpdateUserMessageHandler() {
            @Override
            public void onUpdated(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(getActivity(), "Error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, new BaseChannel.GetMessagesHandler() {
                    @Override
                    public void onResult(List<BaseMessage> list, SendBirdException e) {
                        mChatAdapter.markAllMessagesAsRead();
                    }
                });
            }
        });
    }

    /**
     * Deletes a message within the channel.
     * Note that users can only delete messages sent by oneself.
     *
     * @param message The message to delete.
     */

    private void deleteMessage(final BaseMessage message) {
        mChannel.deleteMessage(message, new BaseChannel.DeleteMessageHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(getActivity(), "Error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                mChatAdapter.loadLatestMessages(CHANNEL_LIST_LIMIT, new BaseChannel.GetMessagesHandler() {
                    @Override
                    public void onResult(List<BaseMessage> list, SendBirdException e) {
                        mChatAdapter.markAllMessagesAsRead();
                    }
                });
            }
        });
    }

    private void initView() {
        this.mAudioRecordButton = audio_record_button;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}





