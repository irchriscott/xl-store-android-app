package com.xlstore.irchristianscott.xlstoreapp


import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.xlstore.irchristianscott.xlstoreapp.models.MAIN_API_URL
import com.xlstore.irchristianscott.xlstoreapp.models.Product
import com.xlstore.irchristianscott.xlstoreapp.models.ProductList
import com.xlstore.irchristianscott.xlstoreapp.models.ProductModel
import kotlinx.android.synthetic.main.fragment_products.*
import okhttp3.*
import java.io.IOException


class ProductsFragment : Fragment() {

    companion object {
        fun newInstance(): ProductsFragment{
            return ProductsFragment()
        }
    }

    lateinit var productsRecyclerView: RecyclerView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //This will return the fragment view and link products recycler view to it adapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_products, container, false)
        productsRecyclerView = view.findViewById<RecyclerView>(R.id.main_products) as RecyclerView
        productsRecyclerView.visibility = View.INVISIBLE

        return view
    }

    //call all products and get the recycler view manager

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsRecyclerView.layoutManager = LinearLayoutManager(activity)
        fetchProductList()
    }

    //Fetch Product from the API

    private fun fetchProductList(){
        val url = "$MAIN_API_URL/product/all/"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback{

            //On request response

            override fun onResponse(call: Call?, response: Response?) {
                val productsBody = response?.body()?.string()

                val productsJSON = GsonBuilder().create()
                val productsList: List<ProductModel> = productsJSON.fromJson(productsBody, object : TypeToken<List<ProductModel>>() {}.type)

                activity.runOnUiThread {
                    productsRecyclerView.visibility = View.VISIBLE
                    progressSpinner.visibility = View.GONE
                    productsRecyclerView.adapter = ProductsAdapter(ProductList(productsList))
                }

            }

            //On request failure

            override fun onFailure(call: Call?, e: IOException?) {
                activity.runOnUiThread {
                    productsRecyclerView.visibility = View.INVISIBLE
                    Toast.makeText(activity, e?.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
    }
}
