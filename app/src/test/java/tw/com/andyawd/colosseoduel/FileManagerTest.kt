package tw.com.andyawd.colosseoduel

import org.junit.Assert.assertEquals
import org.junit.Test

class FileManagerTest {

    @Test
    fun getFile() {
        assertEquals(
            "\\filename.mp4",
            FileManager.instance.getFile("", "filename", ".mp4").toString()
        )
    }
}