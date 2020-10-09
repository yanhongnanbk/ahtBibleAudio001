package com.yan.ahtbibleaudio001.remote

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}
class NetworkState(val status: Status, val msg:String) {

    // use companion object when we want sth to be static

    companion object{
        val LOADED:NetworkState = NetworkState(Status.SUCCESS,"success")
        val LOADING:NetworkState = NetworkState(Status.RUNNING,"running")
        val ERROR:NetworkState = NetworkState(Status.FAILED,"st went wrong")
        val ENDOFLIST: NetworkState= NetworkState(Status.FAILED, "You have reached the end")
    }


}