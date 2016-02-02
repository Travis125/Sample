package com.sunnybear.library.view.square;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sunnybear.library.R;
import com.sunnybear.library.view.image.ImageLoaderView;

/**
 * 九宫格控件
 * Created by guchenkai on 2016/2/2.
 */
public class SquareGridView extends ViewGroup {
    public static final int DEFAULT_MAX_SIZE = 9;
    public static final int DEFAULT_RATIO = 1;
    public static final int DEFAULT_HORIZONTAL_SPACE = 10;
    public static final int DEFAULT_VERTICAL_SPACE = 10;

    private int numColumns;
    private int maxSize = DEFAULT_MAX_SIZE;
    private int horizontalSpacing;
    private int verticalSpacing;
    private float ratio;
    private int childrenWidth;
    private int childrenHeight;

    private SquareViewAdapter squareViewAdapter;

    public SquareGridView(Context context) {
        this(context, null, 0);
    }

    public SquareGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyleable(context, attrs);
    }

    private void initStyleable(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes
                    (attrs, R.styleable.SquareGridView);
            maxSize = array.getInteger(R.styleable.SquareGridView_maxSize, DEFAULT_MAX_SIZE);
            horizontalSpacing = array.
                    getDimensionPixelSize(R.styleable.SquareGridView_horizontal_spacing, DEFAULT_HORIZONTAL_SPACE);
            verticalSpacing = array.
                    getDimensionPixelSize(R.styleable.SquareGridView_vertical_spacing, DEFAULT_VERTICAL_SPACE);
            ratio = array.getFloat(R.styleable.SquareGridView_ratio, DEFAULT_RATIO);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        width = widthSpecSize;
        height = heightSpecSize;

        int count = getRealCount();
        float rowCount = (count + 0f) / numColumns;
        int realRow = (int) Math.ceil(rowCount);

        childrenWidth = (width - getPaddingLeft() - getPaddingRight() - (numColumns - 1) * horizontalSpacing) / numColumns;
        childrenHeight = (int) (childrenWidth * ratio);

        height = getPaddingTop() + getPaddingBottom() + realRow * childrenHeight + (realRow - 1) * verticalSpacing;
        setMeasuredDimension(width, height);
    }

    public int getRealCount() {
        int count = getItemCount();
        return Math.min(count, maxSize);
    }

    public int getItemCount() {
        if (squareViewAdapter != null)
            return squareViewAdapter.getCount();
        return 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getRealCount();
        for (int i = 0; i < count; i++) {
            int row = i / numColumns;
            int column = i % numColumns;
            int left = getPaddingLeft() + column * horizontalSpacing + column * childrenWidth;
            int top = getPaddingTop() + row * verticalSpacing + row * childrenHeight;
            View childView = getChildAt(i);
            childView.layout(left, top, left + childrenWidth, top + childrenHeight);
        }
    }

    /**
     * 设置Adapter
     *
     * @param adapter 适配器
     */
    public void setAdapter(final SquareViewAdapter adapter) {
        squareViewAdapter = adapter;
        int rowColumn = adapter.getCount();
        switch (rowColumn) {
            case 1:
                numColumns = 1;
                break;
            case 2:
                numColumns = 2;
                break;
            case 4:
                numColumns = 2;
                break;
            default:
                numColumns = 3;
                break;
        }
        int count = getRealCount();
        int childCount = getChildCount();
        int shortCount = count - childCount;
        if (shortCount > 0) {
            for (int i = 0; i < shortCount; i++) {
                ImageLoaderView view = new ImageLoaderView(getContext());
                view.setTag(i + childCount);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(childrenWidth, childrenHeight);
                addView(view, params);
            }
        } else if (shortCount < 0) {
            for (int i = 0; i < Math.abs(shortCount); i++) {
                ImageLoaderView view = (ImageLoaderView) getChildAt(i + count);
                view.setVisibility(View.GONE);
            }
        }
        for (int i = 0; i < count; i++) {
            final int index = i;
            final ImageLoaderView view = (ImageLoaderView) getChildAt(i);
            view.setVisibility(View.VISIBLE);
            view.setImageURL(adapter.getImageUrl(i));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter != null) {
                        adapter.onItemClick(view, index, adapter.getItem(index));
                    }
                }
            });
        }
    }
}
