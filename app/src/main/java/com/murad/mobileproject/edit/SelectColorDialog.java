package com.murad.mobileproject.edit;

import com.murad.mobileproject.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SelectColorDialog extends Dialog implements View.OnClickListener{

    private TextView mTextViewYellow;
    private TextView mTextViewRed;
    private TextView mTextViewGreen;
    private TextView mTextViewBlue;
    private Listener listener;

    public SelectColorDialog(Context context, Listener listener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_color);
        this.listener = listener;

        mTextViewYellow = findViewById(R.id.tv_yellow);
        mTextViewRed = findViewById(R.id.tv_red);
        mTextViewGreen = findViewById(R.id.tv_green);
        mTextViewBlue = findViewById(R.id.tv_blue);

        mTextViewYellow.setOnClickListener(this);
        mTextViewRed.setOnClickListener(this);
        mTextViewGreen.setOnClickListener(this);
        mTextViewBlue.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yellow :
                listener.onItemClick("#FCF860");
                break;
            case R.id.tv_red :
                listener.onItemClick("#FC6060");
                break;
            case R.id.tv_green :
                listener.onItemClick("#C3FC60");
                break;
            case R.id.tv_blue :
                listener.onItemClick("#60C6FC");
                break;
        }
        dismiss();
    }


}
