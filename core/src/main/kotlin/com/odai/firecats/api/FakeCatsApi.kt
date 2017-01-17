package com.odai.firecats.api

import com.odai.firecats.cat.model.Cat
import com.odai.firecats.cats.model.Cats
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FakeCatsApi : CatApi {

    private var favouriteCats = Cats(listOf())

    override fun getFavouriteCats(): Flowable<Cats> {
        return Flowable.just(favouriteCats).delay(2, TimeUnit.SECONDS, Schedulers.trampoline())
    }

    override fun getCats(): Flowable<Cats> {
        return Flowable.just(fakeCats()).delay(2, TimeUnit.SECONDS, Schedulers.trampoline())
    }

    override fun addToFavourite(cat: Cat): Flowable<Cat> {
        return Flowable.just(cat).delay(2, TimeUnit.SECONDS, Schedulers.trampoline())
                .doOnNext {
                    favouriteCats = favouriteCats.add(cat)
                }
    }

    override fun removeFromFavourite(cat: Cat): Flowable<Cat> {
        return Flowable.just(cat).delay(2, TimeUnit.SECONDS, Schedulers.trampoline())
                .doOnNext {
                    favouriteCats = favouriteCats.remove(cat)
                }
    }

    private fun fakeCats() = Cats(listOf(
            Cat(100, "Continue", "https://http.cat/100"),
            Cat(101, "Switching Protocols", "https://http.cat/101"),
            Cat(200, "Ok", "https://http.cat/200"),
            Cat(201, "Created", "https://http.cat/201"),
            Cat(202, "Accepted", "https://http.cat/202"),
            Cat(204, "No Content", "https://http.cat/204"),
            Cat(206, "Partial Content", "https://http.cat/206"),
            Cat(207, "Multi Status", "https://http.cat/207"),
            Cat(300, "Multiple Choices", "https://http.cat/300"),
            Cat(301, "Moved Permanently", "https://http.cat/301"),
            Cat(302, "Found", "https://http.cat/302"),
            Cat(303, "See Other", "https://http.cat/303"),
            Cat(304, "Not Modified", "https://http.cat/304"),
            Cat(305, "Use Proxy", "https://http.cat/305"),
            Cat(307, "Temporary Redirect", "https://http.cat/307"),
            Cat(400, "Bad Request", "https://http.cat/400"),
            Cat(401, "Unauthorized", "https://http.cat/401"),
            Cat(402, "Payment Required", "https://http.cat/402"),
            Cat(403, "Forbidden", "https://http.cat/403"),
            Cat(404, "Not Found", "https://http.cat/404"),
            Cat(405, "Method Not Allowed", "https://http.cat/405"),
            Cat(406, "Not Acceptable", "https://http.cat/406"),
            Cat(408, "Request Timeout", "https://http.cat/408"),
            Cat(409, "Conflict", "https://http.cat/409"),
            Cat(410, "Gone", "https://http.cat/410"),
            Cat(411, "Length Required", "https://http.cat/411"),
            Cat(412, "Precondition Failed", "https://http.cat/412"),
            Cat(413, "Request Entity Too Large", "https://http.cat/413"),
            Cat(414, "Request URI Too Long", "https://http.cat/414"),
            Cat(415, "Unsupported Media Type", "https://http.cat/415"),
            Cat(416, "Requested Range Not Satisfiable", "https://http.cat/416"),
            Cat(417, "Expectation Failed", "https://http.cat/417"),
            Cat(418, "I'm a Teapot", "https://http.cat/418"),
            Cat(422, "Unprocessable Entity", "https://http.cat/422"),
            Cat(423, "Locked", "https://http.cat/423"),
            Cat(424, "Failed Dependency", "https://http.cat/424"),
            Cat(425, "Unordered Collection", "https://http.cat/425"),
            Cat(429, "Too Many Requests", "https://http.cat/429"),
            Cat(431, "Request Header Fields Too Large", "https://http.cat/431"),
            Cat(444, "No Response", "https://http.cat/444"),
            Cat(450, "Blocked by Windows Parental Controls", "https://http.cat/450"),
            Cat(451, "Unavailable For Legal Reasons", "https://http.cat/451"),
            Cat(500, "Internal Server Error", "https://http.cat/500"),
            Cat(502, "Bad Gateway", "https://http.cat/502"),
            Cat(503, "Service Unavailable", "https://http.cat/503"),
            Cat(506, "Variant Also Negotiate", "https://http.cat/506"),
            Cat(507, "Insufficient Storage", "https://http.cat/507"),
            Cat(508, "Loop Detected", "https://http.cat/508"),
            Cat(509, "Bandwidth Limit Exceeded", "https://http.cat/509"),
            Cat(599, "Network Connect Timeout", "https://http.cat/599")
    ))

}
