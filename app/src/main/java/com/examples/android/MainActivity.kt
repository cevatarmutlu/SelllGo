package com.examples.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examples.android.firabaseDataClass.DataClassProduct
import com.examples.android.firabaseDataClass.DataClassUser
import com.examples.android.myDialogAlert.LoadingAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var userID: String
    private lateinit var mainTextView: TextView
    private lateinit var userInfoRef: DocumentReference
    private lateinit var productRef: CollectionReference
    private lateinit var productListRecycler: RecyclerView
    private lateinit var productList: ArrayList<DataClassProduct>
    private lateinit var productAdapter: ProductAdapter
    private var user: DataClassUser? = null

    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.main_toolbar))

        userID              = FirebaseAuth.getInstance().currentUser!!.uid
        mainTextView        = findViewById(R.id.main_welcome_textView)
        userInfoRef         = FirebaseFirestore.getInstance().collection("users").document(userID)
        productRef          = FirebaseFirestore.getInstance().collection("products")
        productListRecycler = findViewById(R.id.recyclerProductList)
        productList         = arrayListOf()

        val loadingAlertDialog = LoadingAlertDialog(this)
        loadingAlertDialog.show()

        userInfoRef.get()
            .addOnSuccessListener {document ->
                user = document.toObject(DataClassUser::class.java)
                mainTextView.text = "Merhaba ${user!!.name} ${user!!.surname}"
            }.addOnFailureListener {
                Toast.makeText(this, "Kullanıcı verisi çekme başarısız oldu", Toast.LENGTH_SHORT).show()
                throw it
            }


        db.collection("products").get()
            .addOnSuccessListener {
                it.documents.forEach {Document->
                    val product = Document.toObject(DataClassProduct::class.java)

                    productList.add(product!!)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Veri Çekme başarısız", Toast.LENGTH_SHORT).show()
                throw it
            }
            .addOnCompleteListener {
                productListRecycler.layoutManager = LinearLayoutManager(this)
                productAdapter = ProductAdapter(this, productList)
                productListRecycler.adapter = productAdapter
                loadingAlertDialog.dismiss()
            }

    }

    override fun onBackPressed() {}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        var item: MenuItem = menu!!.findItem(R.id.tb_search)
        var searchView: SearchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter.filter(newText)
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_add_product -> {
                val intent = Intent(this, AddProductActivity::class.java)
                startActivity(intent)
            }
            R.id.tb_account -> {
                val intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
            }
            R.id.tb_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
