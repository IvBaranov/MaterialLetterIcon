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
  public final static int SHAPE_CIRCLE = 0;
  public final static int SHAPE_RECT = 1;
  private final static Rect textBounds = new Rect();

  private final static int DEFAULT_SHAPE_COLOR = Color.BLACK;
  private final static int DEFAULT_SHAPE = SHAPE_CIRCLE;
  private final static int DEFAULT_LETTER_COLOR = Color.WHITE;
  private final static int DEFAULT_LETTER_SIZE = 26;
  private final static String DEFAULT_FONT_PATH = "fonts/Roboto-Light.ttf";
  private final static int DEFAULT_LETTERS_NUMBER = 1;
  private final static boolean DEFAULT_INITIALS_STATE = false;
  private final static int DEFAULT_INITIALS_NUMBER = 2;

  private Context context;
  private Paint mShapePaint;
  private Paint mLetterPaint;
  private int mShapeColor;
  private int mShapeType;
  private String mLetter;
  private int mLetterColor;
  private int mLetterSize;
  private int mLettersNumber;
  private boolean mInitials;
  private int mInitialsNumber;
  private String mOriginalLetter;

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
   * <li>shape type = circle</li>
   * <li>letter color = white</li>
   * <li>letter size = 26 sp</li>
   * <li>number of letters = 1</li>
   * <li>typeface = Roboto Light</li>
   * <li>initials = false</li>
   * <li>initials number = 2</li>
   * </ul>
   */
  private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    this.context = context;

    mShapeColor = DEFAULT_SHAPE_COLOR;
    mShapeType = DEFAULT_SHAPE;
    mLetterColor = DEFAULT_LETTER_COLOR;
    mLetterSize = DEFAULT_LETTER_SIZE;
    mLettersNumber = DEFAULT_LETTERS_NUMBER;
    mInitials = DEFAULT_INITIALS_STATE;
    mInitialsNumber = DEFAULT_INITIALS_NUMBER;

    mShapePaint = new Paint();
    mShapePaint.setStyle(Paint.Style.FILL);
    mShapePaint.setAntiAlias(true);

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
        mInitials =
            attr.getBoolean(R.styleable.MaterialLetterIcon_mli_initials, DEFAULT_INITIALS_STATE);
        mInitialsNumber = attr.getInt(R.styleable.MaterialLetterIcon_mli_initials_number,
            DEFAULT_INITIALS_NUMBER);
        mOriginalLetter = attr.getString(R.styleable.MaterialLetterIcon_mli_letter);
        if (mOriginalLetter != null) {
          setLetter(mOriginalLetter);
        }
        mShapeType = attr.getInt(R.styleable.MaterialLetterIcon_mli_shape_type, DEFAULT_SHAPE);
        mLetterColor =
            attr.getColor(R.styleable.MaterialLetterIcon_mli_letter_color, DEFAULT_LETTER_COLOR);
        mLetterSize =
            attr.getInt(R.styleable.MaterialLetterIcon_mli_letter_size, DEFAULT_LETTER_SIZE);
        mLettersNumber =
            attr.getInt(R.styleable.MaterialLetterIcon_mli_letters_number, DEFAULT_LETTERS_NUMBER);
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
      case SHAPE_CIRCLE:
        drawCircle(canvas, radius, viewWidthHalf, viewHeightHalf);
        break;
      case SHAPE_RECT:
        drawRect(canvas, this.getMeasuredWidth(), this.getMeasuredWidth());
        break;
    }
    if (mLetter != null) {
      drawLetter(canvas, viewWidthHalf, viewHeightHalf);
    }
  }

  private void drawCircle(Canvas canvas, int radius, int width, int height) {
    mShapePaint.setColor(mShapeColor);
    canvas.drawCircle(width, height, radius, mShapePaint);
  }

  private void drawRect(Canvas canvas, int width, int height) {
    mShapePaint.setColor(mShapeColor);
    canvas.drawRect(0, 0, width, height, mShapePaint);
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
   * Sets shape type.
   *
   * @param type one of shapes to draw: {@code MaterialLetterIcon.SHAPE_CIRCLE} or {@code
   * MaterialLetterIcon.SHAPE_RECT}
   */
  public void setShapeType(int type) {
    this.mShapeType = type;
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

  public Paint getShapePaint() {
    return mShapePaint;
  }

  public Paint getLetterPaint() {
    return mLetterPaint;
  }

  public int getShapeColor() {
    return mShapeColor;
  }

  public int getShapeType() {
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

  /**
   * Convert sp to pixel.
   */
  private static int spToPx(float sp, Resources resources) {
    float px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    return (int) px;
  }

  /**
   * Builder.
   */
  public static final class Builder {
    private final Context context;

    private int mShapeColor = DEFAULT_SHAPE_COLOR;
    private int mShapeType = DEFAULT_SHAPE;
    private String mLetter;
    private int mLetterColor = DEFAULT_LETTER_COLOR;
    private int mLetterSize = DEFAULT_LETTER_SIZE;
    private int mLettersNumber = DEFAULT_LETTERS_NUMBER;
    private Typeface mLetterTypeface;
    private boolean mInitials = DEFAULT_INITIALS_STATE;
    private int mInitialsNumber = DEFAULT_INITIALS_NUMBER;

    public Builder(Context context) {
      this.context = context;
      this.mLetterTypeface = Typeface.createFromAsset(context.getAssets(), DEFAULT_FONT_PATH);
    }

    public Builder shapeColor(int color) {
      this.mShapeColor = color;
      return this;
    }

    public Builder shapeType(int type) {
      this.mShapeType = type;
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

    public MaterialLetterIcon create() {
      MaterialLetterIcon icon = new MaterialLetterIcon(context);
      icon.setShapeColor(mShapeColor);
      icon.setShapeType(mShapeType);
      icon.setLetter(mLetter);
      icon.setLetterColor(mLetterColor);
      icon.setLetterSize(mLetterSize);
      icon.setLettersNumber(mLettersNumber);
      icon.setLetterTypeface(mLetterTypeface);
      icon.setInitials(mInitials);
      icon.setInitialsNumber(mInitialsNumber);

      return icon;
    }
  }
}
