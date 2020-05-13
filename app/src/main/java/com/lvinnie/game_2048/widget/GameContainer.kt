package com.lvinnie.game_2048.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.lvinnie.game_2048.R
import me.jessyan.autosize.utils.AutoSizeUtils
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.abs

/**
 * @author : Vinny
 * date : 2020/5/11
 * desc :
 */
class GameContainer : FrameLayout {

    val ANIMATION_DURATION = 100L

    val COUNT = 4
    val DIRECTION_NONE = 0
    val DIRECTION_TOP = 1
    val DIRECTION_RIGHT = 2
    val DIRECTION_BOTTOM = 4
    val DIRECTION_LEFT = 8

    var space: Int
    var moveSpaceMax = 0
    var moveSpaceMin = 0
    var direction = DIRECTION_NONE

    var score = 0
    var size = 0
    var cornerSize: Float = 0f

    var blockSize: Int = 0
    var blockCornerSize: Float = 0f

    var startX: Float = 0f
    var startY: Float = 0f
    var started = false
    var isMoving = false
    var rectF: RectF? = null

    var scoreView: AppCompatTextView? = null
        set(value) {
            field = value
            scoreView?.text = score.toString()
        }
    var bestView: AppCompatTextView? = null
        set(value) {
            field = value
            bestView?.text = GameHelper.getBestScore().toString()
        }
    var scaleInAnimation: ScaleAnimation

    var paintBg: Paint
    var paintBlockBg: Paint
    var blocks: ArrayList<ArrayList<GameBlock?>>
    var locations: ArrayList<ArrayList<Location>>
    var randomTest: ArrayList<Location>
    var moveBlocks: HashSet<GameBlock>
    var goneBlocks: HashSet<GameBlock>

    var dialogRestart: GameDialog
    var dialogGameOver: GameDialog
    var dialogAbout: GameDialog

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        var ta = context.obtainStyledAttributes(attrs, R.styleable.GameContainer)
        space = ta.getDimensionPixelOffset(R.styleable.GameContainer_GC_Space, AutoSizeUtils.dp2px(context, 10f))
        size = ta.getDimensionPixelOffset(R.styleable.GameContainer_GC_Size, AutoSizeUtils.dp2px(context, 300f))
        cornerSize = ta.getDimension(R.styleable.GameContainer_GC_CornerSize, AutoSizeUtils.dp2px(context, 6f).toFloat())

        moveSpaceMin = AutoSizeUtils.dp2px(context, 5f)
        moveSpaceMax = AutoSizeUtils.dp2px(context, 20f)

        scaleInAnimation = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleInAnimation.duration = 150

        isFocusable = true
        isClickable = true

        paintBg = Paint()
        paintBg.isAntiAlias = true
        paintBg.color = resources.getColor(R.color.bg_game_container)

        paintBlockBg = Paint()
        paintBlockBg.isAntiAlias = true
        paintBlockBg.color = resources.getColor(R.color.bg_number_empty)

        blockSize = (size - space * (COUNT + 1)) / COUNT
        blockCornerSize = cornerSize / 2

        randomTest = arrayListOf()
        moveBlocks = HashSet<GameBlock>()
        goneBlocks = HashSet<GameBlock>()
        blocks = arrayListOf<ArrayList<GameBlock?>>()
        locations = arrayListOf<ArrayList<Location>>()
        for (x in 0 until COUNT) {
            var blockItem = arrayListOf<GameBlock?>()
            var locationItem = arrayListOf<Location>()
            for (y in 0 until COUNT) {
                blockItem.add(null)
                locationItem.add(Location(x, y))
            }
            blocks.add(blockItem)
            locations.add(locationItem)
        }


        dialogRestart = GameDialog(context)
        dialogRestart.onConfirmListener = object : GameDialog.OnConfirmListener {
            override fun onConfirm() {
                restart()
            }
        }
        dialogRestart.message = context.resources.getString(R.string.ask_restart)
        dialogRestart.confirm = context.resources.getString(R.string.confirm)

        dialogGameOver = GameDialog(context)
        dialogGameOver.setCancelable(false)
        dialogGameOver.onConfirmListener = object : GameDialog.OnConfirmListener {
            override fun onConfirm() {
                restart()
            }
        }
        dialogGameOver.message = context.resources.getString(R.string.game_over)
        dialogGameOver.confirm = context.resources.getString(R.string.restart)

        dialogAbout = GameDialog(context)
        dialogAbout.message = context.resources.getString(R.string.about_message)
        dialogAbout.confirm = context.resources.getString(R.string.thanks)

