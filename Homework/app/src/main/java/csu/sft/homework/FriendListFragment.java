package csu.sft.homework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FriendListFragment extends Fragment {

    public FriendListFragment() {
        // Required empty public constructor
    }

    public static FriendListFragment newInstance() {
        return new FriendListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }
}
