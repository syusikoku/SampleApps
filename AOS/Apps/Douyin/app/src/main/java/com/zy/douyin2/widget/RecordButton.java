package com.zy.douyin2.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 录制使用的button
 */
public class RecordButton extends AppCompatTextView {
    private OnRecordListener mRecordListener;

    public RecordButton(Context context) {
        this(context, null);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRecordListener == null) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 更新状态： 修改按钮的状态： 显示不同的效果
                setPressed(false);
                mRecordListener.stopRec();
                break;
            case MotionEvent.ACTION_DOWN:
                // 更新状态： 修改按钮的状态： 显示不同的效果
                setPressed(true);
                mRecordListener.startRec();
                break;
        }
        return true;
    }

    public void setOnRecordListener(OnRecordListener _recordListener) {
        this.mRecordListener = _recordListener;
    }

    public static interface OnRecordListener {
        /**
         * 开始录制
         */
        void startRec();

        /**
         * 停止录制
         */
        void stopRec();
    }
}
