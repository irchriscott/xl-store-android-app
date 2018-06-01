package com.xlstore.irchristianscott.xlstoreapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.xlstore.irchristianscott.xlstoreapp.models.ProductCommentModel
import kotlinx.android.synthetic.main.comment_row.view.*
import ru.whalemare.sheetmenu.SheetMenu
import java.lang.Thread.sleep
import java.util.logging.Handler

class CommentsAdapter(private val commentList: List<ProductCommentModel>): RecyclerView.Adapter<CommentsViewHolder>(){

    override fun getItemCount(): Int {
        return commentList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentsViewHolder{

        val layoutInflater = LayoutInflater.from(parent?.context)
        val commentRow = layoutInflater.inflate(R.layout.comment_row, parent, false)

        return CommentsViewHolder(commentRow)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentsViewHolder?, position: Int) {

        val comment = commentList[position]

        if(comment.commenter == "user"){

            holder!!.view.commenterName.text = "${comment.user!!.name} @${comment.user!!.username}"
            holder.view.commentText.text = comment.comment
            holder.view.commentDate.text = comment.comment_date
            Picasso.get().load(comment.user!!.getUserProfileImage()).into(holder!!.view.commenterImage)

        } else if (comment.commenter == "company"){

            holder!!.view.commenterName.text = comment.company!!.name
            holder.view.commentText.text = comment.comment
            holder.view.commentDate.text = comment.comment_date
            Picasso.get().load(comment.company!!.getCompanyProfileImage()).into(holder!!.view.commenterImage)

        }

        holder?.comment = comment
    }
}

class CommentsViewHolder(val view: View, var comment: ProductCommentModel? = null): RecyclerView.ViewHolder(view){

    private val userDataManager = UserDataManager()
    private val userData = userDataManager.getLoggedInUserObject(view.context)

    init {

        view.findViewById<TextView>(R.id.commenterName).setOnClickListener {

            if (comment!!.commenter == "user"){

            } else if (comment!!.commenter == "company"){

            }
        }

        view.setOnLongClickListener {

            if (comment!!.commenter == "user"){
                if(comment!!.user!!.token == userData!!.token){

                    SheetMenu().apply{
                        titleId = R.string.comment_menu_title
                        menu = R.menu.owner
                        click = MenuItem.OnMenuItemClickListener {

                            when(it.itemId){

                                R.id.delete -> {
                                    val confirmDialog = AlertDialog.Builder(view.context).create()
                                    confirmDialog.setTitle("Delete Comment")
                                    confirmDialog.setMessage("Do you want to delete this comment ???")
                                    confirmDialog.setIcon(view.context.resources.getDrawable(R.drawable.ic_help_black_24dp))

                                    confirmDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", {
                                        _, _ ->

                                        val progressDialog = ProgressDialog(view.context, ProgressDialog.STYLE_SPINNER)
                                        progressDialog.setTitle("Please Wait...")
                                        progressDialog.setMessage("Deleting Comment...")
                                        progressDialog.show()

                                        val delay = android.os.Handler()
                                        delay.postDelayed(Runnable {
                                            progressDialog.hide()
                                        }, 4000)

                                    })

                                    confirmDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", {
                                        _, _ ->
                                        confirmDialog.hide()
                                    })

                                    confirmDialog.show()
                                    return@OnMenuItemClickListener true
                                }

                                R.id.edit -> {
                                    return@OnMenuItemClickListener true
                                }
                            }
                            return@OnMenuItemClickListener false
                        }
                    }.show(view.context)

                } else {

                    SheetMenu().apply{
                        titleId = R.string.comment_menu_title
                        menu = R.menu.owner_not
                        click = MenuItem.OnMenuItemClickListener {

                            return@OnMenuItemClickListener false
                        }

                    }.show(view.context)

                }

            } else {

                SheetMenu().apply{
                    titleId = R.string.comment_menu_title
                    menu = R.menu.owner_not
                    click = MenuItem.OnMenuItemClickListener {
                        return@OnMenuItemClickListener true
                    }
                }.show(view.context)

            }

            return@setOnLongClickListener true
        }
    }

}
