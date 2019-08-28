package com.example.hp.chatapplication.openchannel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.chatapplication.Adapter.FileListAdapter;
import com.example.hp.chatapplication.AllContacts;
import com.example.hp.chatapplication.Intefaces.OnBackPressedListener;
import com.example.hp.chatapplication.MapsActivity;
import com.example.hp.chatapplication.MediaPlayerActivity;
import com.example.hp.chatapplication.PhotoViewerActivity;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.Utils.FileUtils;
import com.example.hp.chatapplication.Utils.Main3Activity;
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
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import br.com.safety.audio_recorder.AudioListener;
import br.com.safety.audio_recorder.AudioRecordButton;
import br.com.safety.audio_recorder.AudioRecording;
import br.com.safety.audio_recorder.RecordingItem;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;


public class OpenChatFragment extends Fragment {

    public static final int CAMERA_PIC_REQUEST = 100;
    public static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
    static final String EXTRA_CHANNEL_URL = "CHANNEL_URL";
    private static final String LOG_TAG = OpenChatFragment.class.getSimpleName();
    private static final int CHANNEL_LIST_LIMIT = 30;
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_OPEN_CHAT";
    private static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_OPEN_CHAT";
    private static final int STATE_NORMAL = 0;
    private static final int STATE_EDIT = 1;
    private static final int INTENT_REQUEST_CHOOSE_IMAGE = 300;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;
    private final static int FILE_REQUEST_CODE = 1;
    EmoticonGIFKeyboardFragment emoticonGIFKeyboardFragment;
    FrameLayout attachment_container, keyboard_container;
    EditText chatsearchbar;
    AudioRecordButton audio_record_button;
    FrameLayout recordcontainer, searchframe;
    ImageView chat_backarrow, iv_chatsearch;
    private InputMethodManager mIMM;
    private RecyclerView mRecyclerView;
    private OpenChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private View mRootLayout;
    private com.kevalpatel2106.emoticongifkeyboard.widget.EmoticonEditText mMessageEditText;
    private Button mMessageSendButton;
    private ImageButton mUploadFileButton;
    private View mCurrentEventLayout;
    private TextView mCurrentEventText, tv_chatperson_name;
    private ImageView emoj, iv_chat_more;
    private OpenChannel mChannel;
    private String mChannelUrl;
    private PreviousMessageListQuery mPrevMessageListQuery;
    private int mCurrentState = STATE_NORMAL;
    private BaseMessage mEditingMessage = null;
    private ImageView iv_document, iv_camera, iv_gallery, iv_record, iv_audio, iv_location, iv_contact, iv_schedule;
    private AudioRecordButton mAudioRecordButton;
    private AudioRecording audioRecording;
    private FileListAdapter fileListAdapter;
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    private List<BaseMessage> mMessageList;

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
        mIMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        ((Main3Activity) getActivity()).setOnBackPressedListener(new OnBackPressedListener() {
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
        ((Main3Activity) getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_open_chat, container, false);
        getActivity().getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);

        setRetainInstance(true);

        setHasOptionsMenu(true);
        attachment_container = rootView.findViewById(R.id.attachment_container);
        keyboard_container = rootView.findViewById(R.id.keyboard_container);
        mRootLayout = rootView.findViewById(R.id.layout_open_chat_root);
        // ImageView chat_backarrow = rootView.findViewById(R.id.chat_backarrow);

        iv_document = (ImageView) rootView.findViewById(R.id.iv_document);
        iv_camera = (ImageView) rootView.findViewById(R.id.iv_camera);
        iv_gallery = (ImageView) rootView.findViewById(R.id.iv_gallery);
        iv_record = (ImageView) rootView.findViewById(R.id.ic_recorder);
        iv_audio = (ImageView) rootView.findViewById(R.id.iv_audio);
        iv_contact = (ImageView) rootView.findViewById(R.id.iv_contact);
        iv_location = (ImageView) rootView.findViewById(R.id.iv_location);
        iv_schedule = (ImageView) rootView.findViewById(R.id.iv_schedule);
        iv_chatsearch = (ImageView) rootView.findViewById(R.id.iv_chatsearch);
        chat_backarrow = (ImageView) rootView.findViewById(R.id.chat_backarrow);
        recordcontainer = (FrameLayout) rootView.findViewById(R.id.recordcontainer);
        searchframe = (FrameLayout) rootView.findViewById(R.id.searchframe);
        audio_record_button = (AudioRecordButton) rootView.findViewById(R.id.audio_record_button);
        chatsearchbar = (EditText) rootView.findViewById(R.id.searchbar);


        iv_chatsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchframe.setVisibility(View.VISIBLE);
            }
        });
        // sendUserMessage(chatsearchbar.getText().toString());
        chatsearchbar.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendUserMessage(chatsearchbar.getText().toString());
            }
        });


