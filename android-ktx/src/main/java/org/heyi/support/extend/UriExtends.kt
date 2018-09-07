package org.heyi.support.extend

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 * @author Melrose
 * @since 1.0.0
 */


fun Uri.toPath(context:Context):String{
     val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    if (isKitKat && DocumentsContract.isDocumentUri(context, this)) {
        // ExternalStorageProvider
        if ("com.android.externalstorage.documents" == this.authority) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

        } else if ("com.android.providers.downloads.documents"==this.authority) {
            val id = DocumentsContract.getDocumentId(this)
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

            return getDataColumn(context, contentUri, null, null)
        } else if ("com.android.providers.media.documents"==this.authority) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(context, contentUri, selection, selectionArgs)
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(this.getScheme(), ignoreCase = true)) {

        // Return the remote address
        return if ("com.google.android.apps.photos.content"==this.authority) this.getLastPathSegment() else getDataColumn(context, this, null, null)

    } else if ("file".equals(this.getScheme(), ignoreCase = true)) {
        return this.getPath()
    }// File
    return ""
}


fun Uri.getDataColumn(context: Context, uri: Uri?, selection: String?,
                  selectionArgs: Array<String>?): String {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return ""
}