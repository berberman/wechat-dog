package art.berberman.wechatdog

import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.WebClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import javax.xml.parsers.SAXParserFactory
import kotlin.random.Random

val uuidPattern = "(\\S|\\s)*window.QRLogin.uuid = \"(\\S+)\";".toRegex()

val qrLoginCodePattern = "window.code=(\\d\\d\\d);(\\S|\\s)*".toRegex()

fun getLoginResult(raw: String) = URL(raw.dropWhile { it != 'h' }).query.split("&").associate { s ->
    if (s.startsWith("uuid"))
        "uuid" to s.removePrefix("uuid=")
    else
        s.split("=").let { it[0] to it[1].removeSuffix("\";") }
}.let(::LoginResult)

suspend fun ByteArray.toImage() =
    withContext(Dispatchers.IO) {
        ImageIO.read(this@toImage.inputStream())
    }

suspend fun processXML(raw: String): Map<String, String> {
    val parameters = mutableMapOf<String, String>()
    withContext(Dispatchers.IO) {
        SAXParserFactory.newInstance().newSAXParser().parse(raw.byteInputStream(), object : DefaultHandler() {
            var current = ""
            var temp = ""
            override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
                current = qName
            }

            override fun characters(ch: CharArray, start: Int, length: Int) {
                temp = String(ch, start, length)
            }

            override fun endElement(uri: String, localName: String, qName: String) {
                if (qName == current)
                    parameters[current] = temp
            }
        })
    }
    return parameters
}


suspend fun ByteArray.writeToFile(fileName: String) =
    withContext(Dispatchers.IO) {
        File(fileName).apply {
            deleteOnExit()
            createNewFile()
        }.writeBytes(this@writeToFile)
    }

val timeStamp
    get() = System.currentTimeMillis().toString()

fun WebClient.getAbs(api: Api): HttpRequest<Buffer> = getAbs(api.base)

fun WebClient.postAbs(api: Api): HttpRequest<Buffer> = postAbs(api.base)

fun randomLiteralNumberString(n: Int) = List(n) { Random.nextInt(0, 9) }.fold("") { acc, i -> acc + i.toString() }

val deviceId = "e" + randomLiteralNumberString(15)

const val fakeUserAgent =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36"