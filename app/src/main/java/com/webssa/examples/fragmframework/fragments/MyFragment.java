package com.webssa.examples.fragmframework.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.webssa.examples.fragmframework.AppUtils;
import com.webssa.examples.fragmframework.R;


public class MyFragment extends Fragment {
    private static final String ARGUMENT = "arguments";

    public interface OnNavigationListener {
        void onMovePrevious(int currentFragmentId);
        void onMoveNext(int currentFragmentId);
    }

    public enum PositionType {
        POSITION_START, POSITION_MIDDLE, POSITION_END
    }

    public static class Builder {
        private int id = -1;
        private String title;
        private String content;
        private int bgColorResId = -1;
        private PositionType positionType;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setBgColorResId(int bgColorResId) {
            this.bgColorResId = bgColorResId;
            return this;
        }

        public Builder setPositionType(PositionType positionType) {
            this.positionType = positionType;
            return this;
        }

        public MyFragment create() {
            if (id < 0) throw new IllegalStateException("ID of the fragment is not provided");
            if (bgColorResId <= 0) throw new IllegalStateException("Background color of the fragment is not provided");
            if (title == null) throw new IllegalStateException("Title of the fragment is not provided");
            Bundle args = new Bundle();
            args.putString(ARGUMENT, new GsonBuilder().create().toJson(this));
            MyFragment f = new MyFragment();
            f.setArguments(args);
            return f;
        }
    }

    private OnNavigationListener navListener;
    private Builder arguments;

    private View vContent;
    private ViewGroup vContainer;
    private TextView vTxtTitle;
    private TextView vTxtContent;
    private Button vBtnPrev;
    private Button vBtnNext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(getClass().getSimpleName(), "onAttach() - "+getArguments().getString(ARGUMENT));
        if (!(context instanceof OnNavigationListener)) {
            throw new IllegalStateException("A host Activity must implement the OnNavigationListener");
        }
        navListener = (OnNavigationListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String jsonArg = getArguments().getString(ARGUMENT);
        arguments = new GsonBuilder().create().fromJson(jsonArg, Builder.class);
        Log.d(getClass().getSimpleName(), arguments.id + " - onCreate()"+((savedInstanceState == null) ? "  Saved state is NULL" : "  Saved state is NOT NULL"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vContent == null) {
            Log.d(getClass().getSimpleName(), arguments.id + " - onCreateView(). View content = null");
            vContent = inflater.inflate(R.layout.fragment_my, container, false);
            vContainer = (ViewGroup) vContent.findViewById(R.id.container);
            vTxtTitle = (TextView) vContainer.findViewById(R.id.title);
            vTxtContent = (TextView) vContainer.findViewById(R.id.content);
            vBtnPrev = (Button) vContainer.findViewById(R.id.btn_prev);
            vBtnPrev.setOnClickListener(navClickListener);
            vBtnNext = (Button) vContainer.findViewById(R.id.btn_next);
            vBtnNext.setOnClickListener(navClickListener);
        } else {
            Log.d(getClass().getSimpleName(), arguments.id + " - onCreateView().  View content exists");
            container.removeView(vContent);
        }
        return vContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), arguments.id + " - onViewCreated()");
        int color = AppUtils.getColorFromResourcesCompat(getActivity().getResources(), arguments.bgColorResId, null);
        AppUtils.setBackgroundColorForViewCompat(vContainer, color);
        vTxtTitle.setText(arguments.title);
        vTxtContent.setText(arguments.content);
        updateNavigationButtonsVisibility();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getClass().getSimpleName(), arguments.id + " - onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName(), arguments.id + " - onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(), arguments.id + " - onStop()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getClass().getSimpleName(), arguments.id + " - onDetach()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName(), arguments.id + " - onDestroy()");
    }

    private void updateNavigationButtonsVisibility() {
        vBtnPrev.setVisibility(View.INVISIBLE);
        vBtnNext.setVisibility(View.INVISIBLE);
        if (arguments.positionType != null) {
            switch (arguments.positionType) {
                case POSITION_START:
                    vBtnNext.setVisibility(View.VISIBLE);
                    break;
                case POSITION_MIDDLE:
                    vBtnPrev.setVisibility(View.VISIBLE);
                    vBtnNext.setVisibility(View.VISIBLE);
                    break;
                case POSITION_END:
                    vBtnPrev.setVisibility(View.VISIBLE);
            }
        }
    }

    View.OnClickListener navClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigate(v.getId());
        }
    };

    private void navigate(int viewId) {
        switch (viewId) {
            case R.id.btn_prev:
                navListener.onMovePrevious(arguments.id);
                break;
            case R.id.btn_next:
                navListener.onMoveNext(arguments.id);
                break;
        }
    }
}
