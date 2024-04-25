package com.example.regapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.regapp.R;
import com.example.regapp.dao.MahasiswaDao;
import com.example.regapp.model.Mahasiswa;

import java.util.List;

public class MahasiswaAdapter extends BaseAdapter implements MahasiswaDao {
    public static final String MODE = "mode";
    public static final String MAHASISWA = "mahasiswa";
    public static final String MAHASISWAS = "mahasiswas";
    public static final String POSITION = "position";
    public static final int ADD_MODE = 0;
    public static final int VIEW_MODE = 1;
    public static final int EDIT_MODE = 2;

    private final List<Mahasiswa> mlist;
    private final Context mContext;

    public MahasiswaAdapter(List<Mahasiswa> mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_layout, parent, false);

        TextView tvName = convertView.findViewById(R.id.tv_name);
        tvName.setText(mlist.get(position).getName());
        return convertView;
    }

    @Override
    public void insert(Mahasiswa mahasiswa) {
        mlist.add(mahasiswa);
    }

    @Override
    public void update(int i, Mahasiswa mahasiswa) {
        mlist.set(i, mahasiswa);
    }

    @Override
    public void delete(int i) {
        mlist.remove(i);
    }

    @Override
    public Mahasiswa getMahasiswaById(int id) {
        return mlist.get(id);
    }

    @Override
    public List<Mahasiswa> getAllMahasiswa() {
        return mlist;
    }
}
