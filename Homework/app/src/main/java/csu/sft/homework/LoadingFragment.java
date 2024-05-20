package csu.sft.homework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

public class LoadingFragment extends Fragment {

    private LottieAnimationView loadingAnimation;

    public LoadingFragment() {
        // Required empty public constructor
    }

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        loadingAnimation = view.findViewById(R.id.lottieAnimationView);
        // 启动加载动画
        startLoadingAnimation();
        // 模拟加载数据
        simulateDataLoading();
        return view;
    }
    private void startLoadingAnimation() {
        loadingAnimation.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();
    }

    private void stopLoadingAnimation() {
        loadingAnimation.cancelAnimation();
        loadingAnimation.setVisibility(View.GONE);
    }

    private void simulateDataLoading() {
        // 模拟5秒后停止加载并展示列表
        loadingAnimation.postDelayed(() -> {
            stopLoadingAnimation();
            // 在此处展示实际的好友列表
        }, 5000); // 5000 毫秒 = 5 秒
    }

}

