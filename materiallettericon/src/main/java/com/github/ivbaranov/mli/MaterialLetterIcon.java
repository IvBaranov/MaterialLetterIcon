package com.github.ivbaranov.mli;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class MaterialLetterIcon extends View {
  private final static int DEFAULT_CIRCLE_COLOR = Color.BLACK;
  private final static int DEFAULT_LETTER_COLOR = Color.WHITE;
  private final static int DEFAULT_LETTER_SIZE = 26;
  private final static Rect textBounds = new Rect();

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
   * <li>letter size = 28 sp</li>
   * </ul>
   */
  private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    mCircleColor = DEFAULT_CIRCLE_COLOR;
    mLetterColor = DEFAULT_LETTER_COLOR;
    mLetterSize = spToPx(DEFAULT_LETTER_SIZE, context.getResources());

    mCirclePaint = new Paint();
    mCirclePaint.setStyle(Paint.Style.FILL);
    mCirclePaint.setAntiAlias(true);

    mLetterPaint = new Paint();
    mLetterPaint.setAntiAlias(true);
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
    mLetterPaint.setTextSize(mLetterSize);
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
    this.mLetter = String.valueOf(string.replaceAll("\\s+", "").charAt(0));
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
   * Convert sp to pixel.
   */
  public static int spToPx(float sp, Resources resources) {
    float px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    return (int) px;
  }
}
