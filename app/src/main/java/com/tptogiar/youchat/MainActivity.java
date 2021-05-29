package com.tptogiar.youchat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tptogiar.youchat.fragment.Fragment_contact;
import com.tptogiar.youchat.fragment.Fragment_find;
import com.tptogiar.youchat.fragment.Fragment_me;
import com.tptogiar.youchat.fragment.Fragment_youchat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout bottom_youchat,bottom_contact,bottom_find,bottom_me;
    RelativeLayout curSelect;
    ViewPager2 mainViewPager =null;
    RelativeLayout mainActivity=null;

    ArrayList<Fragment> fragments=new ArrayList<>();



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        init();


    }

    @SuppressLint("WrongViewCast")
    private void init() {
        fragments.add(new Fragment_youchat());
        fragments.add(new Fragment_contact());
        fragments.add(new Fragment_find());
        fragments.add(new Fragment_me());
        for (int i = 0; i < fragments.size(); i++) {
            System.out.println("xxxx  "+fragments.get(i));
        }
        bottom_youchat=(RelativeLayout) findViewById(R.id.bottom_youchat);
        bottom_youchat.setOnClickListener(this);
        bottom_contact=(RelativeLayout) findViewById(R.id.bottom_contact);
        bottom_contact.setOnClickListener(this);
        bottom_find=(RelativeLayout) findViewById(R.id.bottom_find);
        bottom_find.setOnClickListener(this);
        bottom_me=(RelativeLayout) findViewById(R.id.bottom_me);
        bottom_me.setOnClickListener(this);
        bottom_youchat.setSelected(true);

        mainActivity = (RelativeLayout) findViewById(R.id.mainActivity);
        mainViewPager =findViewById(R.id.myViewPager2);
        //设置左右预加载的个数为：3，这样可以避免用户刚打开时
        // 就立即点"我"分页而造成空指针异常，
        // 当然在changePager中fragments.get(position).getView()获
        //取view后判断一下是不是空指针，是的话return也可以实现同样效果
        mainViewPager.setOffscreenPageLimit(3);



        curSelect=bottom_youchat;


        mainViewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }
            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });

        mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changePager(position);
            }
        });


    }


    @Override
    public void onClick(View v) {
        curSelect.setSelected(false);
        switch (v.getId()){
            case R.id.bottom_youchat:
                bottom_youchat.setSelected(true);
                mainViewPager.setCurrentItem(0);
                curSelect=bottom_youchat;
                break;

            case R.id.bottom_contact:
                bottom_contact.setSelected(true);
                mainViewPager.setCurrentItem(1);
                curSelect=bottom_contact;
                break;

            case R.id.bottom_find:
                bottom_find.setSelected(true);
                mainViewPager.setCurrentItem(2);
                curSelect=bottom_find;
                break;
            case R.id.bottom_me:
                bottom_me.setSelected(true);
                mainViewPager.setCurrentItem(3);
                curSelect=bottom_me;
                break;
        }

    }


    /**
     * @Author: Tptogiar
     * @Description:
     * 1.将viewpager的切换转换为对应的点击事件
     * 2.找到fragment最外层元素的背景颜色color,
     *      将mainActivity的背景颜色color
     *
     * 由于状态栏的颜色只会跟随主界面下最外层颜色，
     * 为了达到不同的viewPager对应不同的状态栏颜色的效果，所有需要
     * 在viewpager改变的时候改变主界面下最外层的颜色
     * @Date: 2021/5/28-20:26
     */
    private void changePager(int position) {
        View viewById=null;
        //当前content为：setContentView(R.layout.activity_main);
        //拿不到fragment中的元素，且viewpager在某一pager时，也拿不到别的fragment的元素
        // 所以为了拿到不同fragment的元素，所以先从fragments中拿到对应的fragment的View后
        //再获取fragment中元素
        View fragment_view = fragments.get(position).getView();
        if(position==0){
            onClick(bottom_youchat);
            viewById = fragment_view.findViewById(R.id.top_youchat);
        }
        else if(position==1){
            onClick(bottom_contact);
            viewById = fragment_view.findViewById(R.id.top_contact);
        }
        else if(position==2){
            onClick(bottom_find);
            viewById = fragment_view.findViewById(R.id.top_find);
        }
        else if(position==3){
            onClick(bottom_me);
            viewById = fragment_view.findViewById(R.id.top_me);
        }
        int color = ((ColorDrawable) viewById.getBackground()).getColor();
        //将主mainActivity的背景颜色设置为当前Fragment顶部的颜色，
        //以实现状态栏和fragment一体的效果
        mainActivity.setBackgroundColor(color);
    }
}