package com.murad.mobileproject.main;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.murad.mobileproject.R;
import com.murad.mobileproject.models.Note;
import com.murad.mobileproject.util.SharedPreference;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends BaseAdapter implements Filterable {
    private LayoutInflater mInflater;
    private List<Note> noteList;
    private List<Note> noteListFilter;
    private Boolean isDelete;
    private Context context;


    public NotesAdapter(Context context, List<Note> noteList, Boolean isDelete) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.noteList = noteList;
        this.noteListFilter = noteList;
        this.isDelete = isDelete;
        this.context = context;
    }

    @Override
    public int getCount() {
        return noteListFilter.size();
    }

    @Override
    public Note getItem(int position) {
        return noteListFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View lineView;
        final Note note = noteListFilter.get(position);
        lineView = mInflater.inflate(R.layout.item_note, null);
        TextView mTextViewTitle = lineView.findViewById(R.id.tv_note_item_note_title);
        TextView mTextViewContent = lineView.findViewById(R.id.tv_note_item_note_content);
        TextView mTextViewDate = lineView.findViewById(R.id.tv_note_item_note_date);
        ImageView mImageViewDelete = lineView.findViewById(R.id.iv_main_delete_note);
        RelativeLayout relativeLayout = lineView.findViewById(R.id.rl_note_item);

        if (isDelete){
            mImageViewDelete.setVisibility(View.VISIBLE);
        } else {
            mImageViewDelete.setVisibility(View.GONE);
        }

        mImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListFilter.remove(position);
                for (int i = 0; i < noteList.size() ; i++) {
                    if (noteList.get(i).equals(note)){
                        noteList.remove(i);
                    }
                }
                Gson gson = new Gson();
                SharedPreference.getInstance(context).setNotes(gson.toJson(noteList));
                notifyDataSetChanged();
            }

        });

        mTextViewTitle.setText(note.getTitle());
        mTextViewContent.setText(note.getContent());
        mTextViewDate.setText(note.getStringDate());
        relativeLayout.setBackgroundColor(Color.parseColor(note.getColor()));


        return lineView;
    }

    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Note> noteFilteredList = new ArrayList<Note>();
                if (noteList == null) {
                    noteList = new ArrayList<Note>(noteListFilter);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = noteList.size();
                    results.values = noteList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < noteList.size(); i++) {
                        String adress = noteList.get(i).getAdress();
                        String date = noteList.get(i).getStringDate();
                        if (adress.toLowerCase().contains(constraint.toString()) || date.toLowerCase().contains(constraint.toString())) {
                            noteFilteredList.add(noteList.get(i));
                        }
                    }
                    results.count = noteFilteredList.size();
                    results.values = noteFilteredList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                noteListFilter = (ArrayList<Note>) results.values;
                Integer count = (Integer) results.count;

                notifyDataSetChanged();
            }
        };
        return filter;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public void clearAdapter() {
        noteList.clear();
        notifyDataSetChanged();
    }
}
