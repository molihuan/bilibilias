package com.imcys.bilibilias.home.ui.fragment.home.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.databinding.ItemRcmdVideoBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.model.HomeRCMDVideoBean
import com.imcys.bilibilias.utils.HttpUtils
import com.youth.banner.adapter.BannerAdapter

class RCMDVideoAdapter(
    val context: Context,
    val datas: MutableList<HomeRCMDVideoBean.DataBean.ItemBean>,
) :
    BannerAdapter<HomeRCMDVideoBean.DataBean.ItemBean, RCMDVideoAdapter.ViewHolder>(datas) {

    class ViewHolder(rootView: View) : RecyclerView.ViewHolder(
        rootView
    )


    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {


        val itemRcmdVideoBinding: ItemRcmdVideoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_rcmd_video, parent, false
        )

        return ViewHolder(itemRcmdVideoBinding.root)

    }

    override fun onBindView(
        holder: ViewHolder,
        data: HomeRCMDVideoBean.DataBean.ItemBean,
        position: Int,
        size: Int,
    ) {


        val itemRcmdVideoBinding: ItemRcmdVideoBinding? =
            DataBindingUtil.getBinding(holder.itemView)


        itemRcmdVideoBinding?.apply {

            holder.itemView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("bvId", data.bvid)
                intent.setClass(context, AsVideoActivity::class.java)
                context.startActivity(intent)
            }

            itemRcmdLikeLottie.setAnimation(R.raw.home_like)

            itemBean = data

            itemRcmdLikeLottie.progress = data.likeState

            itemHomeRcmdLike.setOnClickListener {
                Log.d("偏移回调", data.title + data.likeState)

                data.likeState = 1f
                itemRcmdLikeLottie.playAnimation()
                likeVideo(data.bvid)
            }

            executePendingBindings()


        }
    }

    private fun likeVideo(bvid: String) {
        HttpUtils
            .addHeader("cookie", App.cookies)
            .addParam("bvid", bvid)
            .addParam("like", "1")
            .addParam("csrf", App.biliJct)
            .post(BilibiliApi.likeVideoPath, LikeVideoBean::class.java) {
                App.handler.post {
                    if (it.code == 0) {
                        Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "点赞失败，${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }
}