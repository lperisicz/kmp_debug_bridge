package settings

import adb.usecase.GetAvailableDevicesUseCase
import kotlinx.coroutines.flow.first
import record.GetRecordingSessionUseCase

internal interface UpdateSelectedDeviceUseCase {

    suspend operator fun invoke()
}

internal class UpdateSelectedDeviceUseCaseImpl(
    private val getAvailableDevicesUseCase: GetAvailableDevicesUseCase,
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val setSessionDeviceUseCase: SetSessionDeviceUseCase,
) : UpdateSelectedDeviceUseCase {

    override suspend fun invoke() {
        val availableDevices = getAvailableDevicesUseCase()
        val session = getRecordingSessionUseCase().first()

        if (availableDevices.contains(session.device)) {
            return
        }

        setSessionDeviceUseCase(availableDevices.firstOrNull())
    }
}
