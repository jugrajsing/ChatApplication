package com.example.hp.chatapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hp.chatapplication.Adapter.AllContactsAdapter;
import com.example.hp.chatapplication.ModelClasses.ContactVO;

import java.util.ArrayList;
import java.util.List;

public class AllContacts extends AppCompatActivity {
    RecyclerView rvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        setContentView(R.layout.activity_all_contacts);

        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        getAllContacts();
    }

    private void getAllContacts() {
        List<ContactVO> contactVOList = new ArrayList();
        ContactVO contactVO;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String img = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_THUMBNAIL_URI));

                    contactVO = new ContactVO();
                    contactVO.setContactName(name);
                    contactVO.setContactImage(img);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    contactVOList.add(contactVO);
                }
            }

            AllContactsAdapter contactAdapter = new AllContactsAdapter(contactVOList, getApplicationContext());
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            rvContacts.setAdapter(contactAdapter);
        }
    }
}