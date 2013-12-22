package com.isummon.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isummon.R;
import com.isummon.model.MyInvitation;

import java.util.ArrayList;

/**
 * Created by horz on 12/22/13.
 */
public class InvitationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MyInvitation> invitations;

    public InvitationAdapter(Context context, ArrayList<MyInvitation> invitations) {
        this.context = context;
        this.invitations = invitations;
    }

    @Override
    public int getCount() {
        return invitations.size();
    }

    @Override
    public MyInvitation getItem(int i) {
        return invitations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.invitation_item,null);
        }
        ImageView avatarView = (ImageView) view.findViewById(R.id.targetAvatar);
        avatarView.setImageResource(R.drawable.head);

        TextView contentText = (TextView) view.findViewById(R.id.invitation_content);
        MyInvitation myInvitation = getItem(position);
        contentText.setText("邀请" + myInvitation.getTargetName() + "\"" + myInvitation.getHdName() + "\"");

        TextView statusText = (TextView) view.findViewById(R.id.invitation_status);
        statusText.setText("邀请状态：" + myInvitation.getStatus().getChn());

        return view;
    }
}
