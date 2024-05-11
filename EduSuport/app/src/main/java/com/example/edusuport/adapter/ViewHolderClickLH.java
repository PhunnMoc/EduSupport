package com.example.edusuport.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edusuport.R;
import com.example.edusuport.model.LopHoc;

import java.util.ArrayList;
import java.util.Collections;

public class ViewHolderClickLH implements RecyclerView.OnItemTouchListener{
    private OnItemClickListener mListener;

    private LopHocAdapter mAdapter;
    String  idLop;
    public interface OnItemClickListener {
        public void onItemClick(View view, int position, String id);

        public void onLongItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public ViewHolderClickLH(Context context,  RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                mAdapter= (LopHocAdapter) recyclerView.getAdapter();
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                TextView ten=child.findViewById(R.id.txvTenLop);
                TextView id=child.findViewById(R.id.txvSiSo);
                idLop=id.getText().toString();

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                //Lỡ nhấn trúng lỗi, mà đ sài nên t tắt
               // child.findViewById(R.id.ten_navLopHoc).setBackgroundResource(R.drawable.background_subject_radius_click);
                if (child != null && mListener != null) {

                }
            }

        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView),idLop);
//            mAdapter.itemFocusList=  new ArrayList<>(Collections.nCopies(mAdapter.itemFocusList.size(), false));
            mAdapter.setItemFocus( view.getChildAdapterPosition(childView),true);
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
}