package com.bornfight.utils

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by tomislav on 08/06/16.
 *
 * An object which holds a few methods for image manipulation (uri,bitmap,etc)
 */
object ImageUtils {

    /**
     * Resizes the image, and returns it as [Observable]. Max resolution will be 1080x1080, with 80% quality
     */
    fun getResizedImagePath(context: Context, imageUri: Uri): Observable<Uri> {
        return Observable.fromCallable {
            // Get the dimensions of the View
            val targetW = 1080
            val targetH = 1080

            // Get the dimensions of the bitmap
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageUri.path, bmOptions)

            val inSampleSize = calculateInSampleSize(bmOptions, targetW, targetH)

            Log.d("ImageUtils", "Image resize scale factor $inSampleSize")
            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = inSampleSize
            bmOptions.inPurgeable = true

            val bitmap = BitmapFactory.decodeFile(imageUri.path, bmOptions)

            /* MEMORY LEAKS
                try {
                    bitmap = rotateImageIfRequired(context, bitmap, new Uri.Builder().path(imagePath).build());
                } catch (OutOfMemoryError e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            */

            val tempFile = File(context.cacheDir, "Image_" + System.currentTimeMillis() + ".jpg")

            val fos = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()
            bitmap.recycle()

            val oldExif = ExifInterface(imageUri.path)
            val exifOrientation = oldExif.getAttribute(ExifInterface.TAG_ORIENTATION)

            if (exifOrientation != null) {
                val newExif = ExifInterface(tempFile.path)
                newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation)
                newExif.saveAttributes()
            }

            Uri.fromFile(tempFile)
        }
    }

    /**
     * Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width
     * larger than the requested height and width.
     *
     * @param reqHeight
     * @param reqWidth
     * @return [BitmapFactory.Options.inSampleSize] value
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        Log.d("height and width", "h w $height $width")

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        if (height <= 0 || width <= 0) inSampleSize = 4

        return inSampleSize
    }

    /**
     * Returns the [ExifInterface] orientation for the given file
     *
     * @param filepath the file path (e.g. from [URI])
     * @return an integer degree value (from [ExifInterface].ORIENTATION_ROTATE_*)
     */
    fun getExifOrientation(filepath: String?): Int {// YOUR MEDIA PATH AS STRING
        if (filepath == null) return 0

        var degree = 0
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(filepath)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        if (exif != null) {
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            if (orientation != -1) {
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }

            }
        }
        return degree
    }


    /**
     * Rotates an image if required.
     * Was being used in [getResizedImagePath], but not used anymore due to memory leaks.
     */
    private fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap {

        // Detect rotation
        val rotation = getExifOrientation(selectedImage.path)
        if (rotation != 0) {
            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat())
            try {
                val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
                img.recycle()
                return rotatedImg
            } catch (oue: OutOfMemoryError) {
                oue.printStackTrace()
                return img
            }

        } else {
            return img
        }
    }

    /**
     * Returns a cropped bitmap with the specified width/height
     */
    fun getCroppedBitmap(bitmap: Bitmap?, width: Int, height: Int): Bitmap? {
        if (bitmap == null) return null
        val output = Bitmap.createBitmap(
            width,
            height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, width, height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (width / 2).toFloat(), (height / 2).toFloat(),
            (width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output
    }
}
