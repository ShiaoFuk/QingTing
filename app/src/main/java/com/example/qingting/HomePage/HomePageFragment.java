package com.example.qingting.HomePage;

import static androidx.core.content.ContextCompat.getSystemService;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.qingting.MainActivity;
import com.example.qingting.R;
import com.example.qingting.Utils.FragmentUtils;

public class HomePageFragment extends Fragment {
    private static HomePageFragment fragment;
    View rootView;
    FrameLayout frameLayout;
    SearchHistoryFragment searchHistoryFragment;
    SearchResultFragment searchResultFragment;
    EditText search;
    View topBar;
    private HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment getInstance() {
        if (fragment == null)
            fragment = new HomePageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        frameLayout = rootView.findViewById(R.id.page_frame);
        topBar = rootView.findViewById(R.id.top_bar);
        search = rootView.findViewById(R.id.search);
        searchHistoryFragment = SearchHistoryFragment.getInstance(search);
        searchResultFragment = SearchResultFragment.getInstance();
        init();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void init() {
        initSearch();  // 初始化搜索框
        initContent();  // 初始化推荐界面
    }

    private void initSearch() {
        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 如果当前在搜索历史页面才这样
                    if (searchHistoryFragment.isAdded()) {
                        topBar.setVisibility(View.INVISIBLE);
                        FragmentUtils.removeFragmentFromFragment(fragment, frameLayout, searchHistoryFragment);
                    }
                    return;
                }
                // focus在搜索框的时候要替换另一个fragment
//                topBar.setVisibility(View.GONE);
                if (!searchHistoryFragment.isAdded()) {
                    FragmentUtils.replaceFragmentToFragment(fragment, frameLayout, searchHistoryFragment);
                }
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = search.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString(SearchResultFragment.CONTENT_KEY, content);
                    searchResultFragment.setArguments(bundle);
                    FragmentUtils.replaceFragmentToFragment(fragment, frameLayout, searchResultFragment);
                    MainActivity.setNavigationBarVisibility(View.GONE);  // 避免通过导航栏跳转到其他地方之后被杀掉，只能返回
                    clearSearchFocus();
                }
                return false;
            }
        });
    }

    private void initContent() {
        FragmentUtils.addFragmentToFragment(this, frameLayout, HomePageContentFragment.getInstance());
    }



    public void clearSearchFocus() {
        // 获取 InputMethodManager 实例
        InputMethodManager imm = getSystemService(rootView.getContext(), InputMethodManager.class);
        if (imm != null && search.isFocused()) {
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
            search.clearFocus();
        }
    }


    /**
     * remove all childfragment
     * @return if remove at least one child fragment, return true, else false
     */
    public boolean removeChildFragments() {
        boolean flag = false;
        if (SearchHistoryFragment.getInstance().isAdded()) {
            FragmentUtils.removeFragmentFromFragment(this, (FrameLayout) this.getView().findViewById(R.id.page_frame), SearchHistoryFragment.getInstance());
            this.clearSearchFocus();
            flag = true;
        }
        if (SearchResultFragment.getInstance().isAdded()) {
            FragmentUtils.removeFragmentFromFragment(this, (FrameLayout) this.getView().findViewById(R.id.page_frame), SearchResultFragment.getInstance());
            this.clearSearchFocus();
            flag = true;
        }
        if (flag) {
            FragmentUtils.addFragmentToFragment(this, (FrameLayout) this.getView().findViewById(R.id.page_frame), HomePageContentFragment.getInstance());
        }
        return flag;
    }
}