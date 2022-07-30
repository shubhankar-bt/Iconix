package com.shubhankaranku.iconix.fragments.otherFragments

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.shubhankaranku.iconix.R
import com.shubhankaranku.iconix.repository.IconifyRepository
import com.shubhankaranku.iconix.utils.Resource
import com.shubhankaranku.iconix.viewModel.iconDetails.IconDetailsViewModel
import com.shubhankaranku.iconix.viewModel.iconDetails.IconDetailsViewModelFactory
import kotlinx.android.synthetic.main.fragment_icons_details.*
import kotlinx.android.synthetic.main.fragment_icons_details.view.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


open class IconsDetailsFragment : Fragment(R.layout.fragment_icons_details) {

    private val args: IconsDetailsFragmentArgs by navArgs()
    lateinit var iconDetailsViewModel: IconDetailsViewModel
    lateinit var snackbar: Snackbar
    var user_id: Int = 0
    lateinit var author: String
    private val REQUEST_CODE = 2


    private var totalFileSize: Int = 0
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var url: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackbar = Snackbar.make(view, "Loading...", Snackbar.LENGTH_INDEFINITE)

        val icon_id = args.iconId
        val icon_url = args.iconUrl
        val icon_name = args.iconName
        val author_name = args.authorName
        val icon_type = args.iconType
        val icon_price = args.iconPrice
        val icon_license = args.iconLicense

        Glide.with(this).load(icon_url).placeholder(R.drawable.placeholder).into(iv_icon)
        if (icon_price == 0){
            premiumlogo.visibility = View.GONE
        }else{
            premiumlogo.visibility = View.VISIBLE
        }



        tv_icon_name.text = icon_name
        tv_icon_type.text = "Type: $icon_type"
        tv_icon_license.text = "License: $icon_license"


        tv_icon_author.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("user_id", user_id)
            bundle.putString("name", author)
            bundle.putString("license", icon_license)
            findNavController().navigate(
                R.id.action_iconsDetailsFragment_to_authorDetailsFragment,
                bundle
            )
        }

        card_download_btn.setOnClickListener {
            if (!context?.let { it1 -> isPermissionGranted(it1) }!!) {
                askForPermission(context as Activity)
            } else {
                if (icon_price==0){
                    downloadImage(requireContext(), icon_url, icon_name)
                }else{
                    Toast.makeText(requireContext(),"Premium icons can't be downloaded", Toast.LENGTH_SHORT).show()
                }
            }
        }


        val repository = IconifyRepository()
        val iconDetailsViewModelFactory =
            IconDetailsViewModelFactory(requireActivity().application, repository)

        iconDetailsViewModel = ViewModelProvider(
            requireActivity(),
            iconDetailsViewModelFactory
        ).get(IconDetailsViewModel::class.java)


        iconDetailsViewModel.getIconDetails(icon_id)

        iconDetailsViewModel.iconDetails.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { iconDetails ->
                            snackbar.dismiss()
                            user_id = iconDetails.iconset.author.user_id
                            author = iconDetails.iconset.author.name
                            tv_icon_author.text = author
                        }
                    }
                    is Resource.Error -> {
                        snackbar.dismiss()
                        Snackbar.make(
                            view,
                            "Failed due to ${response.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }


                    is Resource.Loading -> {
                        snackbar.show()
                    }
                }
            })

    }

    fun downloadImage(view: View) {

    }

    fun askForPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    fun isPermissionGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    fun downloadImage(context: Context, downloadUrl: String, icon_name: String) {
        Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
        try {
            var bitmap = Glide.with(this)
                .asBitmap()
                .load(downloadUrl)
                .apply(RequestOptions().override(100).downsample(DownsampleStrategy.CENTER_INSIDE).skipMemoryCache(true).diskCacheStrategy(
                    DiskCacheStrategy.NONE))
                .submit().get()
            val wrapper = ContextWrapper(context)
            var file = wrapper.getDir(Environment.getExternalStorageDirectory().path +
                    File.separator + "Iconix", Context.MODE_PRIVATE)
            file = File(file, "$icon_name.jpg")
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            out.flush()
            out.close()
        }
        catch (e: Exception) {
            println(e)
        }

        Toast.makeText(context, "$icon_name Downloaded", Toast.LENGTH_SHORT).show()
    }



}





