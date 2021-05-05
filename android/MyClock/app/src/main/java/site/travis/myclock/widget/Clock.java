package site.travis.myclock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class Clock extends View {

	private final static String TAG = Clock.class.getSimpleName();

	private static final int FULL_ANGLE = 360;

	private static final int CUSTOM_ALPHA = 140;
	private static final int FULL_ALPHA = 255;

	private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
	private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

	private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

	public final static int AM = 0;

	private static final int RIGHT_ANGLE = 90;

	private float PANEL_RADIUS = 200.0f;// 表盘半径

	private float HOUR_POINTER_LENGTH;// 指针长度
	private float MINUTE_POINTER_LENGTH;
	private float SECOND_POINTER_LENGTH;
	private float UNIT_DEGREE = (float) (6 * Math.PI / 180);// 一个小格的度数

	private int mWidth, mCenterX, mCenterY, mRadius;

	private int degreesColor;

	private Context mContext;

	private Paint mNeedlePaint;
	private Paint mNumberPaint;
	private Paint mDegreePaint;

	public Clock(Context context) {
		super(context);
		init(context, null);
	}

	public Clock(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int size;
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
		int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

		if (widthWithoutPadding > heightWithoutPadding) {
			size = heightWithoutPadding;
		} else {
			size = widthWithoutPadding;
		}

		setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
	}

	private void init(Context context, AttributeSet attrs) {

		mContext = context;
		this.degreesColor = DEFAULT_PRIMARY_COLOR;

		mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mNeedlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mNeedlePaint.setStrokeCap(Paint.Cap.ROUND);

		mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mNumberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mNumberPaint.setStrokeCap(Paint.Cap.ROUND);

		mDegreePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mDegreePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mDegreePaint.setStrokeCap(Paint.Cap.ROUND);
		mDegreePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
		mDegreePaint.setColor(degreesColor);

		update();
	}

	private void update() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				postInvalidate(); // 重新绘制
			}
		}, 500, 50);
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);

		mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

		int halfWidth = mWidth / 2;
		mCenterX = halfWidth;
		mCenterY = halfWidth;
		mRadius = halfWidth;
		PANEL_RADIUS = mRadius;
		HOUR_POINTER_LENGTH = PANEL_RADIUS - 400;
		MINUTE_POINTER_LENGTH = PANEL_RADIUS - 250;
		SECOND_POINTER_LENGTH = PANEL_RADIUS - 150;

		drawDegrees(canvas);
		drawHoursValues(canvas);
		drawNeedles(canvas);
	}

	private void drawDegrees(Canvas canvas) {

		int rPadded = mCenterX - (int) (mWidth * 0.01f);
		int rEnd = mCenterX - (int) (mWidth * 0.05f);

		for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

			if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
				mDegreePaint.setAlpha(CUSTOM_ALPHA);
			else {
				mDegreePaint.setAlpha(FULL_ALPHA);
			}

			int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
			int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

			int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
			int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

			canvas.drawLine(startX, startY, stopX, stopY, mDegreePaint);

		}
	}

	/**
	 * Draw Hour Text Values, such as 1 2 3 ...
	 *
	 * @param canvas
	 */
	private void drawHoursValues(Canvas canvas) {
		// Default Color:
		// - hoursValuesColor
		mNumberPaint.setColor(Color.WHITE);
		mNumberPaint.setTextSize(40);
		int rPadded = mCenterX - (int) (mWidth * 0.10f);
		for (int i = 0; i < 12; ++i) {
			int degree = 30 * -i + 60; //计算每个数字所在位置的角度
			double radians = Math.toRadians(degree); //将角度转换为弧度，以便计算正弦值和余弦值
			mNumberPaint.setStrokeWidth(4);
			String hourText;
			hourText = String.valueOf(i + 1);
			Rect rect = new Rect(); //获取数字的宽度和高度
			mNumberPaint.getTextBounds(hourText, 0, hourText.length(), rect);
			int textWidth = rect.width();
			int textHeight = rect.height();
			canvas.drawText(hourText,
							(float) (mCenterX + rPadded * Math.cos(radians) - textWidth / 2),
							(float) (mCenterX - rPadded * Math.sin(radians) + textHeight / 2),
							mNumberPaint); //通过计算出来的坐标进行数字的绘制
		}
	}

	/**
	 * Draw hours, minutes needles
	 * Draw progress that indicates hours needle disposition.
	 *
	 * @param canvas
	 */
	private void drawNeedles(final Canvas canvas) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.CHINESE);
		//  Log.d("date", now.toString());
		/*Date now = calendar.getTime();
		int nowHours = now.getHours();
		int nowMinutes = now.getMinutes();
		int nowSeconds = now.getSeconds();*/

		int nowHours = calendar.get(Calendar.HOUR);
		int nowMinutes = calendar.get(Calendar.MINUTE);
		int nowSeconds = calendar.get(Calendar.SECOND);

		// 画秒针
		drawPointer(canvas, 2, nowSeconds);
		// 画分针
		// todo 画分针
		drawPointer(canvas, 1, nowMinutes);
		// 画时针
		int part = nowMinutes / 12;
		drawPointer(canvas, 0, 5 * nowHours + part);

	}


	private void drawPointer(Canvas canvas, int pointerType, int value) {

		float degree;
		float[] pointerHeadXY = new float[2];

		mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
		switch (pointerType) {
			case 0:
				degree = value * UNIT_DEGREE;
				mNeedlePaint.setColor(Color.WHITE);
				pointerHeadXY = getPointerHeadXY(HOUR_POINTER_LENGTH, degree);
				break;
			case 1:
				degree = value * UNIT_DEGREE;
				mNeedlePaint.setColor(Color.YELLOW);
				pointerHeadXY = getPointerHeadXY(MINUTE_POINTER_LENGTH, degree);
				break;
			case 2:
				degree = value * UNIT_DEGREE;
				mNeedlePaint.setColor(Color.GREEN);
				pointerHeadXY = getPointerHeadXY(SECOND_POINTER_LENGTH, degree);
				break;
		}


		canvas.drawLine(mCenterX, mCenterY, pointerHeadXY[0], pointerHeadXY[1], mNeedlePaint);
	}

	private float[] getPointerHeadXY(float pointerLength, float degree) {
		float[] xy = new float[2];
		xy[0] = (float) (mCenterX + pointerLength * Math.sin(degree));
		xy[1] = (float) (mCenterY - pointerLength * Math.cos(degree));
		return xy;
	}

/*	public enum Pointer {
		SECOND_PONINTER(2),
		MINUTE_PONINTER(1),
		HOUR_PONINTER(0);

		private Pointer(int val) {
			this.val = val;
		}

		final int val;
	}*/


}