/*

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, text);
        mMessageEditText.setThreshold(1);//will start working from first character
        mMessageEditText.setAdapter(adapter);//setting the adapter data into the
*/

        audioRecording = new AudioRecording(getActivity());

        initView();

        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 0);

        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, 0);
        chat_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Main3Activity) getActivity()).finish();
            }
        });

        iv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordcontainer.setVisibility(View.VISIBLE);
                attachment_container.setVisibility(View.GONE);
            }
        });
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
        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMedia();
            }
        });


    /*    chatsearchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!channellist.isEmpty()) {
                    channellist.clear();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                s = s.toString().toLowerCase();
                if (!channellist.isEmpty()) {
                    channellist.clear();
                }
                for (int i3 = 0; i3 < listse.size(); i3++) {
                    liveTvData = new LiveTvData();
                    final String text = listse.get(i3).toLowerCase();
                    if (text.contains(s)) {

                        liveTvData.setName(listse.get(i3));
                        liveTvData.setLogo(map.get(listse.get(i3)));
                        liveTvData.setEpg_now(map2.get(listse.get(i3)));
                        liveTvData.setEpg_next(map1.get(listse.get(i3)));
                        channellist.add(liveTvData);
                        myAdaptor = new Livetvadapter(getContext(), channellist);
                        usrecycler.setAdapter(myAdaptor);
                        myAdaptor.notifyDataSetChanged();


                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    myAdaptor.notifyDataSetChanged();
                } catch (NullPointerException e) {

                }

            }
        });*/


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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_open_channel_chat);

        mCurrentEventLayout = rootView.findViewById(R.id.layout_open_chat_current_event);
        mCurrentEventText = (TextView) rootView.findViewById(R.id.text_open_chat_current_event);
        tv_chatperson_name = (TextView) rootView.findViewById(R.id.tv_chatperson_name);
        iv_chat_more = (ImageView) rootView.findViewById(R.id.iv_chat_more);
        //((Main3Activity) getActivity()).setActionBarTitle(mChannel.getName());


        setUpChatAdapter();
        setUpRecyclerView();
        // adding menu

        // Set up chat box
        mMessageSendButton = (Button) rootView.findViewById(R.id.button_open_channel_chat_send);
        mMessageEditText = (com.kevalpatel2106.emoticongifkeyboard.widget.EmoticonEditText) rootView.findViewById(R.id.edittext_chat_message);
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


        mUploadFileButton = (ImageButton) rootView.findViewById(R.id.button_open_channel_chat_upload);
        mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachment_container.setVisibility(View.VISIBLE);
                keyboard_container.setVisibility(View.GONE);

                //creating a popup menu
              /*  PopupMenu popup = new PopupMenu(getActivity(), mUploadFileButton);

                //inflating menu from xml resource
                popup.inflate(R.menu.more_chatopen);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.muteunmute:
                                //handle menu1 click
                                return true;
                            case R.id.followunfollow:
                                //handle menu2 click
                                return true;
                            case R.id.unfriend:
                                //handle menu3 click
                                return true;
                            case R.id.block:
                                //handle menu3 click
                                return true;
                            case R.id.deletechat:
                                //handle menu3 click
                                return true;
                            case R.id.emailchat:
                                //handle menu3 click
                                return true;
                            case R.id.report:
                                //handle menu3 click
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
*/
            }
        });


        iv_chat_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(getActivity(), iv_chat_more);

                //inflating menu from xml resource
                popup.inflate(R.menu.more_chatopen);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.muteunmute:
                                //handle menu1 click
                                return true;
                            case R.id.followunfollow:
                                //handle menu2 click
                                return true;
                            case R.id.unfriend:
                                //handle menu3 click
                                return true;
                            case R.id.block:
                                //handle menu3 click
                                return true;
                            case R.id.deletechat:
                                //handle menu3 click
                                return true;
                            case R.id.emailchat:
                                //handle menu3 click
                                return true;
                            case R.id.report:
                                //handle menu3 click
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

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
                            editMessage(mEditingMessage, mMessageEditText.getText().toString());
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

       /* mUploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImage();
            }
        });*/

        // Gets channel from URL user requested
        mChannelUrl = getArguments().getString(OpenChannelListFragment.EXTRA_OPEN_CHANNEL_URL);
        refreshFirst();

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
                        mMessageEditText.append(emoticon.getUnicode());

                    }

                    @Override
                    public void onBackSpace() {
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
                        sendUserMessage(gif.getGifUrl());


                    }
                });
        emoticonGIFKeyboardFragment = EmoticonGIFKeyboardFragment
                .getNewInstance(rootView.findViewById(R.id.keyboard_container), emoticonConfig, gifConfig);
        emoj = (ImageView) rootView.findViewById(R.id.emoji_open_close_btn);
        emoj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  getActivity().getWindow().setSoftInputMode(
                        SOFT_INPUT_ADJUST_PAN);*/
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


    private void initView() {
        this.mAudioRecordButton = audio_record_button;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission granted.
                    Snackbar.make(mRootLayout, "Storage permissions granted. You can now upload or download files.",
                            Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    // Permission denied.
                    Snackbar.make(mRootLayout, "Permissions denied.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Set this as true to restart auto-background detection.
        // This means that you will be automatically disconnected from SendBird when your
        // app enters the background.
        SendBird.setAutoBackgroundDetection(true);

        if (requestCode == INTENT_REQUEST_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Log.d(LOG_TAG, "data is null!");
                return;
            }
            showUploadConfirmDialog(data.getData());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //   refresh();
        //   refreshFirst();
       /* ConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER_ID, new ConnectionManager.ConnectionManagementHandler() {
            @Override
            public void onConnected(boolean reconnect) {
                if (reconnect) {
                    refresh();
                } else {
                    refreshFirst();
                }
            }
        });*/

        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                // Add new message to view
                if (baseChannel.getUrl().equals(mChannelUrl)) {
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
        });
    }


    @Override
    public void onPause() {
        // ConnectionManager.removeConnectionManagementHandler(CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mChannel != null) {
            mChannel.exit(new OpenChannel.OpenChannelExitHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }
                }
            });
        }

        super.onDestroyView();
    }


    private void setUpChatAdapter() {
        mChatAdapter = new OpenChatAdapter(getActivity());
        mChatAdapter.setOnItemClickListener(new OpenChatAdapter.OnItemClickListener() {
            @Override
            public void onUserMessageItemClick(UserMessage message) {
            }

            @Override
            public void onFileMessageItemClick(FileMessage message) {
                onFileMessageClicked(message);
            }

            @Override
            public void onAdminMessageItemClick(AdminMessage message) {
            }
        });

        mChatAdapter.setOnItemLongClickListener(new OpenChatAdapter.OnItemLongClickListener() {
            @Override
            public void onBaseMessageLongClick(final BaseMessage message, int position) {
                showMessageOptionsDialog(message, position);
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

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      *//*  getActivity().setOnBackPressedListener(new OpenChannelActivity.onBackPressedListener() {
            @Override
            public boolean onBack() {
                if (mCurrentState == STATE_EDIT) {
                    setState(STATE_NORMAL, null, -1);
                    return true;
                }

                mIMM.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
                return false;
            }
        });*//*
    }*/

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);

        // Load more messages when user reaches the top of the current message list.
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (mLayoutManager.findLastVisibleItemPosition() == mChatAdapter.getItemCount() - 1) {
                    loadNextMessageList(CHANNEL_LIST_LIMIT);
                }
                Log.v(LOG_TAG, "onScrollStateChanged");
            }
        });
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

    private void showUploadConfirmDialog(final Uri uri) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Upload file?")
                .setPositiveButton(R.string.upload, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {

                            // Specify two dimensions of thumbnails to generate
                            List<FileMessage.ThumbnailSize> thumbnailSizes = new ArrayList<>();
                            thumbnailSizes.add(new FileMessage.ThumbnailSize(240, 240));
                            thumbnailSizes.add(new FileMessage.ThumbnailSize(320, 320));

                            sendImageWithThumbnail(uri, thumbnailSizes);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null).show();
    }

    private void requestImage() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            Intent intent = new Intent();
            // Show only images, no videos or anything else
            intent.setType("image/* video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Media"), INTENT_REQUEST_CHOOSE_IMAGE);

            // Set this as false to maintain connection
            // even when an external Activity is started.
            SendBird.setAutoBackgroundDetection(false);
        }
    }

    @SuppressLint("NewApi")
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

    private void refreshFirst() {
        enterChannel(mChannelUrl);
    }

    /**
     * Enters an Open Channel.
     * <p>
     * A user must successfully enter a channel before being able to load or send messages
     * within the channel.
     *
     * @param channelUrl The URL of the channel to enter.
     */
    private void enterChannel(String channelUrl) {
        OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    return;
                }

                // Enter the channel
                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            // Error!
                            e.printStackTrace();
                            return;
                        }

                        mChannel = openChannel;

                        if (getActivity() != null) {
                            // Set action bar title to name of channel
                            // ((Main3Activity) getActivity()).setActionBarTitle(mChannel.getName());
                            ((Main3Activity) getActivity()).setActionBarTitle(mChannel.getName());
                            //Toast.makeText(getContext(), ""+mChannel.getName(), Toast.LENGTH_SHORT).show();
                            tv_chatperson_name.setText(mChannel.getName());


                        }

                        refresh();
                    }
                });
            }
        });
    }

    private void sendUserMessage(String text) {
        mChannel.sendUserMessage(text, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Log.e(LOG_TAG, e.toString());
                    Toast.makeText(
                            getActivity(),
                            "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                // Display sent message to RecyclerView
                mChatAdapter.addFirst(userMessage);
            }
        });
    }

    /**
     * Sends a File Message containing an image file.
     * Also requests thumbnails to be generated in specified sizes.
     *
     * @param uri The URI of the image, which in this case is received through an Intent request.
     */
    private void sendImageWithThumbnail(Uri uri, List<FileMessage.ThumbnailSize> thumbnailSizes) {
        Hashtable<String, Object> info = FileUtils.getFileInfo(getActivity(), uri);
        final String path = (String) info.get("path");
        final File file = new File(path);
        final String name = file.getName();
        final String mime = (String) info.get("mime");
        final int size = (Integer) info.get("size");

        if (path.equals("")) {
            Toast.makeText(getActivity(), "File must be located in local storage.", Toast.LENGTH_LONG).show();
        } else {
            // Send image with thumbnails in the specified dimensions
            mChannel.sendFileMessage(file, name, mime, size, "", null, thumbnailSizes, new BaseChannel.SendFileMessageHandler() {
                @Override
                public void onSent(FileMessage fileMessage, SendBirdException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mChatAdapter.addFirst(fileMessage);
                }
            });
        }
    }

    private void refresh() {
        loadInitialMessageList(CHANNEL_LIST_LIMIT);
    }

    /**
     * Replaces current message list with new list.
     * Should be used only on initial load.
     */
    private void loadInitialMessageList(int numMessages) {

        mPrevMessageListQuery = mChannel.createPreviousMessageListQuery();
        mPrevMessageListQuery.load(numMessages, true, new PreviousMessageListQuery.MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    return;
                }

                mChatAdapter.setMessageList(list);
            }
        });

    }

    /**
     * Loads messages and adds them to current message list.
     * <p>
     * A PreviousMessageListQuery must have been already initialized through {@link #loadInitialMessageList(int)}
     */
    private void loadNextMessageList(int numMessages) throws NullPointerException {

        if (mChannel == null) {
            throw new NullPointerException("Current channel instance is null.");
        }

        if (mPrevMessageListQuery == null) {
            throw new NullPointerException("Current query instance is null.");
        }

        mPrevMessageListQuery.load(numMessages, true, new PreviousMessageListQuery.MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    return;
                }

                for (BaseMessage message : list) {
                    mChatAdapter.addLast((message));
                }
            }
        });
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

                refresh();
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

                refresh();
            }
        });
    }


}

