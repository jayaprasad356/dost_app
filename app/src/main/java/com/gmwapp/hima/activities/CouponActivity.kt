package com.gmwapp.hima.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmwapp.hima.Coupon
import com.gmwapp.hima.CouponAdapter
import com.gmwapp.hima.R
import com.gmwapp.hima.databinding.ActivityCouponBinding

class CouponActivity : AppCompatActivity(), CouponAdapter.OnCouponClickListener  {
    lateinit var binding: ActivityCouponBinding
    private lateinit var bestCouponsAdapter: CouponAdapter
    private lateinit var moreCouponsAdapter: CouponAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUI()
    }

    private fun initUI(){

        val rvMoreCoupons = findViewById<RecyclerView>(R.id.rv_moreCoupons)
        val rvBestCoupons = findViewById<RecyclerView>(R.id.rv_bestCoupons)
        val ivBack = findViewById<ImageView>(R.id.iv_back)



        binding.ivBack.setOnClickListener {
            var intent = Intent(this, PaymentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        val dummyCoupons = listOf(
            Coupon("1", "50% Offer", "SAVE50", "Save ₹50", "Valid on orders above ₹500", "₹500", "₹250", "1400"),
            Coupon("2", "60% Offer", "FLAT60", "Save ₹60", "Valid on orders above ₹600", "₹600", "₹240", "1500"),
        )

        val dummyCoupons2 = listOf(
            Coupon("3", "30% Offer", "CASH30", "Save ₹30", "Valid on prepaid orders", "₹100", "₹70","500"),
            Coupon("4", "₹100 Offer", "DIS100", "Save ₹100", "Valid for first-time users", "₹1000", "₹900", "200")
        )

        bestCouponsAdapter = CouponAdapter(dummyCoupons, this)
        binding.rvBestCoupons.layoutManager = LinearLayoutManager(this)
        binding.rvBestCoupons.adapter = bestCouponsAdapter

        moreCouponsAdapter = CouponAdapter(dummyCoupons2,this)
        binding.rvMoreCoupons.layoutManager = LinearLayoutManager(this)
        binding.rvMoreCoupons.adapter = moreCouponsAdapter
    }

    override fun onCouponClick(coupon: Coupon) {
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra("COUPON_CODE", coupon.couponCode)
            putExtra("ORIGINAL_PRICE", coupon.originalPrice)
            putExtra("DISCOUNTED_PRICE", coupon.discountedPrice)
            putExtra("COINS", coupon.coins)
            putExtra("SAVE", coupon.saveAmount) // Assuming saveAmount represents coins

            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP) // ✅ Add flags before startActivity

        }
        startActivity(intent)
        finish()

    }
}