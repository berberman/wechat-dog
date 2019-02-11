package art.berberman.wechatdog

enum class Api(val base: String) {
    GET_UUID("https://login.weixin.qq.com/jslogin"),
    QR_CODE("https://login.weixin.qq.com/qrcode"),
    QR_LOGIN("https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login"),
    NEW_LOGIN_PAGE("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage"),
    INIT("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit"),
    STATUS_NOTIFY("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxstatusnotify"),
    GET_CONTACT("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact"),
    GET_CONTACT_BATCH("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxbatchgetcontact"),
    SYNC_CHECK1("https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin/synccheck"),
    SYNC_CHECK2("https://webpush.wx2.qq.com/cgi-bin/mmwebwx-bin/synccheck"),
    SYNC_CHECK3("https://webpush.wx8.qq.com/cgi-bin/mmwebwx-bin/synccheck"),
    SYNC_CHECK4("https://webpush.wx.qq.com/cgi-bin/mmwebwx-bin/synccheck"),
    SYNC_CHECK5("https://webpush.web2.wechat.com/cgi-bin/mmwebwx-bin/synccheck"),
    SYNC_CHECK6("https://webpush.web.wechat.com/cgi-bin/mmwebwx-bin/synccheck"),
    SYNC("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync"),
    SEND_MSG("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg"),
    REVOKE_MSG("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxrevokemsg"),
    SEND_MSG_EMOTION("https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendemoticon"),
    GET_ICON("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgeticon"),
    GET_HEAD_IMG("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetheadimg"),
    GET_MSG_IMG("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetmsgimg"),
    GET_VIDEO("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetvideo"),
    GET_VOICE("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetvoice"),

    LOCAL_TEST("http://127.0.0.1")
}
