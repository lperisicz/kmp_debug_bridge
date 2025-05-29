package adb

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal object PairingSession {

    var device: String = generateRandomPasscode()
        private set
    var passcode: String = generateRandomPasscode()
        private set
    private val state = MutableStateFlow(PairingState.IDLE)

    fun initialise() {
        device = generateRandomDevice()
        passcode = generateRandomPasscode()
        updateState(PairingState.IDLE)
    }

    fun updateState(newState: PairingState) = state.update { newState }

    fun state(): PairingState = state.value
}

internal enum class PairingState {

    IDLE,
    PAIRING,
    CONNECTING,
    CONNECTED,
    ERROR;
}

private const val PASSCODE_LENGTH = 8
private const val DEVICE_NAME_PREFIX = "ADBTool-"

private fun generateRandomDevice(): String = DEVICE_NAME_PREFIX + generateRandomSequence()

private fun generateRandomPasscode(): String = generateRandomSequence()

private fun generateRandomSequence(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789" +
            "!@#\$%^&*()-_=+<>?"

    return (1..PASSCODE_LENGTH)
        .map { chars.random() }
        .joinToString("")
}
