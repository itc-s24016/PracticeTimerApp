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
class TimerViewModel : ViewModel() {
    private var initTime = 60_000L // デフォルト 60秒
    private var totalTime by mutableLongStateOf(initTime) // カウント
    private var timeLeft by mutableLongStateOf(initTime) // 残り時間
    private var timer: Job? = null // タイマー用 Job
    private var state by mutableStateOf(TimerState.STOPPED) // 動作状況
    val isRunning get() = state == TimerState.RUNNING // 稼働中かどうか
    val progress get() = timeLeft / totalTime.toFloat() // 進捗状況
    var finish by mutableStateOf(false)
        private set

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

    // 一時停止
    fun stateOrPauseTimer() {
        when (state) {
            TimerState.STOPPED, TimerState.PAUSED -> {
                countDown()
            }
            TimerState.RUNNING -> {
                state = TimerState.PAUSED
            }
        }
    }

    // カウントダウン
    private fun countDown() {
        timer?.cancel()
        state = TimerState.RUNNING
        timer = viewModelScope.launch {
            while (timeLeft > 0 && isRunning) {
                delay(100)
                timeLeft -= 100
            }
            if (timeLeft <= 0) {
                timeLeft = 0
                state = TimerState.STOPPED
                finish = true
            }
        }
    }

    // タイマーリセット
    fun resetTimer() {
        state = TimerState.STOPPED
        timer?.cancel()
        timer = null
        totalTime = initTime
        timeLeft = initTime
    }

    fun applyFinish() {
        timeLeft = totalTime
        finish = false
    }
}