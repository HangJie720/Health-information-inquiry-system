package com.ijustyce.fastandroiddev.irecyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.fastandroiddev.baseLib.utils.ILog;

/**
 * Created by yangchun on 16/4/15.  通用的CommonHolder
 */
public class CommonHolder extends RecyclerView.ViewHolder{

    private View itemView;
    private Context mContext;
    private SparseArray<View> mView;
    
    public CommonHolder(View itemView, Context mContext) {
        super(itemView);
        this.itemView = itemView;
        this.mContext = mContext;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.itemView.setLayoutParams(lp);
        mView = new SparseArray<>();
    }

    public static CommonHolder getInstance(@LayoutRes int id, Context mContext, ViewGroup parent){
        
        if (mContext == null){
            return null;
        }
        return new CommonHolder(LayoutInflater.from(mContext).inflate(id, parent, false), mContext);
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mView.put(viewId, view);
        }

        if (view == null){
            ILog.e("===view is null, id is " + viewId);
        }
        return (T) view;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param resId
     * @return
     */
    public CommonHolder setText(int viewId, int resId) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(mContext.getResources().getString(resId));
        }
        return this;
    }
    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param resId
     * @return
     */
    public CommonHolder setText(int viewId, int resId, String oldChar, Object newChar) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(mContext.getResources().getString(resId).replace(oldChar, "" + newChar));
        }
        return this;
    }
    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text == null ? "" : text);
        }
        return this;
    }

    /**
     * 设置 View的 tag 值
     *
     * @param viewId
     * @param tag
     * @return
     */
    public CommonHolder setTag(int viewId, String tag) {
        View tv = getView(viewId);
        if (tv != null) {
            tv.setTag(tag);
        }
        return this;
    }

    public CommonHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageResource(resId);
        }
        return this;
    }

    public CommonHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageBitmap(bitmap);
        }
        return this;
    }

    public CommonHolder setImageBitmap(int viewId, int resId) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageBitmap(CommonTool.drawableToBitmap
                    (mContext.getResources().getDrawable(resId)));
        }
        return this;
    }

    public CommonHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        if (view != null) {
            view.setImageDrawable(drawable);
        }
        return this;
    }

    public CommonHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        if (view != null) {
            view.setBackgroundResource(backgroundRes);
        }
        return this;
    }

    public CommonHolder setTextColor(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setTextColor(mContext.getResources().getColor(textColorRes));
        }
        return this;
    }

    public CommonHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public CommonHolder setInvisible(int viewId, boolean invisible) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
        }
        return this;
    }

    public CommonHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        if (view != null) {
            view.setChecked(checked);
        }
        return this;
    }

    public Context getContext(){

        return mContext;
    }

    public CommonHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    public CommonHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnLongClickListener(listener);
        }
        return this;
    }
}