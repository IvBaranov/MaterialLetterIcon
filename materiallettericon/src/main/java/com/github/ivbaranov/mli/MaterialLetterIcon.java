package com.github.ivbaranov.mli;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.example.ivbaranov.ma.R;

public class MaterialLetterIcon extends View {
  private final static int DEFAULT_CIRCLE_COLOR = Color.BLACK;
  private final static int DEFAULT_LETTER_COLOR = Color.WHITE;
  private final static int DEFAULT_LETTER_SIZE = 26;
  private final static Rect textBounds = new Rect();

  private Context context;
  private Paint mCirclePaint;
  private Paint mLetterPaint;
  private int mCircleColor;
  private String mLetter;
  private int mLetterColor;
  private int mLetterSize;

  public MaterialLetterIcon(Context context) {
    super(context);

    init(context, null, 0, 0);
  }

  public MaterialLetterIcon(Context context, AttributeSet attrs) {
    super(context, attrs);

    init(context, attrs, 0, 0);
  }

  public MaterialLetterIcon(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context, attrs, defStyleAttr, 0);
  }

  public MaterialLetterIcon(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr);

    init(context, attrs, defStyleAttr, defStyleRes);
  }

  /**
   * Initialize the default values
   * <ul>
   * <li>circle color = black</li>
   * <li>letter color = white</li>
   * <li>letter size = 26 sp</li>
   * <li>typeface = Roboto Light</li>
   * </ul>
   */
  private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    this.context = context;

    mCircleColor = DEFAULT_CIRCLE_COLOR;
    mLetterColor = DEFAULT_LETTER_COLOR;
    mLetterSize = DEFAULT_LETTER_SIZE;

    mCirclePaint = new Paint();
    mCirclePaint.setStyle(Paint.Style.FILL);
    mCirclePaint.setAntiAlias(true);

    mLetterPaint = new Paint();
    mLetterPaint.setAntiAlias(true);
    mLetterPaint.setTypeface(
        Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));

    if (!isInEditMode() && attrs != null) {
      initAttributes(context, attrs);
    }
  }

  private void initAttributes(Context context, AttributeSet attributeSet) {
    TypedArray attr = getTypedArray(context, attributeSet, R.styleable.MaterialLetterIcon);
    if (attr != null) {
      try {
        mCircleColor = attr.getColor(R.styleable.MaterialLetterIcon_mli_circle_color,
            DEFAULT_CIRCLE_COLOR);
        String attrLetter = attr.getString(R.styleable.MaterialLetterIcon_mli_letter);
        if (attrLetter != null) {
          setLetter(attrLetter);
        }
        mLetterColor = attr.getColor(R.styleable.MaterialLetterIcon_mli_letter_color,
            DEFAULT_LETTER_COLOR);
        mLetterSize =
            attr.getInt(R.styleable.MaterialLetterIcon_mli_letter_size, DEFAULT_LETTER_SIZE);
      } finally {
        attr.recycle();
      }
    }
  }

  private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
    return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
  }

  @Override protected void onDraw(Canvas canvas) {
    int viewWidthHalf = this.getMeasuredWidth() / 2;
    int viewHeightHalf = this.getMeasuredHeight() / 2;

    int radius;
    if (viewWidthHalf > viewHeightHalf) {
      radius = viewHeightHalf;
    } else {
      radius = viewWidthHalf;
    }
    drawCircle(canvas, radius, viewWidthHalf, viewHeightHalf);

    if (mLetter != null) {
      drawLetter(canvas, viewWidthHalf, viewHeightHalf);
    }
  }

  private void drawCircle(Canvas canvas, int radius, int width, int height) {
    mCirclePaint.setColor(mCircleColor);
    canvas.drawCircle(width, height, radius, mCirclePaint);
  }

  private void drawLetter(Canvas canvas, float cx, float cy) {
    mLetterPaint.setColor(mLetterColor);
    mLetterPaint.setTextSize(spToPx(mLetterSize, context.getResources()));
    mLetterPaint.getTextBounds(mLetter, 0, 1, textBounds);
    canvas.drawText(mLetter, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(),
        mLetterPaint);
  }

  /**
   * Sets color to drawable.
   *
   * @param color a color integer associated with a particular resource id
   */
  public void setCircleColor(int color) {
    this.mCircleColor = color;
    invalidate();
  }

  /**
   * Set a letter.
   *
   * @param string a string to take first significant letter from
   */
  public void setLetter(String string) {
    this.mLetter = String.valueOf(string.replaceAll("\\s+", "").charAt(0)).toUpperCase();
    invalidate();
  }

  /**
   * Set a letter color.
   *
   * @param color a color integer associated with a particular resource id
   */
  public void setLetterColor(int color) {
    this.mLetterColor = color;
    invalidate();
  }

  /**
   * Set a letter size.
   *
   * @param size size of letter in SP
   */
  public void setLetterSize(int size) {
    this.mLetterSize = size;
    invalidate();
  }

  /**
   * Set a letter typeface.
   *
   * @param typeface a typeface to apply to letter
   */
  public void setLetterTypeface(Typeface typeface) {
    this.mLetterPaint.setTypeface(typeface);
    invalidate();
  }

  /**
   * Convert sp to pixel.
   */
  public static int spToPx(float sp, Resources resources) {
    float px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    return (int) px;
  }
}
