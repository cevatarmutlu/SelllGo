package com.examples.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productImage: ImageView
    private lateinit var productNameTextView: TextView
    private lateinit var productPriceTextView: TextView
    private lateinit var productInfoTextView: TextView

    private var productName: String = ""
    private var productPrice: Int = 0
    private var productInfo: String = ""
    private var productImgUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productImage = findViewById(R.id.product_img)
        productNameTextView = findViewById(R.id.product_name)
        productPriceTextView = findViewById(R.id.product_price)
        productInfoTextView = findViewById(R.id.product_info)

        getProductData()
        setProductData()
    }

    fun getProductData(): Unit {
        if (intent.hasExtra("productName") && intent.hasExtra("productPrice") && intent.hasExtra("productInfo") && intent.hasExtra("productImgUrl"))
        {
            productName = intent.getStringExtra("productName")!!
            productPrice = intent.getIntExtra("productPrice", 0)
            productInfo = intent.getStringExtra("productInfo")!!
            productImgUrl = intent.getStringExtra("productImgUrl")!!
        }else {
            Toast.makeText(this, "Veriler alınırken bir hata oluştu", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun setProductData(): Unit {
        productNameTextView.text = productName
        productPriceTextView.text = productPrice.toString()
        productInfoTextView.text = productInfo
        Picasso.get().load(productImgUrl).into(productImage)

    }
}
