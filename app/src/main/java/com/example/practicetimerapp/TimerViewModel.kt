package com.example.practicetimerapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.ViewModel
import java.util.Locale
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class TimerState {
    STOPPED, // 停止
    RUNNING, // 稼働中
    PAUSED, // 一時停止中
}
class TimerViewModel: ViewModel() {
    private var initTime = 60_000L // デフォルト 60秒
    private var totalTime by mutableLongStateOf(initTime) // カウント
    private var timeLeft by mutableLongStateOf(initTime) // 残り時間
    private var timer: Job? = null // タイマー用 Job
    private var state by mutableStateOf(TimerState.STOPPED) // 動作状況
    val isRunning get() = state == TimerState.RUNNING // 稼働中かどうか
    val progress get() = timeLeft / totalTime.toFloat() // 進捗状況

    /// 残り時間を計算
    val timeLeftText: String
        get() {
            val seconds = (timeLeft / 1000) % 60
            val minutes = (timeLeft / 1000) / 60
            return String.format(Locale.JAPANESE, "%02d:%02d", minutes, seconds)
        }

    // 合計時間を計算
    val totalTimeText: String
        get() {
            val seconds = (totalTime / 1000) % 60
            val minutes = (totalTime / 1000) / 60
            return String.format(Locale.JAPANESE, "%02d:%02d", minutes, seconds)
        }

    // カウントダウン用のダミー実装
    fun countDown() {
        timer?.cancel()
        state = TimerState.RUNNING
        timer = viewModelScope.launch {
            while (timeLeft > 0 && isRunning){
                delay(100)
                timeLeft -= 100
            }
            if (timeLeft <= 0){
                timeLeft = 0
                state = TimerState.STOPPED
            }
        }
    }

    // タイマーリセット
    fun resetTimer() {
        totalTime = initTime
        timeLeft = initTime
    }
}