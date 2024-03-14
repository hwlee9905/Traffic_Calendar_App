package com.example.btnavbar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragCal extends Fragment {
    private View view;

    static public FloatingActionButton fab_main, fab_sub1, fab_sub2;

    private Animation fab_open, fab_close;

    private boolean isFabOpen = false;
    private String TAG = "프래그먼트";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.frag_calendar, container, false);
        fab_open = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_open);

        fab_close = AnimationUtils.loadAnimation(view.getContext(), R.anim.fab_close);



        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);

        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);

        fab_sub2 = (FloatingActionButton) view.findViewById(R.id.fab_sub2);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();
            }
        });

        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();
            }
        });

        fab_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();
            }
        });
        return view;
    }
    private void toggleFab() {

        if (isFabOpen) {



            fab_sub1.startAnimation(fab_close);

            fab_sub2.startAnimation(fab_close);

            fab_sub1.setClickable(false);

            fab_sub2.setClickable(false);

            isFabOpen = false;

        } else {



            fab_sub1.startAnimation(fab_open);

            fab_sub2.startAnimation(fab_open);

            fab_sub1.setClickable(true);

            fab_sub2.setClickable(true);

            isFabOpen = true;

        }

    }
}
