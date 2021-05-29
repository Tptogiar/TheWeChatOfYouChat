package com.tptogiar.youchat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.tptogiar.youchat.adapter.ContactListAdapter;
import com.tptogiar.youchat.adapter.FragmentAtapter;
import com.tptogiar.youchat.adapter.ChattingListAdapter;
import com.tptogiar.youchat.fragment.Fragment_contact;
import com.tptogiar.youchat.fragment.Fragment_find;
import com.tptogiar.youchat.fragment.Fragment_me;
import com.tptogiar.youchat.fragment.Fragment_youchat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //用来记录当前选择了底部的哪一个标签
    RelativeLayout curSelectBottom;
    //用来记录四个fragment
    ArrayList<Fragment> fragments=new ArrayList<>();
    ArrayList<User> users=new ArrayList<>();
    RelativeLayout bottom_youchat,bottom_contact,bottom_find,bottom_me;
    ViewPager2 mainViewPager =null;
    RelativeLayout mainActivity=null;
    RecyclerView youchatRecycle =null;
    RecyclerView contactRecycle=null;
    //弹出菜单
    private PopupWindow mPopWindow;

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


    private void init() {

        loadFragment();
        loadData();
        findViewAndSetListener();

        bottom_youchat.setSelected(true);

        //设置左右预加载的个数为：3，这样可以避免用户刚打开时
        // 就立即点"我"分页而造成空指针异常，
        // 当然在changePager中fragments.get(position).getView()获
        //取view后判断一下是不是空指针，是的话return也可以实现同样效果
        mainViewPager.setOffscreenPageLimit(3);

        curSelectBottom =bottom_youchat;

        //给viewPager设置适配器
        FragmentAtapter fragmentAtapter = new FragmentAtapter(this, fragments);
        mainViewPager.setAdapter(fragmentAtapter);
        mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changePager(position);
                //此时的fragment已经onCreate了，所以在此处getView以及setAdapter
                setAdapter();

            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        curSelectBottom.setSelected(false);
        switch (v.getId()){
            case R.id.bottom_youchat:
                bottom_youchat.setSelected(true);
                mainViewPager.setCurrentItem(0);
                curSelectBottom =bottom_youchat;
                break;

            case R.id.bottom_contact:
                bottom_contact.setSelected(true);
                mainViewPager.setCurrentItem(1);
                curSelectBottom =bottom_contact;
                break;

            case R.id.bottom_find:
                bottom_find.setSelected(true);
                mainViewPager.setCurrentItem(2);
                curSelectBottom =bottom_find;
                break;
            case R.id.bottom_me:
                bottom_me.setSelected(true);
                mainViewPager.setCurrentItem(3);
                curSelectBottom =bottom_me;
                break;

            case R.id.btn_add:
                showPopupWindow(v);
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changePager(int position) {
        View topView=null;
        Button btn_add=null;
        //当前content为：setContentView(R.layout.activity_main);
        //拿不到fragment中的元素，且viewpager在某一pager时，也拿不到别的fragment的元素
        // 所以为了拿到不同fragment的元素，所以先从fragments中拿到对应的fragment的View后
        //再获取fragment中元素
        View fragment_view = fragments.get(position).getView();
        btn_add=fragment_view.findViewById(R.id.btn_add);
        if(position==0){
            onClick(bottom_youchat);
            topView = fragment_view.findViewById(R.id.top_youchat);
        }
        else if(position==1){
            onClick(bottom_contact);
            topView = fragment_view.findViewById(R.id.top_contact);
        }
        else if(position==2){
            onClick(bottom_find);
            topView = fragment_view.findViewById(R.id.top_find);
        }
        else if(position==3){
            onClick(bottom_me);
            topView = fragment_view.findViewById(R.id.top_me);
        }
        int color = ((ColorDrawable) topView.getBackground()).getColor();
        //将主mainActivity的背景颜色设置为当前Fragment顶部的颜色，
        //以实现状态栏和fragment一体的效果
        mainActivity.setBackgroundColor(color);

        //除去第四个tap之外在fragment中btn_add设置按键点击事件
        if(btn_add!=null){
            btn_add.setOnClickListener(this);
        }
    }

    /**
     * @Author: Tptogiar
     * @Description: 加载示例数据
     * @Date: 2021/5/29-22:08
     */
    public void loadData(){
        users.add(new User("鹦鹉兄弟",R.mipmap.ic_avatars_duck,"鲁迅说少喝奶茶，多喝狂犬水"));
        users.add(new User("鲁迅",R.mipmap.ic_avatars_luxun,"这话我没说过"));
        users.add(new User("不是你的微信好友",R.mipmap.ic_avatars_young_man,"请签收你的月亮"));
        users.add(new User("我的电脑",R.mipmap.ic_avatars_computer,"你压到我腿毛了"));
        users.add(new User("5:00am",R.mipmap.ic_avatars_dog1,"天气热了，我不再是单身狗了，我是热狗"));
        users.add(new User("隐形的鸡翅膀",R.mipmap.ic_avatars_duck4,"..."));
        users.add(new User("唐曾",R.mipmap.ic_avatars_young_man2,"徒儿前面是火焰山吗？"));
        users.add(new User("老孙",R.mipmap.ic_avatars_young_man3,"不，前面是广东"));
        users.add(new User("加里敦大学学生",R.mipmap.ic_avatars_dog2,"身价一直没公布，因为最近不稳定，有时梦不到"));
        users.add(new User("白雪公主后妈",R.mipmap.ic_avatars_gril1,""));
        users.add(new User("香蕉不呐呐",R.mipmap.ic_avatars_duck5,"想做个园丁 然后去你们心里种点b树"));
    }

    /**
     * @Author: Tptogiar
     * @Description: 找到界面元素
     * @Date: 2021/5/29-22:09
     */
    public void findViewAndSetListener(){
        bottom_youchat=(RelativeLayout) findViewById(R.id.bottom_youchat);
        bottom_contact=(RelativeLayout) findViewById(R.id.bottom_contact);
        bottom_find=(RelativeLayout) findViewById(R.id.bottom_find);
        bottom_me=(RelativeLayout) findViewById(R.id.bottom_me);
        bottom_youchat.setOnClickListener(this);
        bottom_contact.setOnClickListener(this);
        bottom_find.setOnClickListener(this);
        bottom_me.setOnClickListener(this);

        mainActivity = (RelativeLayout) findViewById(R.id.mainActivity);
        mainViewPager =(ViewPager2) findViewById(R.id.myViewPager2);
    }

    /**
     * @Author: Tptogiar
     * @Description: 加载Fragment
     * @Date: 2021/5/29-22:09
     */
    public void loadFragment(){
        fragments.add(new Fragment_youchat());
        fragments.add(new Fragment_contact());
        fragments.add(new Fragment_find());
        fragments.add(new Fragment_me());
    }

    /**
     * @Author: Tptogiar
     * @Description: 配置适配器
     * @Date: 2021/5/29-22:09
     */
    private void setAdapter() {
        View youchat_view = fragments.get(0).getView();
        youchatRecycle =youchat_view.findViewById(R.id.chattingList);
        youchatRecycle.setLayoutManager(new LinearLayoutManager(this));
        youchatRecycle.setAdapter(new ChattingListAdapter(this,users));

        View contact_view = fragments.get(1).getView();
        contactRecycle=contact_view.findViewById(R.id.contactList);
        contactRecycle.setLayoutManager(new LinearLayoutManager(this));
        contactRecycle.setAdapter(new ContactListAdapter(this,users));
    }


    /**
     * @Author: Tptogiar
     * @Description: 弹出菜单
     * @Date: 2021/5/29-22:09
     */
    private void showPopupWindow(View v) {
        //获取contentView
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popuplayout, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.showAsDropDown(v,-430,20);
    }


}