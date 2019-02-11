package art.berberman.wechatdog

import com.fasterxml.jackson.annotation.JsonProperty
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.core.json.JsonObject
import io.vertx.kotlin.ext.web.client.sendAwait
import io.vertx.kotlin.ext.web.client.sendJsonAwait
import io.vertx.kotlin.ext.web.client.sendJsonObjectAwait

suspend fun getUUID(webClient: WebClient) =
    webClient
        .getAbs(Api.GET_UUID)
        .addQueryParam("appid", "wx782c26e4c19acffb")
        .addQueryParam("fun", "new")
        .addQueryParam("lang", "zh_CN")
        .addQueryParam("_", timeStamp)
        .sendAwait()
        .bodyAsString()
        .let {
            uuidPattern.find(it)?.groups?.get(2)?.value ?: throw RuntimeException()
        }

suspend fun getQRCode(webClient: WebClient, uuid: String) =
    webClient
        .getAbs(Api.QR_CODE.base + "/$uuid")
        .sendAwait()
        .body()


suspend fun qrLogin(webClient: WebClient, uuid: String, tip: Int = 1) =
    webClient
        .getAbs(Api.QR_LOGIN)
        .addQueryParam("tip", tip.toString())
        .addQueryParam("uuid", uuid)
        .addQueryParam("_", timeStamp)
        .sendAwait()
        .bodyAsString()
        .let { result ->
            val status =
                LoginStatus
                    .values()
                    .find {
                        it
                            .status == qrLoginCodePattern
                            .find(result)
                            ?.groups
                            ?.get(1)
                            ?.value
                            ?.toInt()
                    } ?: throw RuntimeException()
            val feedback = status.takeIf { it == LoginStatus.SUCCESS }?.run {
                getLoginResult(result)
            }
            status to feedback
        }

suspend fun newLoginPage(webClient: WebClient, loginResult: LoginResult) =
    webClient
        .getAbs(Api.NEW_LOGIN_PAGE)
        .apply { queryParams().addAll(loginResult.decoded) }
        .addQueryParam("fun", "new")
        .sendAwait()
        .let { processXML(it.bodyAsString()) }
        .let(::LoginPage)

suspend fun init(webClient: WebClient, loginPage: LoginPage): InitResult =
    webClient
        .postAbs(Api.INIT)
        .addQueryParam("pass_ticket", loginPage.pass_ticket)
        .addQueryParam("skey", loginPage.skey)
        .addQueryParam("r", timeStamp)
        .sendJsonAwait(object : Any() {
            @JsonProperty("BaseRequest")
            val r = BaseRequest(
                loginPage.wxuin,
                loginPage.wxsid,
                loginPage.skey,
                deviceId
            )
        })
        .bodyAsJson(InitResult::class.java)

suspend fun statusNotify(
    webClient: WebClient,
    loginPage: LoginPage,
    loginResult: LoginResult,
    initResult: InitResult
) =
    webClient
        .postAbs(Api.STATUS_NOTIFY)
        .addQueryParam("lang", loginResult.lang)
        .addQueryParam("pass_ticket", loginPage.pass_ticket)
        .sendJsonObjectAwait(
            JsonObject(
                "BaseRequest" to
                        BaseRequest(
                            loginPage.wxuin,
                            loginPage.wxsid,
                            loginPage.skey,
                            deviceId
                        ),
                "Code" to 3,
                "FromUserName" to initResult.user.userName,
                "ToUserName" to initResult.user.userName,
                "ClientMsgId" to timeStamp
            )
        )
        .bodyAsJsonObject()
        .getJsonObject("BaseResponse")
        .let { BaseResponse(it.getInteger("Ret"), it.getString("ErrMsg")) }

suspend fun getContact(webClient: WebClient, loginPage: LoginPage) =
    webClient
        .postAbs(Api.GET_CONTACT)
        .addQueryParam("pass_ticket", loginPage.pass_ticket)
        .addQueryParam("skey", loginPage.skey)
        .addQueryParam("r", timeStamp)
        .sendJsonObjectAwait(JsonObject())
        .bodyAsString()

//suspend fun getBatchContact(webClient: WebClient,loginPage: LoginPage)=
//        webClient
//            .postAbs()