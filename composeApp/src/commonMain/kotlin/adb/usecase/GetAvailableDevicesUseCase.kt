package adb.usecase

import adb.ConnectedDevice
import externalcommand.use
import java.io.BufferedReader
import java.io.InputStreamReader

internal interface GetAvailableDevicesUseCase {

    suspend operator fun invoke(): List<ConnectedDevice>
}

internal class GetAvailableDevicesUseCaseImpl : GetAvailableDevicesUseCase {

    private val idRegex = Regex("""^(.*?)\s+device$""")

    override suspend fun invoke(): List<ConnectedDevice> =
        getDeviceIds().map { deviceId ->
            ConnectedDevice(
                id = deviceId,
                name = getPresentableDeviceName(deviceId) + " " + getDeviceApiLevel(deviceId)
            )
        }

    private suspend fun getDeviceIds(): List<String> =
        ProcessBuilder("adb", "devices")
            .redirectErrorStream(true)
            .use { process ->
                BufferedReader(
                    InputStreamReader(
                        process.inputStream
                    )
                )
                    .readText()
                    .trim()
                    .lines()
                    .drop(1)
                    .mapNotNull {
                        idRegex
                            .find(it)
                            ?.groupValues
                            ?.getOrNull(1)
                    }
            }

    private suspend inline fun getPresentableDeviceName(deviceId: String): String =
        ProcessBuilder("adb", "-s", deviceId, "shell", "getprop", "ro.product.model")
            .redirectErrorStream(true)
            .use { process ->
                BufferedReader(
                    InputStreamReader(
                        process.inputStream
                    )
                ).readLine()
            }

    private suspend inline fun getDeviceApiLevel(deviceId: String): String =
        ProcessBuilder("adb", "-s", deviceId, "shell", "getprop", "ro.build.version.sdk")
            .redirectErrorStream(true)
            .use { process ->
                BufferedReader(
                    InputStreamReader(
                        process.inputStream
                    )
                ).readLine()
            }.toPresentable()
}

private fun String.toPresentable(): String =
    "Android ${getAndroidVersion(this.toInt())} ($this)"

fun getAndroidVersion(apiLevel: Int): String {
    return when (apiLevel) {
        23 -> "6.0"
        24 -> "7.0"
        25 -> "7.1"
        26 -> "8.0"
        27 -> "8.1"
        28 -> "9"
        29 -> "10"
        30 -> "11"
        31 -> "12"
        32 -> "12L"
        33 -> "13"
        34 -> "14"
        else -> "Unknown API Level ($apiLevel)"
    }
}
