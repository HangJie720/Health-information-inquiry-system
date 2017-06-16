package com.ijustyce.fastandroiddev.irecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.baseLib.utils.ILog;

/**
 * Created by yangchun on 16/4/15.  封装的 RecyclerView
 */
public class IRecyclerView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private LinearLayout mHeader, mFooter, mFooterLoading;
    private ProgressBar processBar;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private IAdapter adapter;
    private boolean hasMore = true;
    private PullToRefreshListener mRefreshListener;

    private TextView footerLabel;

    private final static String TAG = "IRecyclerView";

    @Override
    public final void onRefresh() {

        if (mRefreshListener != null){
            mRefreshListener.onRefresh();
        }if (!mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    public final void setFooterLabel(String text){

        initFooter();
        if (footerLabel == null) return;
        footerLabel.setText(text);
    }

    private void createRecyclerView(Context context){

        View mView = LayoutInflater.from(context).inflate(R.layout.irecyclerview_view_recycler, this);
        if (mView == null){
            ILog.e(TAG, "mView is null, it is a fastandroiddev error, please contact developer... ");
            return;
        }

        View view = mView.findViewById(R.id.irecyclerview_list);
        if (view instanceof RecyclerView) mRecyclerView = (RecyclerView) view;
        else throw new RuntimeException("can not get RecyclerView, is your xml file " +
                "already have a view and it's id is R.id.irecyclerview_list ? ");

        View view2 = mView.findViewById(R.id.irecyclerview_swipe_refresh);
        if (view2 instanceof SwipeRefreshLayout) mSwipeRefreshLayout = (SwipeRefreshLayout) view2;
        else throw new RuntimeException("can not get SwipeRefreshLayout, is your xml file " +
                "already have a view and it's id is R.id.irecyclerview_swipe_refresh ? ");

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    public final void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        if (mRecyclerView == null) return;
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public final void setAdapter(IAdapter adapter){

        this.adapter = adapter;
        addListener();
    }

    public final void setHasMore(boolean hasMore){

        initFooter();
        footerLabel.setText(hasMore ? "正在加载..." : "没有更多数据了");
        processBar.setVisibility(hasMore ? VISIBLE : INVISIBLE);
        this.hasMore = hasMore;
    }

    public final void setPullToRefreshListener(PullToRefreshListener mRefreshListener){

        mSwipeRefreshLayout.setEnabled(mRefreshListener != null);
        this.mRefreshListener = mRefreshListener;
    }

    public final void onRefreshEnd(){

        mSwipeRefreshLayout.setRefreshing(false);
    }

    public final RecyclerView getRecyclerView(){

        return mRecyclerView;
    }

    /**
     * 瀑布流式 布局
     * @param num 列数或行数（竖直即true为列数，false为行数）
     * @param vertical  是否为竖直布局
     */
    public final void setStaggeredGridLayout(int num, boolean vertical){

        if (num <= 0){
            num = 1;
        }
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(num, vertical ?
                StaggeredGridLayoutManager.VERTICAL : StaggeredGridLayoutManager.HORIZONTAL));
    }

    /**
     * gird layout
     * @param num   列数
     */
    public final void setGirdLayout(int num){

        if (num <= 0){
            num = 1;
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, num));
    }

    public final void onLoadEnd(){

        initFooter();
        if (mFooterLoading != null){
            mFooterLoading.setVisibility(INVISIBLE);
        }
    }

    public final void notifyDataSetChanged(){

        if (adapter != null) {
            ILog.i("===notifyDataSetChanged===");
            adapter.reAddFooter();
            adapter.notifyDataSetChanged();
        }
    }

    public final void addHeaderView(View view){

        if (mHeader == null){
            mHeader = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.irecyclerview_view_header, null)
                    .findViewById(R.id.container);
        }
        if (mHeader != null && view != null){
            mHeader.addView(view);
        }if (adapter != null){
            adapter.setHeaderView(mHeader);
        }
    }

    private void initFooter(){

        if (mFooter == null){
            mFooter = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.irecyclerview_view_footer, null)
                    .findViewById(R.id.container);
            footerLabel = (TextView) mFooter.findViewById(R.id.footerLabel);
            mFooterLoading = (LinearLayout) mFooter.findViewById(R.id.footerLoading);
            processBar = (ProgressBar) mFooter.findViewById(R.id.processBar);
        }
    }

    public final void addFooterView(View view){

        initFooter();
        if (mFooter != null && view != null){
            mFooter.addView(view, mFooter.getChildCount() -1);
        }if (adapter != null){
            adapter.setFooterView(mFooter);
        }
    }

    private void addListener(){

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mRefreshListener == null || newState != RecyclerView.SCROLL_STATE_IDLE)return;
                if (adapter.isFooterVisible() && hasMore) {
                    mRefreshListener.onLoadMore();
                    initFooter();
                    if (mFooterLoading != null){
                        mFooterLoading.setVisibility(VISIBLE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });
        adapter.setFooterView(mFooter);
        adapter.setHeaderView(mHeader);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 初始化入口
     * @param context   context
     * @param attrs     参数
     */
    private void doInit(Context context, AttributeSet attrs){

        this.mContext = context;
        createRecyclerView(context);
    }

    public IRecyclerView(Context context) {
        super(context);

        doInit(context, null);
    }

    public IRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        doInit(context, attrs);
    }

    public IRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        doInit(context, attrs);
    }
}
