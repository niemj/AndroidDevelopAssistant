package com.example.jun.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.example.jun.myapplication.R;
import com.example.jun.myapplication.source.NavigationBar;
import com.example.jun.myapplication.util.PermissionUtils;
import com.example.jun.myapplication.util.PopupPickerHelper;
import com.example.jun.myapplication.widget.commonview.CommonEditText;
import com.example.jun.myapplication.widget.commonview.CommonTitleItemView;
import com.mylhyl.acp.AcpListener;

import java.util.ArrayList;
import java.util.List;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {

    private NavigationBar mNavigationBar;
    private CommonTitleItemView mLoanUseCTIV;
    private CommonEditText mContactNameCET;
    private CommonEditText mContactPhoneCET;
    private ImageView mContactIV;
    private PopupPickerHelper mPopuUtils;
    private View mRootView;
    private List<String> mLoanUseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        mLoanUseCTIV = findViewById(R.id.ctiv_loan_use);
        mContactNameCET = findViewById(R.id.cet_contact_name);
        mContactPhoneCET = findViewById(R.id.cet_contact_phone);
        mContactIV = mContactNameCET.getTitleIndicateIv();
        mRootView = findViewById(R.id.rootView);
        mPopuUtils = new PopupPickerHelper(this, mRootView);

        mContactIV.setOnClickListener(this);
        mLoanUseCTIV.setOnClickListener(this);
        initData();
    }

    private void initData() {

        mLoanUseList = new ArrayList<>();
        mLoanUseList.add("旅游");
        mLoanUseList.add("手机数码");
        mLoanUseList.add("教育");
        mLoanUseList.add("装修");
        mLoanUseList.add("美容");
        mLoanUseList.add("餐饮");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ctiv_loan_use:
                mPopuUtils.showLoanUsePopuWindow(mLoanUseList);
                break;
            case R.id.iv_title_indicate:
                //TODO 通讯录
                PermissionUtils.requestPermissions(this, new AcpListener() {
                    @Override
                    public void onGranted() {
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_PICK);
                        i.setData(ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(i, 0x123);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {

                    }
                }, Manifest.permission.READ_CONTACTS);
                break;
            default:
        }

    }

    public void openContactsResult(Intent data) {
        String name = "";
        String phoneNumber = "";
        if (data == null) {
            return;
        }
        Uri contactData = data.getData();
        if (contactData == null) {
            return;
        }
        Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            if (hasPhone.equalsIgnoreCase("1")) {
                hasPhone = "true";
            } else {
                hasPhone = "false";
            }
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + id, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }
            cursor.close();
        }
        mContactNameCET.setValue(name);
        mContactPhoneCET.setValue(phoneNumber);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0x123:
                    openContactsResult(data);
                    break;
                default:
            }
        }
    }
}
