package com.androidpay.view.calander;

import android.databinding.DataBindingUtil;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.androidpay.R;
import com.androidpay.utils.Utils;
import com.androidpay.view.BaseFragment;

import java.util.List;

/**
 * Created by Anuj Sharma on 3/27/2017.
 */

public class CalanderFragment extends BaseFragment implements  WeekView.EventClickListener, WeekView.EventLongPressListener{
    private View rootView;
    private WeekView mWeekView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calander,container,false);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView)rootView. findViewById(R.id.weekView);


        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Utils.getInstance().showToast("click event");
            }
        });

        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                Utils.getInstance().showToast("month change listener");
                return null;
            }
        });

        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
                Utils.getInstance().showToast("long press");
            }
        });
// Set an action when any event is clicked.
//        mWeekView.setOnEventClickListener(mEventClickListener);

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
//        mWeekView.setMonthChangeListener(mMonthChangeListener);

// Set long press listener for events.
//        mWeekView.setEventLongPressListener(mEventLongPressListener);
        return rootView;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }
}
