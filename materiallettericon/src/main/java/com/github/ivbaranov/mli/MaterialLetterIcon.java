package com.github.ivbaranov.mli;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.example.ivbaranov.ma.R;

public class MaterialLetterIcon extends View {
  @Deprecated public final static int SHAPE_CIRCLE = 0;
  @Deprecated public final static int SHAPE_RECT = 1;
  @Deprecated public final static int SHAPE_ROUND_RECT = 2;
  @Deprecated public final static int SHAPE_TRIANGLE = 3;

  public enum Shape {CIRCLE, RECT, ROUND_RECT, TRIANGLE}

  private final static Rect textBounds = new Rect();

  private final static int DEFAULT_SHAPE_COLOR = Color.BLACK;
  private final static boolean DEFAULT_BORDER = false;
  private final static int DEFAULT_BORDER_COLOR = Color.BLACK;
  private final static int DEFAULT_BORDER_SIZE = 2;
  private final static Shape DEFAULT_SHAPE = Shape.CIRCLE;
  private final static int DEFAULT_LETTER_COLOR = Color.WHITE;
  private final static int DEFAULT_LETTER_SIZE = 26;
  private final static String DEFAULT_FONT_PATH = "fonts/Roboto-Light.ttf";
  private final static int DEFAULT_LETTERS_NUMBER = 1;
  private final static boolean DEFAULT_INITIALS_STATE = false;
  private final static int DEFAULT_INITIALS_NUMBER = 2;
  private final static float DEFAULT_ROUND_RECT_RADIUS = 2;

  private Context context;
  private Paint mShapePaint;
  private Paint mBorderPaint;
  private Paint mLetterPaint;
  private int mShapeColor;
  private boolean mBorder;
  private int mBorderColor;
  private int mBorderSize;
  private Shape mShapeType;
  private String mLetter;
  private int mLetterColor;
  private int mLetterSize;
  private int mLettersNumber;
  private boolean mInitials;
  private int mInitialsNumber;
  private String mOriginalLetter;
  private float mRoundRectRx;
  private float mRoundRectRy;

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
   * <li>shape color = black</li>
   * <li>border = false</li>
   * <li>border color = black</li>
   * <li>border size = 2 dp/li>
   * <li>shape type = circle</li>
   * <li>letter color = white</li>
   * <li>letter size = 26 sp</li>
   * <li>number of letters = 1</li>
   * <li>typeface = Roboto Light</li>
   * <li>initials = false</li>
   * <li>initials number = 2</li>
   * <li>round-rect x radius = 2 dp</li>
   * <li>round-rect y radius = 2 dp</li>
   * </ul>
   */
  private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    this.context = context;

    mShapeColor = DEFAULT_SHAPE_COLOR;
    mBorder = DEFAULT_BORDER;
    mBorderColor = DEFAULT_BORDER_COLOR;
    mBorderSize = DEFAULT_BORDER_SIZE;
    mShapeType = DEFAULT_SHAPE;
    mLetterColor = DEFAULT_LETTER_COLOR;
    mLetterSize = DEFAULT_LETTER_SIZE;
    mLettersNumber = DEFAULT_LETTERS_NUMBER;
    mInitials = DEFAULT_INITIALS_STATE;
    mInitialsNumber = DEFAULT_INITIALS_NUMBER;
    mRoundRectRx = DEFAULT_ROUND_RECT_RADIUS;
    mRoundRectRy = DEFAULT_ROUND_RECT_RADIUS;

    mShapePaint = new Paint();
    mShapePaint.setStyle(Paint.Style.FILL);
    mShapePaint.setAntiAlias(true);

    mBorderPaint = new Paint();
    mBorderPaint.setStyle(Paint.Style.STROKE);
    mBorderPaint.setAntiAlias(true);

