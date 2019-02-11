package art.berberman.wechatdog

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main() {
    val vertx = Vertx.vertx()
    vertx.createHttpServer().let {
        it.requestHandler { req ->
            GlobalScope.launch(vertx.dispatcher()) {
                val body: String = suspendCoroutine<Buffer> {
                    req.bodyHandler { b ->
                        it.resume(b)
                    }
                }.toString()
                req.response().end(
                    """
                      Welcome to vertx echo server!

                      Headers: ${req.headers().joinToString()}

                      Params: ${req.params().joinToString()}

                      Body: $body

                  """.trimIndent()
                )
            }


        }
        it.listen()
    }
}