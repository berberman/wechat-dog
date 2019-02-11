package art.berberman.wechatdog

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

annotation class NoArgs

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
@NoArgs
data class BaseRequest(
    val uin: String,
    val sid: String,
    val sKey: String,
    val deviceID: String
) {
    companion object {
        fun fromLoginPage(loginPage: LoginPage) =
            with(loginPage) {
                BaseRequest(wxuin, wxsid, skey, deviceId)
            }
    }
}


@Suppress("CanBePrimaryConstructorProperty")
class BaseResponse(returnCode: Int, errorMessage: String) {
    constructor() : this(-1, "")

    @JsonProperty("Ret")
    var returnCode: Int = returnCode
    @JsonProperty("ErrMsg")
    var errorMessage: String = errorMessage

    override fun toString(): String = " BaseResponse(returnCode= $returnCode, errorMessage= $errorMessage)"
}

data class LoginResult(
    val decoded: Map<String, String>
) {
    val ticket: String by decoded
    val uuid: String by decoded
    val lang: String by decoded
    val scan: String by decoded
}

enum class LoginStatus(val status: Int) {
    NOTHING(0), SCANNED(201), SUCCESS(200), TIMEOUT(408)
}

data class LoginPage(
    val decoded: Map<String, String>
) {
    val ret: String by decoded
    val message: String by decoded
    val skey: String by decoded
    val wxsid: String by decoded
    val wxuin: String by decoded
    val pass_ticket: String by decoded
    val isgrayscale: String by decoded
}

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgs
data class InitResult(
    val baseResponse: BaseResponse,
    val syncKey: SyncKey,
    val user: User
) {
    @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
    @NoArgs
    data class SyncKey(
        val count: Int,
        val list: Array<KeyEntry>
    ) {
        @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
        @NoArgs
        data class KeyEntry(
            val key: Int,
            val `val`: Int
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SyncKey

            if (count != other.count) return false
            if (!list.contentEquals(other.list)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = count
            result = 31 * result + list.contentHashCode()
            return result
        }
    }

    @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgs
    data class User(
        val userName: String,
        val nickName: String,
        val uin: String
    )
}

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
@NoArgs
data class SentMessage(
    val baseResponse: BaseResponse,
    val msgID: String,
    val localID: String
)

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
@NoArgs
data class Contact(
    val baseResponse: BaseResponse,
    val memberCount: Int,
    val memberList: Array<Member>,
    val seq: Int
) {
    @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgs
    data class Member(
        val uin: String,
        val userName: String,
        val nickName: String,
        val headImgUrl: String,
        val contactFlag: Int,
        val memberCount: Int,
        val memberList: Array<Member>,
        val remarkName: String,
        val hideInputBarFlag: Int,
        val sex: Int,
        val signature: String,
        val verifyFlag: Int,
        val ownerUin: String,
        val starFriend: Int,
        val appAccountFlag: Int,
        val statues: Int,
        val attrStatus: Int,
        val province: String,
        val city: String,
        val alias: String,
        val snsFlag: Int,
        val uniFriend: Int,
        val displayName: String,
        val chatRoomId: Int,
        val keyWord: String,
        val encryChatRoomId: String,
        val isOwner: Int
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (baseResponse != other.baseResponse) return false
        if (memberCount != other.memberCount) return false
        if (!memberList.contentEquals(other.memberList)) return false
        if (seq != other.seq) return false

        return true
    }

    override fun hashCode(): Int {
        var result = baseResponse.hashCode()
        result = 31 * result + memberCount
        result = 31 * result + memberList.contentHashCode()
        result = 31 * result + seq
        return result
    }
}