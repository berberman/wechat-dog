package art.berberman.wechatdog

import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import kotlinx.coroutines.runBlocking


fun main() = runBlocking<Unit> {
    val client = WebClient.create(Vertx.vertx(), WebClientOptions().apply {
        userAgent = fakeUserAgent
    })

    val uuid = getUUID(client)
    val qrcode = getQRCode(client, uuid)
    qrcode.bytes.writeToFile("qrcode.png")
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
    println("contact $contact")
}