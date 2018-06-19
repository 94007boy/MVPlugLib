package djjie.com.mvpmodifydemo;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;


/**
 * Created by xiaolv on 16/8/13.
 */
public class ImageViewAttrAdapter {

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("avatar")
    public static void getHeaderImage(final ImageView iv, String avatar) {
        if (TextUtils.isEmpty(avatar)){
            iv.setImageResource(R.mipmap.default_header);
        }else {
            Glide.with(iv.getContext()).load(avatar).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv);
//            Glide.with(iv.getContext()).load(avatar).asBitmap().centerCrop().placeholder(R.mipmap.default_header).into(new BitmapImageViewTarget(iv) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable =
//                            RoundedBitmapDrawableFactory.create(iv.getContext().getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    iv.setImageDrawable(circularBitmapDrawable);
//                }
//            });
        }
    }
//    @BindingAdapter("cover")
//    public static void getCoverImage(ImageView view, String imgUrl) {
//        if (!TextUtils.isEmpty(imgUrl) && imgUrl.endsWith(".gif")){
//            Glide.with(view.getContext().getApplicationContext()).load(imgUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.default_image).centerCrop().dontAnimate().into(view);
//        }else {
//            Glide.with(view.getContext().getApplicationContext()).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.default_image).centerCrop().dontAnimate().into(view);
//        }
//    }
//
//    @BindingAdapter({"img"})
//    public static void getInternetImage(final ImageView mImageView, String imgUrl){
//        if (!TextUtils.isEmpty(imgUrl) && imgUrl.endsWith(".gif")){
//            Glide.with(mImageView.getContext().getApplicationContext()).load(imgUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.default_image).fitCenter().dontAnimate().into(mImageView);
//        }else {
//            Glide.with(mImageView.getContext().getApplicationContext()).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.default_image).fitCenter().dontAnimate().into(new GlideDrawableImageViewTarget(mImageView){
//                @Override
//                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                    super.onResourceReady(resource, animation);
//                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    mImageView.setImageDrawable(resource);
//                }
//
//                @Override
//                public void onLoadStarted(Drawable placeholder) {
//                    super.onLoadStarted(placeholder);
//                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
//            });
//        }
//    }
}
