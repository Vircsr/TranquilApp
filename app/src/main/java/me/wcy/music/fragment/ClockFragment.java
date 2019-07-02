package me.wcy.music.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import me.wcy.music.application.AppCache;
import me.wcy.music.model.Clock;
import java.util.ArrayList;

import me.wcy.music.R;
import me.wcy.music.activity.SetAlarmActivity;
import me.wcy.music.adapter.ClockListAdapter;
import me.wcy.music.utils.binding.Bind;

public class ClockFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    @Bind(R.id.clockadd)
    public ImageView clockadd;
    @Bind(R.id.clock_List)
    public ListView clockListView;
    private Context mcontext;
    private OnFragmentInteractionListener mListener;
    private int number=0;
    private int hour;
    private int minute;
    private ClockListAdapter adapter;
    Switch itemswitch;
//    public ClockFragment() {
//        // Required empty public constructor
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
//        ViewUtils.inject(this);
        clockadd = view.findViewById(R.id.clockadd);
        clockListView = view.findViewById(R.id.clock_List);
        adapter = new ClockListAdapter(AppCache.get().getmClockList());
        clockListView.setAdapter(adapter);
        clockListView.setOnItemClickListener((parent, view1, position, id) -> {

            View itemview=clockListView.getChildAt(position-clockListView.getFirstVisiblePosition());
            itemswitch= itemview.findViewById(R.id.clock_statebutton);
            number=position-parent.getFirstVisiblePosition();//number为列表中的第几项，从0开始；
            TextView clockringtime=itemview.findViewById(R.id.clock_ringtime);
            Intent intent=new Intent(getActivity(),SetAlarmActivity.class);
            intent.putExtra("itemid",number+"th");
            //Toast.makeText(getActivity(), "dsj"+number, Toast.LENGTH_SHORT).show();
            startActivity(intent);

        });
        clockadd.setOnClickListener((View.OnClickListener) this);
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clockadd:// 新建界面
                startActivity(new Intent(getActivity(), SetAlarmActivity.class));
                break;
        }
    }


    public Clock addClock(int hour, int minute){        //闹钟界面添加闹钟
       Clock clock = new Clock();
        clock.setTime(hour, minute);
        return clock;
    }

    public ArrayList<Clock> loadClockList() {
        ArrayList<Clock> clockArrayList = new ArrayList<>();
        // String key="KEY_CLOCK_"+0;
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        /*SharedPreferences sp = getContext().getSharedPreferences("SP_CLOCK",MODE_PRIVATE);                     //创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        String clockJson = sp.getString(key,"");
        if(clockJson!="")  //防空判断
        {
            Gson gson = new Gson();
            Clock clock=new Clock();
            // Toast.makeText(getApplicationContext(),clockJson,Toast.LENGTH_LONG).show();
            clock = gson.fromJson(clockJson, Clock.class);
            Toast.makeText(getContext(),0+"fragment"+clock.getTime(),Toast.LENGTH_LONG).show();//将json字符串转换成 people对象            clockArrayList.add(clock);xiaozhiqiang
        }*/

        return clockArrayList;
    }

    @Override
    public void onResume() {
        super.onResume();
        clockListView.setAdapter(new ClockListAdapter(getActivity(), loadClockList()));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Clock clock = AppCache.get().getmClockList().get(position);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    public void onRestoreInstanceState(final Bundle savedInstanceState) {

    }
}
