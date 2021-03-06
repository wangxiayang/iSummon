package com.isummon.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.isummon.R;
import com.isummon.data.GlobalVariables;
import com.isummon.model.UserModel;
import com.isummon.widget.ContactAdapter;
import com.isummon.widget.ProgressTaskBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建活动后弹出此界面，邀请好友参加活动。
 * 需传入活动的id。
 */
public class InviteActivity extends ISummonActivity {

    /**
     * 活动id的key
     */
    public static final String HD_ID = "hdid";

    private ArrayList<UserModel> invitedList;
    private int hdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite);
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg_black);

        Intent intent = getIntent();
        hdId = intent.getIntExtra(HD_ID, -1);

        invitedList = new ArrayList<UserModel>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new ProgressTaskBundle<Void, List<UserModel>>(
                this,
                R.string.loading_contacts
        ) {
            @Override
            protected List<UserModel> doWork(Void... params) {
                return GlobalVariables.netHelper.getAllContacts();
            }

            @Override
            protected void dealResult(List<UserModel> result) {
                if(result.size() == 0) {
                    Button submitButton = (Button) findViewById(R.id.submit_invitation);
                    submitButton.setText(R.string.no_contact);
                    submitButton.setEnabled(false);
                }
                else {
                    findViewById(R.id.contact_list).setVisibility(View.VISIBLE);
                    updateContactList(result);
                }
            }
        }.action();
    }

    private void updateContactList(List<UserModel> contacts) {
        ListView listView = (ListView) findViewById(R.id.contact_list);
        listView.setAdapter(new ContactAdapter(this, contacts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = super.getView(position, convertView, parent);
                convertView.setBackgroundResource(R.drawable.contact_item_bg_notinvited);
                TextView name = (TextView) convertView.findViewById(R.id.contact_name);
                name.setTextColor(Color.BLACK);
                return convertView;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserModel pickedContact = (UserModel) parent.getItemAtPosition(position);
                if(invitedList.contains(pickedContact)) {
                    invitedList.remove(pickedContact);
                    view.setBackgroundResource(R.drawable.contact_item_bg_notinvited);
                }
                else {
                    invitedList.add(pickedContact);
                    view.setBackgroundResource(R.drawable.contact_item_bg_invited);
                }
            }
        });
    }

    /**
     * 提交邀请的用户列表。
     * @param v
     */
    public void submitInivitation(View v) {
        new ProgressTaskBundle<Void, Integer>(
                this,
                R.string.submitting_invitation
        ) {
            @Override
            protected Integer doWork(Void... params) {
                return GlobalVariables.netHelper.invite(hdId, invitedList);
            }

            @Override
            protected void dealResult(Integer result) {
                if(result == 0) {
                    showToast(R.string.submitting_success);
                    finish();
                }
                else {
                    showToast(R.string.submitting_failed);
                }
            }
        }.action();
    }

    /**
     * 跳过邀请阶段。
     * @param v
     */
    public void skip(View v) {
        finish();
    }
}
