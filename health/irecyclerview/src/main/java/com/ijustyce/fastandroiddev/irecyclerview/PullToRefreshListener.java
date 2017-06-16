package com.ijustyce.fastandroiddev.irecyclerview;

/**
 * Created by yangchun on 16/4/15.  下拉与上拉的监听
 */
public interface PullToRefreshListener {

    public void onRefresh();
    public void onLoadMore();
}
