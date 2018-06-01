package com.xlstore.irchristianscott.xlstoreapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.xlstore.irchristianscott.xlstoreapp.models.MAIN_API_URL
import com.xlstore.irchristianscott.xlstoreapp.models.ProductCommentModel
import kotlinx.android.synthetic.main.fragment_comments.*
import okhttp3.*
import java.io.IOException


class CommentsFragment() : Fragment() {

    companion object {
        fun newInstance(): CommentsFragment {
            return CommentsFragment()
        }
    }

    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var noComments: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_comments, container, false)

        commentsRecyclerView = view.findViewById<RecyclerView>(R.id.commentsRecycleView) as RecyclerView
        noComments = view.findViewById<TextView>(R.id.no_comments) as TextView
        commentsRecyclerView.visibility = View.INVISIBLE
        noComments.visibility = View.INVISIBLE

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commentsRecyclerView.layoutManager = LinearLayoutManager(activity)

        val productID = arguments.getInt("PRODUCT_ID")
        getProductComments(productID)

    }


    private fun getProductComments(productID: Int){

        val url = "$MAIN_API_URL/product/$productID/comments/all/"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback{

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call?, response: Response?) {
                val commentString = response?.body()?.string()

                val commentJSON = GsonBuilder().create()
                val commentList: List<ProductCommentModel> = commentJSON.fromJson(commentString, object : TypeToken<List<ProductCommentModel>>() {}.type)

                activity.runOnUiThread {

                    println(commentString)

                    if(commentList.count() > 0){

                        commentsRecyclerView.visibility = View.VISIBLE
                        noComments.visibility = View.GONE
                        progressSpinner.visibility = View.GONE
                        commentsRecyclerView.adapter = CommentsAdapter(commentList)

                    } else {

                        commentsRecyclerView.visibility = View.GONE
                        noComments.visibility = View.VISIBLE
                        progressSpinner.visibility = View.GONE
                        noComments.text = "No Comments"

                    }
                }

            }

            override fun onFailure(call: Call?, e: IOException?) {
                activity.runOnUiThread {
                    Toast.makeText(activity, e!!.message.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        })
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
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

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