        firstStart()

    }

    fun clickRestart() {
        dialogRestart.show()
    }

    fun restart() {

        removeAllViews()

        score = 0
        setScore()

        randomBlock()
        randomBlock()
    }

    private fun randomBlock() {

        for (x in 0 until COUNT) {
            for (y in 0 until COUNT) {
                blocks[x][y] = null
            }
        }

        val count = childCount
        for (index in 0 until count) {
            var block = getChildAt(index) as GameBlock
            blocks[block.x][block.y] = block
        }

        for (x in 0 until COUNT) {
            for (y in 0 until COUNT) {
                if (blocks[x][y] == null) {
                    randomTest.add(locations[x][y])
                }
            }
        }

        if (randomTest.size == 0) {
            return
        }

        val point = randomTest[(0 until randomTest.size).random()]

        addRandomBlock(point.x, point.y)

        if (randomTest.size <= 1) {
            checkGameOver()
        }
        randomTest.clear()
    }

    private fun addRandomBlock(x: Int, y: Int) {
        var n = (1..4).random()
        if (n < 4) {
            n = 2
        }
        addBlock(x, y, n)
    }

    private fun addBlock(x: Int, y: Int, n: Int) {
        val gameBlock = GameBlock(context, blockSize, blockCornerSize, n, x, y, space)
        blocks[x][y] = gameBlock
        addView(gameBlock)
        gameBlock.startAnimation(scaleInAnimation)
    }

    private fun checkGameOver() {
        for (x in 0 until COUNT) {
            for (y in 0 until COUNT) {
                if (x < COUNT - 1 && blocks[x][y]?.number == blocks[x+1][y]?.number) {
                    return
                }
                if (y < COUNT - 1 && blocks[x][y]?.number == blocks[x][y+1]?.number) {
                    return
                }
            }
        }
        GameHelper.clearGameData()
        dialogGameOver.show()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (isMoving) {
            return true
        }

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                started = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (started) {
                    var x = event.x - startX
                    var y = event.y - startY
                    if (abs(x) >= moveSpaceMax || abs(y) >= moveSpaceMax) {
                        direction = getDirection(x, y)
                        when (direction) {
                            DIRECTION_TOP -> {
                                moveTop()
                            }
                            DIRECTION_RIGHT -> {
                                moveRight()
                            }
                            DIRECTION_BOTTOM -> {
                                moveBottom()
                            }
                            DIRECTION_LEFT -> {
                                moveLeft()
                            }
                        }
                        started = false
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                started = false
            }
            MotionEvent.ACTION_CANCEL -> {
                started = false

            }
        }
        return super.onTouchEvent(event)
    }

    private fun getDirection(x: Float, y: Float): Int {

        var direction = DIRECTION_NONE

        if (abs(x) > abs(y)) {
            if (x > 0) {
                direction = DIRECTION_RIGHT
            } else {
                direction = DIRECTION_LEFT
            }
        } else {
            if (y > 0) {
                direction = DIRECTION_BOTTOM
            } else {
                direction = DIRECTION_TOP
            }
        }
        return direction
    }

    override fun dispatchDraw(canvas: Canvas?) {

        if (rectF == null) {
            rectF = RectF(0f, 0f, size.toFloat(), size.toFloat())
        }

        canvas?.drawRoundRect(rectF!!, cornerSize, cornerSize, paintBg)

        for (x in 0..COUNT) {
            for (y in 0..COUNT) {
                canvas?.drawRoundRect((blockSize * x + space * (x + 1)).toFloat(), (blockSize * y + space * (y + 1)).toFloat(), (blockSize * (x + 1) + space * (x + 1)).toFloat(), (blockSize * (y + 1) + space * (y + 1)).toFloat(), blockCornerSize, blockCornerSize, paintBlockBg)
            }
        }
        super.dispatchDraw(canvas)
    }

    private fun moveTop() {
        for (x in 0 until COUNT) {
            var lastGameBlock = blocks[x][0]
            var lastLocation: Int
            if (lastGameBlock == null) {
                lastLocation = -1
            } else {
                lastLocation = 0
            }
            for (y in 1 until COUNT) {
                blocks[x][y]?.apply {
                    if (lastGameBlock == null) {
                        lastGameBlock = this
                        setEndY(++lastLocation)
                    } else {
                        if (lastGameBlock!!.number == number) {
                            goneBlocks.add(lastGameBlock!!)
                            lastGameBlock = null
                            merge()
                            score += number * 2
                            setEndY(lastLocation)
                        } else {
                            lastGameBlock = this
                            setEndY(++lastLocation)
                        }
                    }
                    if (needMoveY()) {
                        moveBlocks.add(this)
                    }
                }
            }
        }

        startYAnimation()
    }

    private fun moveBottom() {
        for (x in 0 until COUNT) {
            var lastGameBlock = blocks[x][COUNT-1]
            var lastLocation: Int
            if (lastGameBlock == null) {
                lastLocation = COUNT
            } else {
                lastLocation = COUNT - 1
            }
            for (y in COUNT-2 downTo 0) {
                blocks[x][y]?.apply {
                    if (lastGameBlock == null) {
                        lastGameBlock = this
                        setEndY(--lastLocation)
                    } else {
                        if (lastGameBlock!!.number == number) {
                            goneBlocks.add(lastGameBlock!!)
                            lastGameBlock = null
                            merge()
                            score += number * 2
                            setEndY(lastLocation)
                        } else {
                            lastGameBlock = this
                            setEndY(--lastLocation)
                        }
                    }
                    if (needMoveY()) {
                        moveBlocks.add(this)
                    }
                }
            }
        }

        startYAnimation()
    }

    private fun startYAnimation() {

        if (moveBlocks.size == 0) {
            return
        }

        setScore()

        isMoving = true
        var valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = ANIMATION_DURATION
        valueAnimator.addUpdateListener { p0 ->
            var percent = p0.animatedValue as Float
            if (percent == 1f) {
                for (item in moveBlocks) {
                    item.moveY(percent)
                    item.endY(this)
                }
                moveBlocks.clear()
                for (item in goneBlocks) {
                    removeView(item)
                }
                goneBlocks.clear()
                randomBlock()
                isMoving = false
            } else {
                for (item in moveBlocks) {
                    item.moveY(percent)
                }
            }
        }
        valueAnimator.start()
    }

    private fun moveLeft() {
        for (y in 0 until COUNT) {
            var lastGameBlock = blocks[0][y]
            var lastLocation: Int
            if (lastGameBlock == null) {
                lastLocation = -1
            } else {
                lastLocation = 0
            }
            for (x in 1 until COUNT) {
                blocks[x][y]?.apply {
                    if (lastGameBlock == null) {
                        lastGameBlock = this
                        setEndX(++lastLocation)
                    } else {
                        if (lastGameBlock!!.number == number) {
                            goneBlocks.add(lastGameBlock!!)
                            lastGameBlock = null
                            merge()
                            score += number * 2
                            setEndX(lastLocation)
                        } else {
                            lastGameBlock = this
                            setEndX(++lastLocation)
                        }
                    }
                    if (needMoveX()) {
                        moveBlocks.add(this)
                    }
                }
            }
        }
        startXAnimation()
    }

    private fun moveRight() {
        for (y in 0 until COUNT) {
            var lastGameBlock = blocks[COUNT-1][y]
            var lastLocation: Int
            if (lastGameBlock == null) {
                lastLocation = COUNT
            } else {
                lastLocation = COUNT - 1
            }
            for (x in COUNT-2 downTo 0) {
                blocks[x][y]?.apply {
                    if (lastGameBlock == null) {
                        lastGameBlock = this
                        setEndX(--lastLocation)
                    } else {
                        if (lastGameBlock!!.number == number) {
                            goneBlocks.add(lastGameBlock!!)
                            lastGameBlock = null
                            merge()
                            score += number * 2
                            setEndX(lastLocation)
                        } else {
                            lastGameBlock = this
                            setEndX(--lastLocation)
                        }
                    }
                    if (needMoveX()) {
                        moveBlocks.add(this)
                    }
                }
            }
        }

        startXAnimation()
    }

    private fun startXAnimation() {

        if (moveBlocks.size == 0) {
            return
        }

        setScore()

        isMoving = true
        var valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = ANIMATION_DURATION
        valueAnimator.addUpdateListener { p0 ->
            var percent = p0.animatedValue as Float
            if (percent == 1f) {
                for (item in moveBlocks) {
                    item.moveX(percent)
                    item.endX(this)
                }
                moveBlocks.clear()
                for (item in goneBlocks) {
                    removeView(item)
                }
                goneBlocks.clear()
                randomBlock()
                isMoving = false
            } else {
                for (item in moveBlocks) {
                    item.moveX(percent)
                }
            }
        }
        valueAnimator.start()
    }

    private fun setScore() {
        scoreView?.text = score.toString()
        if (GameHelper.setNewScore(score)) {
            bestView?.text = score.toString()
        }
    }

    private fun gameOver() {
        dialogGameOver.show()
    }

    fun about() {
        dialogAbout.show()
    }

    fun save() {
        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("count", COUNT)
        jsonObject.put("score", score)
        val jsonArray: JSONArray = JSONArray()
        for (x in 0 until COUNT) {
            for (y in 0 until COUNT) {
                blocks[x][y]?.apply {
                    val block: JSONObject = JSONObject()
                    block.put("x", x)
                    block.put("y", y)
                    block.put("n", number)
                    jsonArray.put(block)
                }
            }
        }
        jsonObject.put("data", jsonArray)
        GameHelper.saveGameData(jsonObject.toString())
    }

    private fun firstStart() {
        val data = GameHelper.getGameData()
        if (TextUtils.isEmpty(data)) {
            restart()
        } else {
            val jsonObject = JSONObject(data)
            val count = jsonObject.getInt("count")
            if (count != COUNT) {
                GameHelper.resetBestScore()
                restart()
            } else {
                val jsonArray = jsonObject.getJSONArray("data")
                for (index in 0 until jsonArray.length()) {
                    val block = jsonArray.getJSONObject(index)
                    addBlock(block.getInt("x"), block.getInt("y"), block.getInt("n"))
                }
                score = jsonObject.getInt("score")
                setScore()
            }
        }
    }
}