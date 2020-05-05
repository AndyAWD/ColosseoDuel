package tw.com.andyawd.colosseoduel

import android.content.Context
import android.net.Uri
import java.io.File

class FileManager {
    companion object {
        const val FORWARD_SLASH = "/"
        const val MP4 = ".mp4"
        val instance = FileManagerHolder.fileManager
    }

    private object FileManagerHolder {
        val fileManager = FileManager()
    }

    /**
     * 取得路徑資料夾字串
     * 例如：/storage/emulated/0/Android/data/tw.com.andyawd.colosseoduel/files/Movies/
     */
    fun getFolderPath(applicationContext: Context, environment: String): String =
        "${applicationContext.applicationContext.getExternalFilesDir(environment)}$FORWARD_SLASH"

    /**
     * 從路徑資料夾建立檔案
     * 例如：/storage/emulated/0/Android/data/tw.com.andyawd.colosseoduel/files/Movies/1090505.mp4
     */
    fun getFile(folderPath: String, filename: String, fileType: String): File {
        val file = File(folderPath, "$filename$fileType")

        if (file.exists()) {
            file.delete()
        }

        return file
    }

    /**
     * 組合getFile和getFolderPath的懶人包
     */
    fun getUriFromFile(
        applicationContext: Context,
        environment: String,
        filename: String,
        fileType: String
    ): Uri =
        Uri.fromFile(getFile(getFolderPath(applicationContext, environment), filename, fileType))
}