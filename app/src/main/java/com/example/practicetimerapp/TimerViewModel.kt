package com.example.practicetimerapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.ViewModel
import java.util.Locale

class TimerViewModel: ViewModel() {
    private var initTime = 60_000L // デフォルト 60秒
    private var totalTime by mutableLongStateOf(initTime) // カウント
    private var timeLeft by mutableLongStateOf(initTime) // 残り時間
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
        timeLeft -= 1000
    }

    // タイマーリセット
    fun resetTimer() {
        totalTime = initTime
        timeLeft = initTime
    }
}