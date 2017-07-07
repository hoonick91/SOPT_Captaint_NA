package org.sopt.captainna.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.sopt.captainna.R;
import org.sopt.captainna.activity.MakeGroupActivity;
import org.sopt.captainna.activity.SearchGroupActivity;

/**
 * Created by minjeong on 2017-06-29.
 */

public class GroupDialog extends Dialog {

    private LinearLayout addGroup;
    private LinearLayout findGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.7f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_group);

        setLayout(); // id연결
        clickListener();

    }

    public GroupDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    private void clickListener(){
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MakeGroupActivity.class);
                getContext().startActivity(intent);
                dismiss();
            }
        });
        findGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchGroupActivity.class);
                getContext().startActivity(intent);
                dismiss();
            }
        });

    }
    private void setLayout(){
        addGroup = (LinearLayout) findViewById(R.id.addGroup);
        findGroup = (LinearLayout) findViewById(R.id.findGroup);
    }

}

