package art.berberman.wechatdog

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.client.WebClientSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File


fun main() = runBlocking<Unit> {
    val client = WebClientSession.create(WebClient.create(Vertx.vertx(), WebClientOptions().apply {
        userAgent = fakeUserAgent
    }))

    val uuid = getUUID(client)
    val qrcode = getQRCode(client, uuid)
    qrcode.bytes.writeToFile("qrcode.png")
    withContext(Dispatchers.IO) {
        Runtime.getRuntime()
            .exec("rundll32.exe C:\\Windows\\System32\\shimgvw.dll,ImageView_Fullscreen ${File("qrcode.png").absolutePath}")
    }
    var status: LoginStatus? = null
    var result: LoginResult? = null
    while (status != LoginStatus.SUCCESS) {
        qrLogin(
            client, uuid,
            if (status == LoginStatus.SCANNED)
                0
            else 1
        ).let {
            status = it.first
            it.second?.let { r -> result = r }
        }
    }
    println("result: $result")

    val loginPage = newLoginPage(client, result!!)
    println("page: $loginPage")

    val initResult = init(client, loginPage)
    println("init: $initResult")

    statusNotify(client, loginPage, result!!, initResult)

    val contact = getContact(client, loginPage)
    println("contact: $contact")


}