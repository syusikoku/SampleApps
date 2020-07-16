package com.zy.douyin2.filter;

import android.content.Context;

import com.zy.douyin2.R;

/**
 * 新的屏幕过滤器:
 *    负责往屏幕上进行渲染
 */
public class NewScreenFilter extends AbsFilter {
    public NewScreenFilter(Context _context) {
        super(_context, R.raw.base_vertex, R.raw.base_frag);
    }
}
