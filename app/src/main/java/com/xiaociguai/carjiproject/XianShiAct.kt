package com.xiaociguai.carjiproject

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 车辆状态数据模型 —— 界面与数据之间的"契约"。
 *
 * 不管以后数据从哪来（手写的假数据 / 车机 Car API / 网络接口），
 * 都先组装成一个 CarStatus，再交给界面渲染。
 * 默认值全部为 0，所以 CarStatus() 就代表"模板的空状态"。
 */
data class CarStatus(
    val speed: Int = 0,           // 车速，单位 km/h
    val range: Int = 0,           // 剩余续航，单位 km
    val battery: Int = 0,         // 电量，单位 %
    val temperature: Int = 0,     // 车外温度，单位 ℃
    val gear: String = "P",       // 档位：P / R / N / D
    val time: String = "00:00",   // 时间，如 "10:55"
    val date: String = "0000-00-00" // 日期，如 "2026-10-08"
)

class XianShiAct : AppCompatActivity() {

    // ---------- 所有"会变的数值"控件，在这里存一份引用 ----------
    // 只在这里声明，不在别处 findViewById，避免重复查找
    private lateinit var tvSpeed: TextView
    private lateinit var tvRangeValue: TextView
    private lateinit var tvBatteryValue: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvGear: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_xian_shi)

        // 让内容避开系统状态栏和导航栏
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()

        // ====================================================================
        //  ★ 唯一的数据入口 ★
        //  现在固定传一个全 0 的 CarStatus，所以界面是静止的模板。
        //
        //  以后要让数字动起来，只需要换掉下面这一行，例如：
        //      render(CarStatus(speed = 72, range = 320, battery = 82,
        //                       temperature = 24, gear = "D",
        //                       time = "10:55", date = "2026-10-08"))
        //
        //  如果想持续刷新，就把 render(...) 放进定时器（Handler / 协程）里反复调用，
        //  界面代码一行都不用改。
        // ====================================================================
        render(CarStatus())
    }

    /** 把布局里的数值控件都找出来，只做一次 */
    private fun bindViews() {
        tvSpeed = findViewById(R.id.tvSpeed)
        tvRangeValue = findViewById(R.id.tvRangeValue)
        tvBatteryValue = findViewById(R.id.tvBatteryValue)
        tvTemp = findViewById(R.id.tvTemp)
        tvGear = findViewById(R.id.tvGear)
        tvTime = findViewById(R.id.tvTime)
        tvDate = findViewById(R.id.tvDate)
    }

    /**
     * 渲染：把一份车辆状态数据显示到界面上。
     *
     * 所有数值的赋值都集中在这一个函数里，
     * 以后要加字段 / 改格式（比如加个小数、换个单位）都只改这里。
     */
    private fun render(status: CarStatus) {
        tvSpeed.text = status.speed.toString()
        tvRangeValue.text = "${status.range} km"
        tvBatteryValue.text = "${status.battery}%"
        tvTemp.text = "${status.temperature}°C"
        tvGear.text = status.gear
        tvTime.text = status.time
        tvDate.text = status.date
    }
}
