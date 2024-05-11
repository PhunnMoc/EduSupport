package com.example.edusuport.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.edusuport.R;
import com.example.edusuport.controllers.TheLat_HSGVController;
import com.example.edusuport.model.TheLat;

import java.util.ArrayList;

public class FlashCardAdapter extends PagerAdapter {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    FrameLayout mCardBackLayout,mCardFrontLayout,click_flip;
    private boolean mIsBackVisible = false;
    Context mContext;
    ArrayList<TheLat> List= new ArrayList<TheLat>();
    TheLat_HSGVController theLatHsgvController=new TheLat_HSGVController();
    LayoutInflater layoutInflater;
    private ViewPager viewPager;

    public FlashCardAdapter(Context context, ArrayList<TheLat> list, ViewPager viewPager) {
        this.viewPager = viewPager;
        List = list;
        mContext = context;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if(position==getCount()-1){
            View endLayout = layoutInflater.inflate(R.layout.congrat_finish_fc, container, false);
            LinearLayout reload =(LinearLayout) endLayout.findViewById(R.id.btn_congrat_reload);
            LinearLayout out=(LinearLayout) endLayout.findViewById(R.id.btn_congrat_out);
            reload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Đặt lại vị trí của ViewPager về 0
                    viewPager.setCurrentItem(0);
                }
            });
            out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) mContext).onBackPressed();
                }
            });
            container.addView(endLayout);
            return endLayout;
        }
        View view=layoutInflater.inflate(R.layout.item_main_the_lat_hs,container,false);
        LinearLayout main_card=(LinearLayout) view.findViewById(R.id.main_card);

        click_flip=(FrameLayout) view.findViewById(R.id.click_card);

        FrameLayout mCardBackLayout = (FrameLayout) view.findViewById(R.id.card_back);
        TextView includedTextViewBack = mCardBackLayout.findViewById(R.id.txt_back);

        FrameLayout mCardFrontLayout = (FrameLayout) view.findViewById(R.id.card_front);
        TextView includedTextViewFront = mCardFrontLayout.findViewById(R.id.txt_front);

        includedTextViewFront.setText(List.get(position).getCauHoi());
        includedTextViewBack.setText(List.get(position).getCauTraLoi());

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.outanimation_flashcard);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.inanimation_flashcard);

        int distance = 8000;
        float scale = mContext.getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
        click_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsBackVisible) {
                    mSetRightOut.setTarget(mCardFrontLayout);
                    mSetLeftIn.setTarget(mCardBackLayout);
                    mSetRightOut.start();
                    mSetLeftIn.start();
                    mIsBackVisible = true;
                } else {
                    mSetRightOut.setTarget(mCardBackLayout);
                    mSetLeftIn.setTarget(mCardFrontLayout);
                    mSetRightOut.start();
                    mSetLeftIn.start();
                    mIsBackVisible = false;
                }
            }
        });
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }



}
