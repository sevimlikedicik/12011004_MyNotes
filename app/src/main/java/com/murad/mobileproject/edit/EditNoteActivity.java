package com.murad.mobileproject.edit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.murad.mobileproject.models.Note;
import com.murad.mobileproject.R;
import com.murad.mobileproject.models.Reminder;
import com.murad.mobileproject.util.SharedPreference;
import com.murad.mobileproject.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextTitle;
    private EditText mEditTextNote;
    private EditText mEditTextAdress;
    private CheckBox mCheckBox;
    private Button mButtonSave;
    private LinearLayout mLinearLayoutSelectColor;
    private ImageView mImageViewBack;
    private Listener listener;
    private String selectedColor;
    private CardView mCardViewColor;
    private boolean isEdit = false;
    private int position = -1;
    private List<Note> noteList;
    private TextView mTextViewAddReminder;
    private ReminderListener reminderListener;
    Note noteEdit;
    private Reminder reminderNote;
    String mediaKey;
    private ImageView mImageViewAddPhoto;
    private ImageView mImageViewAddVideo;
    private ImageView mImageViewAddAudio;
    private String selectedPhoto;
    private String selectedVideo;
    private String selectedAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        mEditTextTitle = findViewById(R.id.et_edit_note_title);
        mEditTextNote = findViewById(R.id.et_edit_note_content);
        mEditTextAdress = findViewById(R.id.et_edit_note_adress);
        mCheckBox = findViewById(R.id.checkbox);
        mButtonSave = findViewById(R.id.btn_edit_note_save);
        mLinearLayoutSelectColor = findViewById(R.id.ll_edit_note_select_color);
        mCardViewColor = findViewById(R.id.cv_edit_note_color);
        mTextViewAddReminder = findViewById(R.id.tv_edit_note_add_reminder);
        mImageViewBack = findViewById(R.id.iv_edit_note_back);
        mImageViewAddPhoto = findViewById(R.id.iv_edit_note_add_photo);
        mImageViewAddVideo = findViewById(R.id.iv_edit_note_add_video);
        mImageViewAddAudio = findViewById(R.id.iv_edit_note_add_audio);

        mButtonSave.setOnClickListener(this);
        mLinearLayoutSelectColor.setOnClickListener(this);
        mTextViewAddReminder.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);
        mImageViewAddPhoto.setOnClickListener(this);
        mImageViewAddVideo.setOnClickListener(this);
        mImageViewAddAudio.setOnClickListener(this);

        if (getIntent().getExtras() != null){
            noteEdit = (Note) getIntent().getExtras().getSerializable("noteItem");
            position = getIntent().getExtras().getInt("position");
            mEditTextTitle.setText(noteEdit.getTitle());
            mEditTextNote.setText(noteEdit.getContent());
            mEditTextAdress.setText(noteEdit.getAdress());
            mCheckBox.setChecked(noteEdit.isPrior());
            mCardViewColor.setCardBackgroundColor(Color.parseColor(noteEdit.getColor()));
            if (noteEdit.getPhotoUri() != null){
                mImageViewAddPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_camera_black_24dp));
                selectedPhoto = noteEdit.getPhotoUri();
            }
            if (noteEdit.getAudioUri() != null){
                mImageViewAddAudio.setImageDrawable(getResources().getDrawable(R.drawable.ic_library_music_black_24dp));                 selectedVideo = noteEdit.getAudioUri();
            }
            if (noteEdit.getVideoUri() != null){
                mImageViewAddVideo.setImageDrawable(getResources().getDrawable(R.drawable.ic_ondemand_video_black_24dp));                 selectedVideo = noteEdit.getVideoUri();
            }
            reminderNote = noteEdit.getReminder();
            selectedColor = noteEdit.getColor();
            isEdit = true;
        }

        listener = new Listener() {
            @Override
            public void onItemClick(String colorCode) {
                selectedColor = colorCode;
                mCardViewColor.setCardBackgroundColor(Color.parseColor(colorCode));
            }
        };

        reminderListener = new ReminderListener() {
            @Override
            public void onReminderInfo(Reminder reminder) {
                reminderNote = reminder;
            }
        };

    }
    private void saveNotes(){
        try {
            noteList = Note.parse(EditNoteActivity.this, new JSONArray(SharedPreference.getInstance(EditNoteActivity.this).getNotes()));
        } catch (JSONException e) {
            noteList = new ArrayList<>();
        }

        if (isEdit){
            noteList.remove(position);
        }
        Note note = new Note();
        note.setTitle(mEditTextTitle.getText().toString());
        note.setContent(mEditTextNote.getText().toString());
        note.setAdress(mEditTextAdress.getText().toString());
        note.setPrior(mCheckBox.isChecked());
        note.setDateTime(new Date());
        note.setColor(selectedColor);
        note.setStringDate(Utils.dateFormatter(note.getDateTime()));
        note.setReminder(reminderNote);
        note.setAudioUri(selectedAudio);
        note.setPhotoUri(selectedPhoto);
        note.setVideoUri(selectedVideo);
        if (note.getReminder() != null){
            setNotification(note);
        }
        noteList.add(0,note);

        Gson gson = new Gson();
        SharedPreference.getInstance(EditNoteActivity.this).setNotes(gson.toJson(noteList));

        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_note_save :
                saveNotes();
                break;
            case R.id.iv_edit_note_back :
                onBackPressed();
                break;
            case R.id.ll_edit_note_select_color :
                SelectColorDialog selectColorDialog = new SelectColorDialog(EditNoteActivity.this, listener);
                selectColorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                selectColorDialog.setCancelable(true);
                selectColorDialog.show();
                break;
            case R.id.tv_edit_note_add_reminder :
                AddReminderDialog addReminderDialog = new AddReminderDialog(EditNoteActivity.this,reminderNote, reminderListener);
                addReminderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addReminderDialog.setCancelable(false);
                addReminderDialog.show();
                break;
            case R.id.iv_edit_note_add_photo :
                if (noteEdit != null && noteEdit.getPhotoUri() != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(noteEdit.getPhotoUri())));

                } else {
                    mediaKey = "image/*" ;
                    startFilesAction(mediaKey);
                }
                break;
            case R.id.iv_edit_note_add_video :
                if (noteEdit != null && noteEdit.getVideoUri() != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(noteEdit.getVideoUri())));

                } else {
                    mediaKey = "video/*";
                    startFilesAction(mediaKey);
                }
                break;
            case R.id.iv_edit_note_add_audio :
                if (noteEdit != null && noteEdit.getAudioUri() != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(noteEdit.getAudioUri())));

                } else {
                    mediaKey = "audio/*";
                    startFilesAction(mediaKey);
                }
                break;
        }
    }

    private void startFilesAction(String key){
        if (checkIfAlreadyHavePermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            pickImageFromFiles(key);
        } else {
            requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_PERMISSION);
        }
    }

    private void setNotification(Note noteItem){
        Reminder reminderItem = noteItem.getReminder();
        String[] arrTime = reminderItem.getTime().split(":");
        String hour = arrTime[0];
        String min = arrTime[1];

        String[] arrDate = reminderItem.getDate().split("-");

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.DAY_OF_MONTH, Integer.valueOf(arrDate[0]));
        calSet.set(Calendar.MONTH, Integer.valueOf(arrDate[1]) - 1);
        calSet.set(Calendar.YEAR, Integer.valueOf(arrDate[2]));
        calSet.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calSet.set(Calendar.MINUTE, Integer.parseInt(min));
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        if (calSet.compareTo(calNow) <= 0) {
            calSet.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(EditNoteActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EditNoteActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (reminderItem.getPeriod() != 0){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), 60*60*24*1000*reminderItem.getPeriod(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,calSet.getTimeInMillis(),pendingIntent);
        }
    }

    int PICK_FILES_REQUEST = 1000;
    private static final int REQUEST_READ_PERMISSION = 786;
    public static final int GALLERY_PERMISSION =101;

    private void requestPermission(String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    private boolean checkIfAlreadyHavePermission(String permissionType) {
        int result = ActivityCompat.checkSelfPermission(EditNoteActivity.this, permissionType);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILES_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (mediaKey.equals("image/*")){
                mImageViewAddPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_camera_black_24dp));
                selectedPhoto = uri.toString();
            } else if (mediaKey.equals("video/*")){
                mImageViewAddVideo.setImageDrawable(getResources().getDrawable(R.drawable.ic_ondemand_video_black_24dp));
                selectedVideo = uri.toString();
            } else {
                mImageViewAddAudio.setImageDrawable(getResources().getDrawable(R.drawable.ic_library_music_black_24dp));
                selectedAudio = uri.toString();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GALLERY_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromFiles(mediaKey);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void pickImageFromFiles(String key) {
        Intent intent = new Intent();
        intent.setType(key);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Files"), PICK_FILES_REQUEST);
    }
}
