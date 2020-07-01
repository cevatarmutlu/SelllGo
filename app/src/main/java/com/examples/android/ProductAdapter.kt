package com.examples.android

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.examples.android.firabaseDataClass.DataClassProduct
import com.squareup.picasso.Picasso

class ProductAdapter(private val context: Context, private val productList: ArrayList<DataClassProduct>):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>(), Filterable
{
    private var productListFull = ArrayList<DataClassProduct>(productList)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.product_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.text = product.productName
        holder.productPrice.text = product.productPrice.toString()
        holder.productInfo.text = if (product.productInfo!!.length >= 10) {
            product.productInfo.substring(0, 9)
        }else{
            product.productInfo
        }
        Picasso.get().load(product.productImageUrl).into(holder.productImageView)
        holder.productLayout.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java).let {
                it.putExtra("productName", product.productName)
                it.putExtra("productPrice", product.productPrice)
                it.putExtra("productInfo", product.productInfo)
                it.putExtra("productImgUrl", product.productImageUrl)
            }
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.list_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.list_product_price)
        val productInfo: TextView = itemView.findViewById(R.id.list_product_info)
        val productImageView: ImageView = itemView.findViewById(R.id.list_product_img)
        val productLayout: LinearLayout = itemView.findViewById(R.id.product_list_item_layout)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredList = ArrayList<DataClassProduct>()

                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(productListFull)
                }else {
                    var filterPattern = constraint.toString()
                    productListFull.forEach {
                        if (it.productName!!.contains(filterPattern)){
                            filteredList.add(it)
                        }
                    }
                }
                var result: FilterResults = FilterResults()
                result.values = filteredList

                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                productList.clear()
                productList.addAll(results!!.values as List<DataClassProduct>)
                notifyDataSetChanged()
            }

        }
    }


}