package com.example.clock

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.text.SimpleDateFormat
import java.util.*

class ClockView : View {
    var hPaint: Paint? = null
    var mPaint:Paint? = null
    var sPaint:Paint? = null
    var circlePaint:Paint? = null
    var cPointPaint:Paint? = null
    var paintDegree:Paint? = null
    var txtPaint:Paint? = null
    var titlePaint:Paint? = null
    val maxWidth = 500
    var c_width:Int = 0
    var c_height:Int = 0

    var hSound = 0
    var mSound = 0
    var sSound = 0

    private var hcount = 0 //当前小时数

    private var mcount = 0 //当前分钟数

    private var scount = 0 //当前秒钟数


    private val hScale = 30 //每小时之间30度

    private val mScale = 6 //每分钟之间是6度


    var hRect: RectF? = null
    var mRect:RectF? = null
    var sRect:RectF? = null
    var first = true //执行动画之后再转动指针


    constructor(context: Context?) : super(context){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context,attrs){
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,attrs,defStyleAttr){
        init()
    }


     fun init() {
        //外圆盘画笔
        circlePaint = Paint()
         circlePaint?.setAntiAlias(true)
         circlePaint?.setDither(true)
         circlePaint?.setStyle(Paint.Style.STROKE)
         circlePaint?.setColor(Color.BLACK)
         circlePaint?.setStrokeWidth(4f)

         //圆心画笔
         cPointPaint = Paint()
         cPointPaint?.setAntiAlias(true)
         cPointPaint?.setDither(true)
         cPointPaint?.setStyle(Paint.Style.FILL_AND_STROKE)
         cPointPaint?.setColor(-0xb350b0) //绿色
         cPointPaint?.setStrokeWidth(4f)



        //时针画笔
        hPaint = Paint()
        hPaint?.isAntiAlias = true
        hPaint?.isDither = true
        hPaint?.style = Paint.Style.FILL_AND_STROKE
        hPaint?.color = -0xcb631e //蓝色
        hPaint?.strokeWidth = 14f
        //时针矩形
        hRect = RectF(-20F, 0F, 150F, 0F)

        //分针画笔
        mPaint = Paint()
        mPaint?.setAntiAlias(true)
        mPaint?.setDither(true)
        mPaint?.setStyle(Paint.Style.FILL_AND_STROKE)
        mPaint?.setColor(-0x24dac0) //红色
        mPaint?.setStrokeWidth(12f)
        //分针矩形
        mRect = RectF(-30F, 0F, 200F, 0F)

        //秒针画笔
        sPaint = Paint()
        sPaint?.setAntiAlias(true)
        sPaint?.setDither(true)
        sPaint?.setStyle(Paint.Style.FILL_AND_STROKE)
        sPaint?.setColor(-0xb350b0) //绿色
        sPaint?.setStrokeWidth(10f)
        //秒钟矩形
        sRect = RectF(-40F, 0F, 250F, 0F)

        //刻度画笔
        paintDegree = Paint()
        paintDegree?.setColor(-0x1000000)
        paintDegree?.setStyle(Paint.Style.STROKE)
        paintDegree?.setAntiAlias(true)

        //文字画笔
        txtPaint = Paint()
        txtPaint?.setColor(-0x1000000)
        txtPaint?.setTextSize(50f)
        txtPaint?.setAntiAlias(true)

        //标题画笔
        titlePaint = Paint()
        titlePaint?.setColor(-0x1000000)
        titlePaint?.setTextSize(100f)
        titlePaint?.setAntiAlias(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(maxWidth, maxWidth)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(maxWidth, width)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(height, maxWidth)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        c_width = measuredWidth - paddingStart - paddingEnd
        c_height = measuredHeight - paddingBottom - paddingTop
        c_height = Math.min(width, height)
        c_width = height
        //圆心点
        val cpoints = width / 2
        hSound = width / 5 //时针长度
        mSound = width / 4 //分针长度
        sSound = width / 3 //秒针长度
        val radius = cpoints * 3 / 4 //外圆半径
        if (!first) {
            getDatas()
        }
        val titleText = "谢鹏的时钟"
        //X坐标等于圆心的X坐标减去文字的一半的长度,Y坐标等于圆心的Y坐标减圆的半径再减20
        //X坐标等于圆心的X坐标减去文字的一半的长度,Y坐标等于圆心的Y坐标减圆的半径再减20
        //X坐标等于圆心的X坐标减去文字的一半的长度,Y坐标等于圆心的Y坐标减圆的半径再减20
        canvas.drawText(
            titleText,
            cpoints - titlePaint!!.measureText(titleText) / 2,
            (cpoints - radius - 40).toFloat(),
            titlePaint!!
        )
        //画出12个小时的刻度线及文字
        for (i in 0..11) {
            var txtTime = Integer.toString(i)
            //3，6，9，12比其他的略粗略长
            if (i % 3 == 0) {
                if (i == 0) {
                    txtTime = "12"
                }
                paintDegree?.setStrokeWidth(10f)
                canvas.drawLine(
                    (width / 2).toFloat(),
                    (height / 2 - radius).toFloat(),
                    (width / 2).toFloat(),
                    (height / 2 - radius + 40).toFloat(),
                    paintDegree!!
                )
            } else {
                paintDegree?.setStrokeWidth(8f)
                canvas.drawLine(
                    (width / 2).toFloat(),
                    (height / 2 - radius).toFloat(),
                    (width / 2).toFloat(),
                    (height / 2 - radius + 30).toFloat(),
                    paintDegree!!
                )
            }

            canvas.drawText(
                txtTime,
                cpoints - txtPaint!!.measureText(txtTime) / 2,
                (height / 2 - radius + 90).toFloat(),
                txtPaint!!
            )

            canvas.rotate(hScale.toFloat(), (width / 2).toFloat(), (height / 2).toFloat())
        }

        //画出60个分钟的刻度线
        for (x in 0..59) {
            paintDegree?.setStrokeWidth(6f)
            if (x % 5 != 0) { //当x % 5 == 0时即是时钟刻度，因此不需要绘制，避免重复绘制
                canvas.drawLine(
                    (width / 2).toFloat(),
                    (height / 2 - radius).toFloat(),
                    (width / 2).toFloat(),
                    (height / 2 - radius + 16).toFloat(),
                    paintDegree!!
                )
            }
            canvas.rotate(mScale.toFloat(), (width / 2).toFloat(), (height / 2).toFloat())
        }

        //画外层圆
        circlePaint?.let {
            canvas.drawCircle(cpoints.toFloat(), cpoints.toFloat(), radius.toFloat(),
                it
            )
        }
        //画内层圆
        circlePaint?.let {
            canvas.drawCircle(cpoints.toFloat(), cpoints.toFloat(), (radius / 4).toFloat(),
                it
            )
        }
        //平移至中心点
        canvas.translate(cpoints.toFloat(), cpoints.toFloat())
        //保存画布
        canvas.save()

        //int hRotate = 270 + hScale * hcount;
        var offset = 30 * mcount / 60
        offset -= offset % mScale //时针相对分针数，有一个偏移量
        val hRotate = 270 + hScale * hcount + offset
        canvas.rotate(hRotate.toFloat())
        //        canvas.drawLine(0, -10, 0, hSound, hPaint);//画时针
        canvas.drawRoundRect(hRect!!, 15f, 15f, hPaint!!) //画时针
        canvas.restore()
        canvas.save()
        val mRotate = 270 + mScale * mcount
        canvas.rotate(mRotate.toFloat())
        mRect?.let { mPaint?.let { it1 -> canvas.drawRoundRect(it, 25f, 25f, it1) } } //画分针
        //canvas.drawLine(0, -15, 0, mSound, mPaint);//画分针
        canvas.restore()
        canvas.save()
        //一圈360度,总共60秒，因此时间每多一秒，度数加6
        val sRotate = 270 + mScale * scount
        canvas.rotate(sRotate.toFloat())
        //canvas.drawLine(0, -25, 0, sSound, sPaint);//画秒针
        sRect?.let { sPaint?.let { it1 -> canvas.drawRoundRect(it, 15f, 15f, it1) } } //画秒针
        cPointPaint?.let { canvas.drawCircle(0f, 0f, 6f, it) } //画圆心
        if (!first) {
            postInvalidateDelayed(1000)
        }
    }

    private fun getDatas() {
        val format = SimpleDateFormat("HH,mm,ss")
        val time = format.format(Date())
        try {
            val s = time.split(",").toTypedArray()
            hcount = s[0].toInt()
            mcount = s[1].toInt()
            scount = s[2].toInt()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun startAnim() {
        getDatas()
        val animatorh =
            ValueAnimator.ofInt(0, if (hcount > 12) hcount - 12 else hcount) //大于十二点时减去12 避免转两圈
        val animatorm = ValueAnimator.ofInt(0, mcount)
        val animators = ValueAnimator.ofInt(0, scount)

        //设置动画时长
        animatorh.duration = 1500
        animatorm.duration = 1500
        animators.duration = 1500
        animatorh.interpolator = LinearInterpolator()
        animatorh.addUpdateListener { animation -> hcount = animation.animatedValue as Int }
        animatorh.start()
        animatorm.interpolator = LinearInterpolator()
        animatorm.addUpdateListener { animation -> mcount = animation.animatedValue as Int }
        animatorm.start()
        animators.interpolator = LinearInterpolator()
        animators.addUpdateListener { animation ->
            scount = animation.animatedValue as Int
            postInvalidate() //添加之后动画才会执行，不然看不到效果
        }
        animators.start()
        //添加动画完成时的监听,在动画完成之后开始指针的转动
        animators.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                first = false
                postInvalidate()
            }
        })
    }

    fun SetFirstInit(ttt: Boolean) {
        first = ttt
    }
}