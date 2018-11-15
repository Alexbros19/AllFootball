package com.alexbros.opidlubnyi.allfootball;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.views.ChannelImageView;

public class EventContextFragment extends Fragment {
    private String firstTeamProfileId = "";
    private String secondTeamProfileId = "";
    private View rootView;
    private FragmentActivity activity;
    private boolean refreshData = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        Bundle arguments = getArguments();
        if (arguments != null) {
            firstTeamProfileId = arguments.getString(Constants.INTENT_EXTRA_FIRST_TEAM_ID);
            secondTeamProfileId = arguments.getString(Constants.INTENT_EXTRA_SECOND_TEAM_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.event_list_context, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.eventContentToolbar);

        DrawerArrowDrawable drawerArrowDrawable = new DrawerArrowDrawable(activity);
        drawerArrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_START);
        drawerArrowDrawable.setProgress(1.0f);
        drawerArrowDrawable.setColor(Color.WHITE);
        toolbar.setNavigationIcon(drawerArrowDrawable);
        toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());

        toolbar.setTitle(getString(R.string.string_teams_profile));
        toolbar.setSubtitle("");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (refreshData) {
            (new GetTeamProfileAsyncTask(getFirstTeamProfileOnCompleteListener, firstTeamProfileId)).execute();
            (new GetTeamProfileAsyncTask(getSecondTeamProfileOnCompleteListener, secondTeamProfileId)).execute();
            refreshData = false;
        }
    }

    private GetTeamProfileAsyncTask.OnCompleteListener getFirstTeamProfileOnCompleteListener = new GetTeamProfileAsyncTask.OnCompleteListener() {
        @Override
        public void onSuccess(TeamProfile teamProfile) {
            // Official channels
            if (!teamProfile.hasChannels()) {
                rootView.findViewById(R.id.firstTeamNoChannels).setVisibility(View.VISIBLE);
            } else {
                LinearLayout teamSocialChannels = rootView.findViewById(R.id.firstTeamChannelsHorizontalLayout);
                for (String key : teamProfile.channels.keySet()) {
                    if (!teamProfile.channels.get(key).equals("")) {
                        ChannelImageView channelImageView = new ChannelImageView(activity, key, teamProfile.channels.get(key));
                        teamSocialChannels.addView(channelImageView);
                    }
                }
            }

            TextView firstTeamProfileNameTextView = rootView.findViewById(R.id.firstTeamNameTextView);
            firstTeamProfileNameTextView.setText(teamProfile.name);
        }

        @Override
        public void onError() {

        }
    };

    private GetTeamProfileAsyncTask.OnCompleteListener getSecondTeamProfileOnCompleteListener = new GetTeamProfileAsyncTask.OnCompleteListener() {
        @Override
        public void onSuccess(TeamProfile teamProfile) {
            if (!teamProfile.hasChannels()) {
                rootView.findViewById(R.id.secondTeamNoChannels).setVisibility(View.VISIBLE);
            } else {
                LinearLayout teamSocialChannels = rootView.findViewById(R.id.secondTeamChannelsHorizontalLayout);
                for (String key : teamProfile.channels.keySet()) {
                    if (!teamProfile.channels.get(key).equals("")) {
                        ChannelImageView channelImageView = new ChannelImageView(activity, key, teamProfile.channels.get(key));
                        teamSocialChannels.addView(channelImageView);
                    }
                }
            }

            TextView secondTeamProfileNameTextView = rootView.findViewById(R.id.secondTeamNameTextView);
            secondTeamProfileNameTextView.setText(teamProfile.name);
        }

        @Override
        public void onError() {

        }
    };

    public static void showFragment(FragmentManager fragmentManager, @IdRes int containerViewId, String firstTeamId, String secondTeamId) {
        Bundle arguments = new Bundle();
        arguments.putString(Constants.INTENT_EXTRA_FIRST_TEAM_ID, firstTeamId);
        arguments.putString(Constants.INTENT_EXTRA_SECOND_TEAM_ID, secondTeamId);

        EventContextFragment fragment = new EventContextFragment();
        fragment.setArguments(arguments);
        fragmentManager.beginTransaction().replace(containerViewId, fragment).commit();
    }
}
