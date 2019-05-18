package android.support.v4.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class OpenedNestedScrollView extends NestedScrollView {

    public OpenedNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public OpenedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public OpenedNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    boolean overScrollByCompat(int deltaX, int deltaY,
                               int scrollX, int scrollY,
                               int scrollRangeX, int scrollRangeY,
                               int maxOverScrollX, int maxOverScrollY,
                               boolean isTouchEvent) {
        return openedOverScrollByCompat(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    protected boolean openedOverScrollByCompat(int deltaX, int deltaY,
                                               int scrollX, int scrollY,
                                               int scrollRangeX, int scrollRangeY,
                                               int maxOverScrollX, int maxOverScrollY,
                                               boolean isTouchEvent) {
        return super.overScrollByCompat(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
