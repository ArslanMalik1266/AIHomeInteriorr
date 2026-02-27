package org.yourappdev.homeinterior.utils

actual suspend fun uriToByteArray(context: Any, uriString: String): ByteArray {
    // iOS mein hum direct URI (file path) se data read karte hain
    val url = NSURL(string = uriString)
    val data = NSData.dataWithContentsOfURL(url) ?: return byteArrayOf()

    // NSData ko ByteArray mein convert karna
    return ByteArray(data.length.toInt()).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), data.bytes, data.length)
        }
    }
}