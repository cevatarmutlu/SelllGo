package com.examples.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.examples.android.firabaseDataClass.DataClassProduct
import com.examples.android.myDialogAlert.LoadingAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class AddProductActivity : AppCompatActivity() {

    companion object {
        private const val IMAGE_REQUEST = 1
    }

    private lateinit var productNameEditText: EditText
    private lateinit var productName: String
    private lateinit var productPriceEditText: EditText
    private var productPrice: Int = 0
    private lateinit var productInfoEditText: EditText
    private lateinit var productInfo: String
    private lateinit var addProductButton: Button
    private lateinit var addProductImageButton: Button
    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var db = FirebaseFirestore.getInstance()
    private var storage = FirebaseStorage.getInstance()
    private var productImgUrl: String? = null
    private lateinit var imageUri: Uri
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        productNameEditText     = findViewById(R.id.add_product_name)
        productPriceEditText    = findViewById(R.id.add_product_price)
        productInfoEditText     = findViewById(R.id.add_product_info)
        addProductButton        = findViewById(R.id.add_product_button)
        addProductImageButton   = findViewById(R.id.add_product_img_button)
        val loadingAlertDialog  = LoadingAlertDialog(this)


            addProductImageButton.setOnClickListener {
                val intent = Intent().let {
                    it.type = "image/*"
                    it.setAction(Intent.ACTION_GET_CONTENT)
                }
                startActivityForResult(intent, IMAGE_REQUEST)
            }

        addProductButton.setOnClickListener {
            loadingAlertDialog.show()
            productName     = productNameEditText.text.toString()
            productPrice    = productPriceEditText.text.toString().toInt()
            productInfo     = productInfoEditText.text.toString()



            val productRef2 = db.collection("products")
            val userProduct2 = db.collection("users").document(user.uid)
            val productId = productRef2.document().id

            val productImageId = db.collection("Users").document().id
            val reference = storage.reference.child("products/$productImageId")
            var uploadImg = reference.putFile(imageUri)


            uploadImg.continueWithTask {task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Resim Yüklenirken bir hata oluştu", Toast.LENGTH_SHORT).show()
                    task.exception?.let {e->
                        throw e
                    }
                }
                reference.downloadUrl
            }
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        productImgUrl = it.result.toString()

                        val product = DataClassProduct(
                            user.uid,
                            productName,
                            productPrice,
                            productInfo,
                            productImgUrl
                        )


                        db.runTransaction {transaction->

                            val userProductList = transaction.get(userProduct2)
                            val userProducts = userProductList.get("products") as List<String>
                            val newUserProducts = userProducts.plus(productId)
                            transaction.update(userProduct2, "products", newUserProducts)
                            transaction.set(productRef2.document(productId), product)
                        }
                            .addOnCompleteListener {
                                loadingAlertDialog.dismiss()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener {ex->
                                Toast.makeText(this, "Yükleme başarısız", Toast.LENGTH_SHORT).show()
                                throw ex
                            }
                    }else {
                        it.exception?.let {e->
                            throw e
                        }
                        Toast.makeText(this, "Resim Yüklenirken bir hata oluştu", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            Picasso.get().load(imageUri).into(findViewById<ImageView>(R.id.add_product_img))
        }
    }
}