    mLetterPaint = new Paint();
    mLetterPaint.setAntiAlias(true);
    mLetterPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT_PATH));

    if (!isInEditMode() && attrs != null) {
      initAttributes(context, attrs);
    }
  }

  private void initAttributes(Context context, AttributeSet attributeSet) {
    TypedArray attr = getTypedArray(context, attributeSet, R.styleable.MaterialLetterIcon);
    if (attr != null) {
      try {
        mShapeColor =
            attr.getColor(R.styleable.MaterialLetterIcon_mli_shape_color, DEFAULT_SHAPE_COLOR);
        mBorder = attr.getBoolean(R.styleable.MaterialLetterIcon_mli_border, DEFAULT_BORDER);
        mBorderColor =
            attr.getColor(R.styleable.MaterialLetterIcon_mli_border_color, DEFAULT_BORDER_COLOR);
        mBorderSize =
            attr.getInt(R.styleable.MaterialLetterIcon_mli_border_size, DEFAULT_BORDER_SIZE);
        mInitials =
            attr.getBoolean(R.styleable.MaterialLetterIcon_mli_initials, DEFAULT_INITIALS_STATE);
        mInitialsNumber = attr.getInt(R.styleable.MaterialLetterIcon_mli_initials_number,
            DEFAULT_INITIALS_NUMBER);
        mOriginalLetter = attr.getString(R.styleable.MaterialLetterIcon_mli_letter);
        if (mOriginalLetter != null) {
          setLetter(mOriginalLetter);
        }
        mShapeType = Shape.values()[attr.getInt(R.styleable.MaterialLetterIcon_mli_shape_type,
            DEFAULT_SHAPE.ordinal())];
        mLetterColor =
            attr.getColor(R.styleable.MaterialLetterIcon_mli_letter_color, DEFAULT_LETTER_COLOR);
        mLetterSize =
            attr.getInt(R.styleable.MaterialLetterIcon_mli_letter_size, DEFAULT_LETTER_SIZE);
        mLettersNumber =
            attr.getInt(R.styleable.MaterialLetterIcon_mli_letters_number, DEFAULT_LETTERS_NUMBER);
        mRoundRectRx = attr.getFloat(R.styleable.MaterialLetterIcon_mli_round_rect_rx,
            DEFAULT_ROUND_RECT_RADIUS);
        mRoundRectRy = attr.getFloat(R.styleable.MaterialLetterIcon_mli_round_rect_ry,
            DEFAULT_ROUND_RECT_RADIUS);
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
    switch (mShapeType) {
      case CIRCLE:
        drawCircle(canvas, radius, viewWidthHalf, viewHeightHalf);
        break;
      case RECT:
        drawRect(canvas, this.getMeasuredWidth(), this.getMeasuredWidth());
        break;
      case ROUND_RECT:
        drawRoundRect(canvas, this.getMeasuredWidth(), this.getMeasuredWidth());
        break;
      case TRIANGLE:
        drawTriangle(canvas);
        break;
    }
    if (mLetter != null) {
      drawLetter(canvas, viewWidthHalf, viewHeightHalf);
    }
  }

  private void drawCircle(Canvas canvas, int radius, int width, int height) {
    mShapePaint.setColor(mShapeColor);
    canvas.drawCircle(width, height, radius, mShapePaint);
    if (mBorder) {
      final int borderPx = dpToPx(mBorderSize, context.getResources());
      mBorderPaint.setColor(mBorderColor);
      mBorderPaint.setStrokeWidth(dpToPx(mBorderSize, context.getResources()));
      canvas.drawCircle(width, height, radius - borderPx / 2, mBorderPaint);
    }
  }

  private void drawRect(Canvas canvas, int width, int height) {
    mShapePaint.setColor(mShapeColor);
    canvas.drawRect(0, 0, width, height, mShapePaint);
    if (mBorder) {
      final int borderPx = dpToPx(mBorderSize, context.getResources());
      mBorderPaint.setColor(mBorderColor);
      mBorderPaint.setStrokeWidth(dpToPx(mBorderSize, context.getResources()));
      canvas.drawRect(borderPx / 2, borderPx / 2, width - borderPx / 2, height - borderPx / 2,
          mBorderPaint);
    }
  }

  private void drawRoundRect(Canvas canvas, float width, float height) {
    mShapePaint.setColor(mShapeColor);
    int rectRxPx = dpToPx(mRoundRectRx, context.getResources());
    int rectRyPx = dpToPx(mRoundRectRy, context.getResources());
    if (mBorder) {
      final int borderPx = dpToPx(mBorderSize, context.getResources());
      canvas.drawRoundRect(
          new RectF(borderPx / 2, borderPx / 2, width - borderPx / 2, height - borderPx / 2),
          rectRxPx, rectRyPx, mShapePaint);

      mBorderPaint.setColor(mBorderColor);
      mBorderPaint.setStrokeWidth(dpToPx(mBorderSize, context.getResources()));
      canvas.drawRoundRect(
          new RectF(borderPx / 2, borderPx / 2, width - borderPx / 2, height - borderPx / 2),
          rectRxPx, rectRyPx, mBorderPaint);
    } else {
      canvas.drawRoundRect(new RectF(0, 0, width, height), rectRxPx, rectRyPx, mShapePaint);
    }
  }

  private void drawTriangle(Canvas canvas) {
    Rect bounds = canvas.getClipBounds();
    Path triangle = new Path();
    triangle.moveTo(bounds.left + bounds.right / 10, bounds.bottom - bounds.bottom / 5);
    triangle.lineTo(bounds.left + (bounds.right - bounds.left) / 2, bounds.top);
    triangle.lineTo(bounds.right - bounds.right / 10, bounds.bottom - bounds.bottom / 5);
    triangle.lineTo(bounds.left + bounds.right / 10, bounds.bottom - bounds.bottom / 5);

    mShapePaint.setColor(mShapeColor);
    mShapePaint.setStyle(Paint.Style.FILL);
    canvas.drawPath(triangle, mShapePaint);
  }

  private void drawLetter(Canvas canvas, float cx, float cy) {
    mLetterPaint.setColor(mLetterColor);
    mLetterPaint.setTextSize(spToPx(mLetterSize, context.getResources()));
    if (mInitials) {
      mLetterPaint.getTextBounds(mLetter, 0,
          mInitialsNumber > mLetter.length() ? mLetter.length() : mInitialsNumber, textBounds);
    } else {
      mLetterPaint.getTextBounds(mLetter, 0,
          mLettersNumber > mLetter.length() ? mLetter.length() : mLettersNumber, textBounds);
    }
    canvas.drawText(mLetter, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(),
        mLetterPaint);
  }

  /**
   * Sets color to shape.
   *
   * @param color a color integer associated with a particular resource id
   */
  public void setShapeColor(int color) {
    this.mShapeColor = color;
    invalidate();
  }

  /**
   * Sets border to shape.
   *
   * @param border if true, draws shape with border
   */
  public void setBorder(boolean border) {
    this.mBorder = border;
    invalidate();
  }

  /**
   * Sets border color.
   *
   * @param color a color integer associated with a particular resource id
   */
  public void setBorderColor(int color) {
    this.mBorderColor = color;
    invalidate();
  }

  /**
   * Set size of border.
   *
   * @param borderSize size of border in DP
   */
  public void setBorderSize(int borderSize) {
    this.mBorderSize = borderSize;
    invalidate();
  }

  /**
   * Sets shape type. Please use {@code setShapeType(Shape shapeType)} instead.
   *
   * @param type one of shapes to draw: {@code MaterialLetterIcon.SHAPE_CIRCLE}, {@code
   * MaterialLetterIcon.SHAPE_RECT}, {@code MaterialLetterIcon.SHAPE_ROUND_RECT}, {@code
   * MaterialLetterIcon.SHAPE_TRIANGLE}
   */
  @Deprecated public void setShapeType(int type) {
    this.mShapeType = Shape.values()[0];
    invalidate();
  }

  /**
   * Sets shape type.
   *
   * @param shapeType one of shapes to draw: {@code MaterialLetterIcon.Shape.CIRCLE}, {@code
   * MaterialLetterIcon.Shape.RECT}, {@code MaterialLetterIcon.Shape.ROUND_RECT}, {@code
   * MaterialLetterIcon.Shape.TRIANGLE}
   */
  public void setShapeType(Shape shapeType) {
    this.mShapeType = shapeType;
    invalidate();
  }

  /**
   * Set letters.
   *
   * @param string a string to take letters from
   */
  public void setLetter(String string) {
    if (string == null || string.isEmpty()) {
      return;
    }

    string = string.trim();
    this.mOriginalLetter = string;

    int desireLength;
    if (mInitials) {
      String initials[] = string.split("\\s+");
      StringBuilder initialsPlain = new StringBuilder(mLettersNumber);
      for (String initial : initials) {
        initialsPlain.append(initial.substring(0, 1));
      }
      this.mLetter = initialsPlain.toString();
      desireLength = mInitialsNumber;
    } else {
      this.mLetter = String.valueOf(string.replaceAll("\\s+", ""));
      desireLength = mLettersNumber;
    }

    this.mLetter =
        mLetter.substring(0, desireLength > mLetter.length() ? mLetter.length() : desireLength)
            .toUpperCase();
    invalidate();
  }

  /**
   * Set letters color.
   *
   * @param color a color integer associated with a particular resource id
   */
  public void setLetterColor(int color) {
    this.mLetterColor = color;
    invalidate();
  }

  /**
   * Set letters size.
   *
   * @param size size of letter in SP
   */
  public void setLetterSize(int size) {
    this.mLetterSize = size;
    invalidate();
  }

  /**
   * Set number of letters to be displayed.
   *
   * @param num number of letters
   */
  public void setLettersNumber(int num) {
    this.mLettersNumber = num;
    invalidate();
  }

  /**
   * Set letters typeface.
   *
   * @param typeface a typeface to apply to letter
   */
  public void setLetterTypeface(Typeface typeface) {
    this.mLetterPaint.setTypeface(typeface);
    invalidate();
  }

  /**
   * Set initials state.
   *
   * @param state if true, gets first letter of {@code initialsNumber} words in provided string
   */
  public void setInitials(boolean state) {
    this.mInitials = state;
    setLetter(mOriginalLetter);
  }

  /**
   * Set number of initials to show.
   *
   * @param initialsNumber number of initials to show
   */
  public void setInitialsNumber(int initialsNumber) {
    this.mInitialsNumber = initialsNumber;
    setLetter(mOriginalLetter);
  }

  /**
   * Set the x-radius of the oval used to round the corners.
   *
   * @param rx x-radius of the oval
   */
  public void setRoundRectRx(float rx) {
    this.mRoundRectRx = rx;
    invalidate();
  }

  /**
   * Set the y-radius of the oval used to round the corners.
   *
   * @param ry y-radius of the oval
   */
  public void setRoundRectRy(float ry) {
    this.mRoundRectRy = ry;
    invalidate();
  }

  public Paint getShapePaint() {
    return mShapePaint;
  }

  public Paint getBorderPaint() {
    return mBorderPaint;
  }

  public boolean hasBorder() {
    return mBorder;
  }

  public int getBorderColor() {
    return mBorderColor;
  }

  public int getBorderSize() {
    return mBorderSize;
  }

  public Paint getLetterPaint() {
    return mLetterPaint;
  }

  public int getShapeColor() {
    return mShapeColor;
  }

  public Shape getShapeType() {
    return mShapeType;
  }

  public String getLetter() {
    return mLetter;
  }

  public int getLetterColor() {
    return mLetterColor;
  }

  public int getLetterSize() {
    return mLetterSize;
  }

  public int getLettersNumber() {
    return mLettersNumber;
  }

  public boolean isInitials() {
    return mInitials;
  }

  public int getInitialsNumber() {
    return mInitialsNumber;
  }

  public float getRoundRectRx() {
    return mRoundRectRx;
  }

  public float getRoundRectRy() {
    return mRoundRectRy;
  }

  /**
   * Convert sp to pixel.
   */
  private static int spToPx(float sp, Resources resources) {
    float px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    return (int) px;
  }

  /**
   * Convert dp to pixel.
   */
  private static int dpToPx(float dp, Resources resources) {
    float px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    return (int) px;
  }

  /**
   * Builder.
   */
  public static final class Builder {
    private final Context context;

    private int mShapeColor = DEFAULT_SHAPE_COLOR;
    private Shape mShapeType = DEFAULT_SHAPE;
    private boolean mBorder = DEFAULT_BORDER;
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderSize = DEFAULT_BORDER_SIZE;
    private String mLetter;
    private int mLetterColor = DEFAULT_LETTER_COLOR;
    private int mLetterSize = DEFAULT_LETTER_SIZE;
    private int mLettersNumber = DEFAULT_LETTERS_NUMBER;
    private Typeface mLetterTypeface;
    private boolean mInitials = DEFAULT_INITIALS_STATE;
    private int mInitialsNumber = DEFAULT_INITIALS_NUMBER;
    private float mRoundRectRx = DEFAULT_ROUND_RECT_RADIUS;
    private float mRoundRectRy = DEFAULT_ROUND_RECT_RADIUS;

    public Builder(Context context) {
      this.context = context;
      this.mLetterTypeface = Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT_PATH);
    }

    public Builder shapeColor(int color) {
      this.mShapeColor = color;
      return this;
    }

    @Deprecated public Builder shapeType(int type) {
      this.mShapeType = Shape.values()[0];
      return this;
    }

    public Builder shapeType(Shape shapeType) {
      this.mShapeType = shapeType;
      return this;
    }

    public Builder border(boolean border) {
      this.mBorder = border;
      return this;
    }

    public Builder borderColor(int borderColor) {
      this.mBorderColor = borderColor;
      return this;
    }

    public Builder borderSize(int borderSize) {
      this.mBorderSize = borderSize;
      return this;
    }

    public Builder letter(String letter) {
      this.mLetter = letter;
      return this;
    }

    public Builder letterColor(int color) {
      this.mLetterColor = color;
      return this;
    }

    public Builder letterSize(int size) {
      this.mLetterSize = size;
      return this;
    }

    public Builder lettersNumber(int num) {
      this.mLettersNumber = num;
      return this;
    }

    public Builder letterTypeface(Typeface typeface) {
      this.mLetterTypeface = typeface;
      return this;
    }

    public Builder initials(boolean state) {
      this.mInitials = state;
      return this;
    }

    public Builder initialsNumber(int num) {
      this.mInitialsNumber = num;
      return this;
    }

    public Builder roundRectRx(float rx) {
      this.mRoundRectRx = rx;
      return this;
    }

    public Builder roundRectRy(float ry) {
      this.mRoundRectRy = ry;
      return this;
    }

    public MaterialLetterIcon create() {
      MaterialLetterIcon icon = new MaterialLetterIcon(context);
      icon.setShapeColor(mShapeColor);
      icon.setShapeType(mShapeType);
      icon.setBorder(mBorder);
      icon.setBorderColor(mBorderColor);
      icon.setBorderSize(mBorderSize);
      icon.setLetter(mLetter);
      icon.setLetterColor(mLetterColor);
      icon.setLetterSize(mLetterSize);
      icon.setLettersNumber(mLettersNumber);
      icon.setLetterTypeface(mLetterTypeface);
      icon.setInitials(mInitials);
      icon.setInitialsNumber(mInitialsNumber);
      icon.setRoundRectRx(mRoundRectRx);
      icon.setRoundRectRy(mRoundRectRy);

      return icon;
    }
  }
}
